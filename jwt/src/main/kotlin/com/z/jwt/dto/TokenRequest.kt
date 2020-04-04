package com.z.jwt.dto

import java.time.LocalDateTime

data class TokenRequest(val subject:String,
                        val claims:Map<String,Array<String>> = emptyMap(),
                        val expiration: LocalDateTime = LocalDateTime.now().plusDays(1L))