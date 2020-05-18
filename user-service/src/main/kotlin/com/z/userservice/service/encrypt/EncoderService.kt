package com.z.userservice.service.encrypt

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class EncoderService(private val passwordEncoder: PasswordEncoder) {

    fun encodeIfNotEmpty(value: String?): String {
        return if(value != null && value.isNotBlank()) passwordEncoder.encode(value) else ""
    }

}