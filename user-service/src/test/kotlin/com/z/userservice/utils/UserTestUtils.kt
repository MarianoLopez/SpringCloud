package com.z.userservice.utils

import com.z.jwt.domain.UserRoles
import com.z.userservice.dto.UserResponse
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

object UserTestUtils {

    fun buildUserResponse(id:Long = 1L, name:String = "Mariano",
                          state:Boolean = true, service:String = "USER-SERVICE",
                          roles:Set<UserRoles> = UserRoles.values().toSet(),
                          dateTime:LocalDateTime = LocalDateTime.now()): UserResponse {
        return UserResponse(
                id = id, name = name, state = state, roles = roles,
                createdDate = dateTime, createdBy = service, modifiedBy = service,
                lastModifiedDate = dateTime
        )
    }

    fun buildUserDetails(): UserDetails {
        val test = "Mariano"
        return org.springframework.security.core.userdetails.User.builder()
                .username(test)
                .password(test)
                .authorities(SimpleGrantedAuthority(UserRoles.USER.name))
                .build()
    }
}