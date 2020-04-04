package com.z.jwt.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.z.jwt.utils.defaultDateTimeFormat
import java.time.LocalDateTime

data class TokenResponse(
    val token:String,
    @get:JsonFormat(pattern = defaultDateTimeFormat)
    val expires: LocalDateTime,
    val claims:Map<String,Array<String>> = emptyMap(),
    val subject:String,
    val enabled:Boolean = true
)