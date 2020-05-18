package com.z.userservice.service.signup

import com.z.userservice.dto.AddUserRequest

interface UserSignUp {
    fun add(addUserRequest: AddUserRequest)

    fun confirm(token: String)
}