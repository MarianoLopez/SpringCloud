package com.z.userservice.dao

import com.z.userservice.domain.User
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.PersistenceException


@DataJpaTest
class UserDaoTest {

    @Autowired
    private lateinit var userDao: UserDao
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Test
    fun `findAll(pageRequest) should return Page of User`() {
        val pageRequest = PageRequest.of(1, 10)

        val userPage = userDao.findAll(pageRequest)

        assertThat(userPage, notNullValue())
        assertThat(userPage.totalElements, `is`(1L))
        assertThat(userPage.number, `is`(pageRequest.pageNumber))
        assertThat(userPage.size, `is`(pageRequest.pageSize))
    }

    @Test
    fun `findById(1) entity should be present`() {
        val userId = 1L
        val user = userDao.findById(userId)
        assertThat(user.isPresent, `is`(true))
    }

    @Test
    fun `findById(-1) entity shouldn't be present`() {
        val userId = -1L
        val user = userDao.findById(userId)
        assertThat(user.isPresent, `is`(false))
    }

    @Test
    fun `save(User) insert into user, user_roles and sequence call should be done successfully`(){
        val user = userDao.save(User())
        entityManager.flush()
        assertThat(user, notNullValue())
        assertThat(user.id, notNullValue())
    }

    @Test
    fun `save(User) with duplicated name should throw PersistenceException by unique constraint`(){
        val exception = assertThrows(PersistenceException::class.java) {
            userDao.save(User(name = "Mariano"))
            entityManager.flush()
        }
        assertThat(exception.message, containsString("could not execute statement"))
    }
}