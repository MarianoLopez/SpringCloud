package com.z.userservice.dao

import com.z.userservice.domain.User
import com.z.userservice.domain.User.Graphs.USER_ROLES_GRAPH
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserDao: PagingAndSortingRepository<User, Long>, UserRepository {
    @EntityGraph(USER_ROLES_GRAPH)
    override fun findAll(pageable: Pageable): Page<User>

    @EntityGraph(USER_ROLES_GRAPH)
    override fun findById(id: Long): Optional<User>

    @EntityGraph(USER_ROLES_GRAPH)
    fun findByName(name:String): Optional<User>

    fun existsByName(name:String): Boolean
}