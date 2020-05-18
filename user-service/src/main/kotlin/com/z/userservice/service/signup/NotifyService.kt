package com.z.userservice.service.signup

import com.z.userservice.dao.ConfirmationDao
import com.z.userservice.domain.ConfirmationStatus
import com.z.userservice.domain.UserConfirmation
import com.z.userservice.domain.UserConfirmationId
import com.z.userservice.dto.UserResponse
import com.z.zcoreblocking.dto.property.RabbitMqProperties
import com.z.zcoreblocking.dto.queue.UserConfirmationToken
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpException
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation.REQUIRES_NEW
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class NotifyService (
        private val confirmationDao: ConfirmationDao,
        private val confirmationTokenService: ConfirmationTokenService,
        private val rabbitTemplate: RabbitTemplate,
        private val rabbitMqProperties: RabbitMqProperties) {
     private val logger = LoggerFactory.getLogger(NotifyService::class.java)

    @Transactional(propagation = REQUIRES_NEW)
     fun notifyNewUser(userResponse: UserResponse) {
        val userConfirmationToken = createUserConfirmationToken(id = userResponse.id, username = userResponse.name,
                email = userResponse.email)
        val status: ConfirmationStatus = this.publishUserConfirmationToken(userConfirmationToken)

        logger.debug("userConfirmationToken: $userConfirmationToken, status: $status")
        confirmationDao.save(createUserConfirmation(userConfirmationToken, status))
    }

    private fun publishUserConfirmationToken(userConfirmationToken: UserConfirmationToken): ConfirmationStatus {
        return try {
            rabbitTemplate.convertAndSend(rabbitMqProperties.exchange, rabbitMqProperties.routingKey, userConfirmationToken)
            ConfirmationStatus.SENT
        } catch (amqpException: AmqpException) {
            logger.error(amqpException.localizedMessage)
            ConfirmationStatus.NOT_SENT
        }
    }

    @Scheduled(fixedDelayString = "\${fixedDelay.in.ms:30000}")
    fun schedulePublish() {
        logger.debug("Running schedulePublish for all not sent messages")
        confirmationDao.findAllNotSent().forEach(this::reSend)
    }

    @Transactional
    fun reSend(userConfirmation: UserConfirmation) {
        val status: ConfirmationStatus = this.publishUserConfirmationToken(createUserConfirmationToken(
                id = userConfirmation.id.userId,
                username = userConfirmation.username,
                email = userConfirmation.email
        ))
        userConfirmation.apply {
            if(this.id.status != status) {
                this.id.status = status
            }
            this.date = LocalDateTime.now()
        }
        logger.debug("ConfirmationService.update: $userConfirmation")
        confirmationDao.save(userConfirmation)
    }

    private fun createUserConfirmationToken(id:Long, username:String, email:String): UserConfirmationToken {
        val tokenResponse = confirmationTokenService.createConfirmationToken(id)
        return UserConfirmationToken(userId = id, username = username, email = email, token = tokenResponse.token)
    }

    private fun createUserConfirmation(userConfirmationToken: UserConfirmationToken,
                                       confirmationStatus: ConfirmationStatus): UserConfirmation {
        return  UserConfirmation(
                id = UserConfirmationId(userId = userConfirmationToken.userId, status = confirmationStatus),
                username = userConfirmationToken.username,
                email = userConfirmationToken.email)
    }
}