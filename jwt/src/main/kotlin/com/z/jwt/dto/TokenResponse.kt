package com.z.jwt.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.z.jwt.utils.defaultDateTimeFormat
import java.time.LocalDateTime

data class TokenResponse(
        var token:String = "",
        @get:JsonFormat(pattern = defaultDateTimeFormat)
        var expires: LocalDateTime = LocalDateTime.now(),
        var claims:Map<String,Array<String>> = emptyMap(),
        var subject:String = "",
        var enabled:Boolean = true
)