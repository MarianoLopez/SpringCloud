package com.z.userservice.service.user

import com.z.userservice.dto.AddUserRequest

interface UserSignUp {
    fun add(addUserRequest: AddUserRequest)

    fun confirm(token: String)
}