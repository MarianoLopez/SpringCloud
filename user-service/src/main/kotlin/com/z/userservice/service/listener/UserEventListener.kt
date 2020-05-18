package com.z.userservice.service.listener

import com.z.userservice.dto.event.AddUserEvent
import com.z.userservice.service.signup.NotifyService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class UserEventListener(private val notifyService: NotifyService) {
    private val logger = LoggerFactory.getLogger(UserEventListener::class.java)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleUserEvent(addUserEvent: AddUserEvent) {
        logger.debug("TransactionalEventListener -> $addUserEvent")
        notifyService.notifyNewUser(addUserEvent.data)
    }
}