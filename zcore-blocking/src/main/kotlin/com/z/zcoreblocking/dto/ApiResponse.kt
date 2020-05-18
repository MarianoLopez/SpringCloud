package com.z.zcoreblocking.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.z.jwt.utils.defaultDateTimeFormat
import java.time.LocalDateTime

data class ApiResponse(
        val title:String,
        @field:JsonFormat(pattern = defaultDateTimeFormat)
        val date: LocalDateTime = LocalDateTime.now(),
        val payload: Any = Any()
)