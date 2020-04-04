package com.z.jwt.service

import com.z.jwt.dto.TokenRequest
import com.z.jwt.dto.TokenResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl

class JwtSpringService(private val jwtService: JwtService) {
    private val authoritiesKey = "authorities"
    val prefix = jwtService.jwtProperties.prefix

    fun createAuthenticationToken(authentication: Authentication): TokenResponse {
        return jwtService.createToken(
            TokenRequest(
                subject = authentication.name,
                claims =  mapOf(authoritiesKey to authentication.authorities.map {role-> role.authority }.toTypedArray())
            )
        )
    }

    fun getAuthentication(token:String): SecurityContext {
        return jwtService.decode(token).let {
            SecurityContextImpl(
                UsernamePasswordAuthenticationToken(
                    it.subject, "*****",
                    it.claims[authoritiesKey]?.map { role -> SimpleGrantedAuthority(role) }
                )
            )
        }
    }
}