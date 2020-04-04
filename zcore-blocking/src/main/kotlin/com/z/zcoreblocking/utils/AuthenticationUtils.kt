package com.z.zcoreblocking.utils

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

object AuthenticationUtils {
    fun getCurrentAuthentication(): Authentication {
        return SecurityContextHolder.getContext().authentication
    }
}