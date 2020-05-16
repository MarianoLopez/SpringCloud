package com.z.userservice.service.authentication

import com.z.jwt.dto.TokenResponse
import com.z.jwt.service.JwtSpringService
import com.z.userservice.dto.LoginRequest
import org.springframework.stereotype.Service

@Service
class LoginService(private val jwtService: JwtSpringService,
                   private val authenticationManager: UserAuthenticationManager) {
    fun authenticate(loginRequest: LoginRequest): TokenResponse {
        return this.authenticationManager
                .authenticate(loginRequest.toAuthentication())
                .let { jwtService.createAuthenticationToken(it) }
    }
}