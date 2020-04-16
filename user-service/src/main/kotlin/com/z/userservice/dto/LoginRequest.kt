package com.z.userservice.dto

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication


data class LoginRequest(val username:String, val password:String) {
    fun toAuthentication(): Authentication = UsernamePasswordAuthenticationToken(this.username, this.password, emptyList())
}