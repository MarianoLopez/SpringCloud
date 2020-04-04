package com.z.userservice.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.z.jwt.domain.UserRoles
import com.z.jwt.utils.defaultDateTimeFormat
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val name:String,
    val state:Boolean,
    val roles:Set<UserRoles>,
    @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = defaultDateTimeFormat)
    val createdDate: LocalDateTime,
    @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = defaultDateTimeFormat)
    val lastModifiedDate: LocalDateTime,
    val createdBy: String,
    val modifiedBy: String
)