package com.z.userservice.dto.event

import com.z.zcoreblocking.dto.queue.UserConfirmationToken
import org.springframework.context.ApplicationEvent

data class AddUserEvent(val message: String = "", val data: UserConfirmationToken): ApplicationEvent(data)