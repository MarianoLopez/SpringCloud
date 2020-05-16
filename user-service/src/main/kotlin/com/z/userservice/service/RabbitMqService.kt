package com.z.userservice.service

import com.z.zcoreblocking.dto.property.RabbitMqProperties
import com.z.zcoreblocking.dto.queue.UserConfirmationToken
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpException
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RabbitMqService (private val rabbitTemplate: RabbitTemplate,
                       private val rabbitMqProperties: RabbitMqProperties) {
    private val logger = LoggerFactory.getLogger(RabbitMqService::class.java)

    fun publishUserConfirmationToken(userConfirmationToken: UserConfirmationToken) {
        try {
            rabbitTemplate.convertAndSend(rabbitMqProperties.exchange, rabbitMqProperties.routingKey, userConfirmationToken)
        } catch (amqpException: AmqpException) {
            logger.error(amqpException.localizedMessage)
        }
    }
}