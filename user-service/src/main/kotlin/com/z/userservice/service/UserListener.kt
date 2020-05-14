package com.z.userservice.service

import com.z.userservice.domain.event.UserEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class UserListener(private val rabbitMqService: RabbitMqService) {
    private val logger = LoggerFactory.getLogger(UserListener::class.java)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    fun handleUserEvent(userEvent: UserEvent) {
        logger.debug("handleUserEvent -> $userEvent")
        rabbitMqService.publish(userEvent)
    }
}