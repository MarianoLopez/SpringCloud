package com.z.userservice.dao

import com.z.userservice.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserRepository {
    fun findAllByNameOrEmailContaining(nameOrEmail: String, pageable: Pageable): Page<User>
}