package com.z.userservice.service.user

import com.z.userservice.dao.UserDao
import com.z.userservice.domain.User
import com.z.userservice.dto.UserResponse
import com.z.userservice.transformer.UserTransformer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class UserSearchService (private val userDao: UserDao,
                         private val userTransformer: UserTransformer ): UserSearch {

    override fun findById(id: Long): UserResponse = this.userTransformer.transform(this.findUserById(id))

    override fun findUserById(id: Long): User = this.userDao.findById(id).orElseThrow { EntityNotFoundException() }

    override fun findAllByUsernameOrEmail(pageable: Pageable, usernameOrEmail: String): Page<UserResponse> {
        return this.userDao.findAllByNameOrEmailContaining(usernameOrEmail, pageable)
                .map { userTransformer.transform(it) }
    }

    override fun findAll(pageable: Pageable): Page<UserResponse> {
        return this.userDao.findAll(pageable).map { userTransformer.transform(it) }
    }

    override fun existsByName(name: String): Boolean = this.userDao.existsByName(name)
}