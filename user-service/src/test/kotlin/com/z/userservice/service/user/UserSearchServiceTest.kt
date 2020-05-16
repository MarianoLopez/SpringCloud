package com.z.userservice.service.user

import com.z.userservice.dao.UserDao
import com.z.userservice.domain.User
import com.z.userservice.dto.UserResponse
import com.z.userservice.transformer.UserTransformer
import com.z.userservice.utils.UserTestUtils.buildUserResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@DisplayName("User Search Service Unit Tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserSearchServiceTest {
    private lateinit var mockUserDao: UserDao
    private lateinit var mockUserTransformer: UserTransformer
    private lateinit var userSearch: UserSearch

    @BeforeEach
    fun setup() {
        mockUserDao = mock(UserDao::class.java)
        mockUserTransformer = mock(UserTransformer::class.java)

        userSearch = UserSearchService(userDao = mockUserDao, userTransformer = mockUserTransformer)
    }


    @Test
    fun `findById(id) with existent id should return UserResponse`() {
        val userId = 1L
        val optionalUser = Optional.of(User(id = userId))
        val userResponse = buildUserResponse(id = userId)

        `when`(mockUserDao.findById(userId)).thenReturn(optionalUser)
        `when`(mockUserTransformer.transform(optionalUser.get())).thenReturn(userResponse)

        val result: UserResponse = userSearch.findById(userId)

        verify(mockUserDao).findById(userId)
        verify(mockUserTransformer).transform(optionalUser.get())
        assertThat(result, `is`(userResponse))
    }

    @Test
    fun `findAll(pageable) should return an UserResponse Page`() {
        val pageRequest = PageRequest.of(1, 20)
        val user = User()
        val userPage: Page<User> = PageImpl(listOf(user))
        val userResponse = buildUserResponse()

        `when`(mockUserDao.findAll(pageRequest)).thenReturn(userPage)
        `when`(mockUserTransformer.transform(user)).thenReturn(userResponse)
        val result:Page<UserResponse> = userSearch.findAll(pageRequest)

        verify(mockUserDao).findAll(pageRequest)
        verify(mockUserTransformer).transform(user)
        assertThat(result, notNullValue())
        assertThat(result.totalElements, `is`(1L))
        assertThat(result.isFirst, `is`(true))
        assertThat(result.content, hasItem(userResponse))
    }




    @Test
    fun `existsByName(username) should return true if username exists`() {
        val name = "test"
        val exists = true

        `when`(mockUserDao.existsByName(name)).thenReturn(exists)
        val result = userSearch.existsByName(name)

        verify(mockUserDao).existsByName(name)
        verifyNoInteractions(mockUserTransformer)
        assertThat(result, `is`(exists))
    }
}