package com.z.userservice.dto

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication


data class LoginRequest(val name:String, val password:String) {
    fun toAuthentication(): Authentication = UsernamePasswordAuthenticationToken(this.name, this.password, emptyList())
}