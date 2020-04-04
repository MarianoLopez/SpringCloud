package com.z.core.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.z.core.utils.defaultDateTimeFormat
import java.time.LocalDateTime

data class ApiResponse(
        val title:String,
        @get:JsonFormat(pattern = defaultDateTimeFormat)
        val date: LocalDateTime = LocalDateTime.now(),
        val payload: Any = Any()
)