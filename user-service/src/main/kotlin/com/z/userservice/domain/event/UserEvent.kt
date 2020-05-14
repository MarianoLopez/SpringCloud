package com.z.userservice.domain.event

import org.springframework.context.ApplicationEvent

data class UserEvent(val message: String = "", val type: UserEventType, val data: Any): ApplicationEvent(data)