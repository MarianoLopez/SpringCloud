package com.z.userservice.utils

import com.z.jwt.domain.UserRoles
import com.z.userservice.dto.UserResponse
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

object UserTestUtils {
    private const val ID = 1L
    private const val NAME = "Mariano"
    private const val STATE = true
    private const val EMAIL = "test@test.com"
    private const val SERVICE = "USER-SERVICE"
    private val ROLES = UserRoles.values().toSet()

    fun buildUserResponse(id:Long = ID, name:String = NAME, state:Boolean = STATE,
                          email:String = EMAIL, service:String = SERVICE, roles:Set<UserRoles> = ROLES,
                          dateTime:LocalDateTime = LocalDateTime.now()): UserResponse {
        return UserResponse(
                id = id, name = name, state = state, roles = roles, email = email,
                createdDate = dateTime, createdBy = service, modifiedBy = service,
                lastModifiedDate = dateTime
        )
    }

    fun buildUserDetails(username: String = NAME, password: String = NAME, roles: Set<UserRoles> = ROLES): UserDetails {
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(password)
                .authorities(roles.map { SimpleGrantedAuthority(it.name)})
                .build()
    }
}