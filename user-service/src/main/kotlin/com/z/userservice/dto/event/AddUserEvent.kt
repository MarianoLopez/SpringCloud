package com.z.userservice.dto.event

import com.z.userservice.dto.UserResponse
import org.springframework.context.ApplicationEvent

data class AddUserEvent(val data: UserResponse): ApplicationEvent(data)