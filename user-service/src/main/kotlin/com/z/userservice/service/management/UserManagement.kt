package com.z.userservice.service.management

import com.z.userservice.dto.DeleteUserRequest
import com.z.userservice.dto.UpdateUserRequest
import com.z.userservice.dto.UserResponse

interface UserManagement {
    fun update(updateUserRequest: UpdateUserRequest): UserResponse

    fun delete(deleteUserRequest: DeleteUserRequest): UserResponse
}