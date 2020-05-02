package com.z.zcoreblocking.utils.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

object AuthenticationUtils {
    fun getCurrentAuthentication(): Authentication {
        return SecurityContextHolder.getContext()?.authentication
                ?: UsernamePasswordAuthenticationToken("anonymous","")
    }
}