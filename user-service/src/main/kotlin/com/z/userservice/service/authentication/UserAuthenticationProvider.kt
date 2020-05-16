package com.z.userservice.service.authentication

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserAuthenticationProvider(@Qualifier("userManagementService")
                                 private val userDetailsService: UserDetailsService,
                                 private val passwordEncoder: PasswordEncoder): AuthenticationProvider {
    private val logger: Logger = LoggerFactory.getLogger(UserAuthenticationProvider::class.java)

    override fun authenticate(auth: Authentication): Authentication {
        val name = auth.name
        val password = auth.credentials.toString()
        logger.debug("UserAuthenticationProvider authenticate($name)")

        return this.userDetailsService.loadUserByUsername(name)
            .apply {
                when {
                    !this.isEnabled -> throw DisabledException("user $name is disabled")
                    !passwordEncoder.matches(password,this.password) -> throw BadCredentialsException("Bad credentials")
                }
            }.let {
                UsernamePasswordAuthenticationToken(name, password, it.authorities)
            }
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}