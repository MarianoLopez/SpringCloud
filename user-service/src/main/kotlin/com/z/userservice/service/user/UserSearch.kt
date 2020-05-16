package com.z.userservice.service.user

import com.z.userservice.domain.User
import com.z.userservice.dto.UserResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserSearch {

    fun findById(id:Long): UserResponse

    fun findUserById(id:Long): User

    fun findAllByUsernameOrEmail(pageable: Pageable, usernameOrEmail:String): Page<UserResponse> 

    fun findAll(pageable: Pageable): Page<UserResponse>

    fun existsByName(name:String): Boolean
}