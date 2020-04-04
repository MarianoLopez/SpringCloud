package com.z.userservice.service.authentication

import com.z.userservice.service.authentication.UserAuthenticationProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class UserAuthenticationManager(
		private val userAuthenticationProvider: UserAuthenticationProvider): AuthenticationManager {

	override fun authenticate(auth: Authentication): Authentication {
		return userAuthenticationProvider.authenticate(auth)
	}
}