package com.z.userservice.service.user

import com.z.userservice.dto.event.AddUserEvent
import com.z.userservice.service.RabbitMqService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class UserListener(private val rabbitMqService: RabbitMqService) {
    private val logger = LoggerFactory.getLogger(UserListener::class.java)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    fun handleUserEvent(addUserEvent: AddUserEvent) {
        logger.debug("handleUserEvent -> $addUserEvent")
        rabbitMqService.publishUserConfirmationToken(addUserEvent.data)
    }
}