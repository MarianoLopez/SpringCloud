package com.z.userservice.service.listener

import com.z.userservice.dto.event.AddUserEvent
import com.z.userservice.dto.event.ConfirmUserEvent
import com.z.userservice.service.signup.NotifyService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class UserEventListener(private val notifyService: NotifyService) {
    private val logger = LoggerFactory.getLogger(UserEventListener::class.java)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleAddUserEvent(addUserEvent: AddUserEvent) {
        logger.debug("handleAddUserEvent -> $addUserEvent")
        notifyService.notifyNewUser(addUserEvent.data)
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleConfirmUserEvent(confirmUserEvent: ConfirmUserEvent) {
        logger.debug("handleConfirmUserEvent -> $confirmUserEvent")
        notifyService.confirm(confirmUserEvent.data)
    }
}