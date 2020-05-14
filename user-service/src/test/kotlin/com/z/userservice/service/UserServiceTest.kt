package com.z.userservice.service

import com.z.jwt.domain.UserRoles
import com.z.userservice.dao.UserDao
import com.z.userservice.domain.User
import com.z.userservice.dto.AddUserRequest
import com.z.userservice.dto.UpdateUserRequest
import com.z.userservice.dto.UserResponse
import com.z.userservice.transformer.AddUserRequestTransformer
import com.z.userservice.transformer.UserDetailsTransformer
import com.z.userservice.transformer.UserTransformer
import com.z.userservice.utils.UserTestUtils.buildUserDetails
import com.z.userservice.utils.UserTestUtils.buildUserResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.mockito.Mockito.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import javax.validation.ConstraintViolationException
import javax.validation.Validation


@DisplayName("UserService unit tests")
@TestInstance(PER_CLASS)
class UserServiceTest {

    private val validator = Validation.buildDefaultValidatorFactory().validator //static =(
    private lateinit var mockUserDao: UserDao
    private lateinit var mockUserTransformer: UserTransformer
    private lateinit var mockAddUserTransformer: AddUserRequestTransformer
    private lateinit var mockUserDetailsTransformer: UserDetailsTransformer
    private lateinit var  mockPasswordEncoder: PasswordEncoder
    private lateinit var mockApplicationEventPublisher: ApplicationEventPublisher
    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        mockUserDao = mock(UserDao::class.java)
        mockUserTransformer = mock(UserTransformer::class.java)
        mockAddUserTransformer = mock(AddUserRequestTransformer::class.java)
        mockUserDetailsTransformer = mock(UserDetailsTransformer::class.java)
        mockApplicationEventPublisher = mock(ApplicationEventPublisher::class.java)
        mockPasswordEncoder = mock(PasswordEncoder::class.java)

        userService = UserService(
                userDao = mockUserDao, userDetailsTransformer = mockUserDetailsTransformer,
                userTransformer = mockUserTransformer, addUserRequestTransformer = mockAddUserTransformer,
                passwordEncoder = mockPasswordEncoder, validator = validator, applicationEventPublisher = mockApplicationEventPublisher)
    }

    @Test
    fun `loadUserByUsername(username) with existent username should return UserDetails`() {
        val username = "test"
        val user = User()
        val optionalUser: Optional<User> = Optional.of(user)
        val userDetails = buildUserDetails()

        `when`(mockUserDao.findByName(username)).thenReturn(optionalUser)
        `when`(mockUserDetailsTransformer.transform(user)).thenReturn(userDetails)
        val result = userService.loadUserByUsername(username)

        verify(mockUserDao).findByName(username)
        verify(mockUserDetailsTransformer).transform(user)
        verifyNoInteractions(mockAddUserTransformer, mockPasswordEncoder, mockUserTransformer)
        assertThat(result, `is`(userDetails))
    }

    @Test
    fun `loadUserByUsername(username) with nonexistent username should throw UsernameNotFoundException`() {
        val username = "test"
        val empty: Optional<User> = Optional.empty()

        `when`(mockUserDao.findByName(username)).thenReturn(empty)

        val exception = assertThrows(UsernameNotFoundException::class.java) {
            userService.loadUserByUsername(username)
        }

        verify(mockUserDao).findByName(username)
        verifyNoInteractions(mockAddUserTransformer, mockUserDetailsTransformer, mockPasswordEncoder, mockUserTransformer)
        assertThat(exception.localizedMessage, `is`("user $username not found"))
    }

    @Test
    fun `findById(id) with existent id should return UserResponse`() {
        val userId = 1L
        val optionalUser = Optional.of(User(id = userId))
        val userResponse = buildUserResponse(id = userId)

        `when`(mockUserDao.findById(userId)).thenReturn(optionalUser)
        `when`(mockUserTransformer.transform(optionalUser.get())).thenReturn(userResponse)

        val result:UserResponse = userService.findById(userId)

        verify(mockUserDao).findById(userId)
        verify(mockUserTransformer).transform(optionalUser.get())
        verifyNoInteractions(mockAddUserTransformer, mockUserDetailsTransformer, mockPasswordEncoder)
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
        val result:Page<UserResponse> = userService.findAll(pageRequest)

        verify(mockUserDao).findAll(pageRequest)
        verify(mockUserTransformer).transform(user)
        verifyNoInteractions(mockAddUserTransformer, mockUserDetailsTransformer, mockPasswordEncoder)
        assertThat(result, notNullValue())
        assertThat(result.totalElements, `is`(1L))
        assertThat(result.isFirst, `is`(true))
        assertThat(result.content, hasItem(userResponse))
    }

    @Test
    fun `save(addUserRequest) with valid request should encrypt the user's password, call the dao layer and return UserResponse`() {
        val username = "tester"
        val nonEncodedPassword = "nonEncryptedPassword"
        val email = "test@test.com"
        val addUserRequest = AddUserRequest(password = nonEncodedPassword, username = username, email = email)
        val passwordEncoded = "\$2a\$10\$ztBuLyQC9g8iGcS5w46RmeiTvnq8AVmo7KEVjIiMt8/OYOBihYRcG"
        val user =  User(password = nonEncodedPassword)
        val userResponse = buildUserResponse()

        `when`(mockAddUserTransformer.transform(addUserRequest)).thenReturn(user)
        `when`(mockPasswordEncoder.encode(nonEncodedPassword)).thenReturn(passwordEncoded)
        `when`(mockUserDao.save(user)).thenReturn(user)
        `when`(mockUserTransformer.transform(user)).thenReturn(userResponse)
        val response:UserResponse = userService.save(addUserRequest)

        verify(mockAddUserTransformer).transform(addUserRequest)
        verify(mockPasswordEncoder).encode(nonEncodedPassword)
        verify(mockUserDao).save(user)
        verify(mockUserTransformer).transform(user)
        verifyNoInteractions(mockUserDetailsTransformer)
        assertThat(response, `is`(userResponse))
        assertThat(user.password, `is`(passwordEncoded))
    }

    @Test
    fun `save(addUserRequest) with non valid request should throw ConstraintViolation`() {
        val username = "t"
        val nonEncodedPassword = "t"
        val email = "invalid"
        val addUserRequest = AddUserRequest(password = nonEncodedPassword, username = username, email = email)

        val exception = assertThrows(ConstraintViolationException::class.java) {
            userService.save(addUserRequest)
        }

        verifyNoInteractions(mockUserDetailsTransformer, mockAddUserTransformer, mockPasswordEncoder, mockUserDao, mockUserTransformer)
        val violationFieldNames = exception.constraintViolations.map { it.propertyPath.toString() }
        assertThat(violationFieldNames, hasSize(3))
        assertThat(violationFieldNames, containsInAnyOrder("username", "password", "email"))
    }

    @Test
    fun `update(addUserResponse) with valid request should update the user's fields`() { //TODO combinations
        val id = 1L
        val nonEncodedPassword = "newPassword"
        val passwordEncoded = "\$2a\$10\$ztBuLyQC9g8iGcS5w46RmeiTvnq8AVmo7KEVjIiMt8/OYOBihYRcG"
        val state = false
        val roles = UserRoles.values().toSet()
        val request = UpdateUserRequest(id = id, password = nonEncodedPassword, state = state, roles = roles)
        val user = User(id = id)

        `when`(mockUserDao.findById(request.id)).thenReturn(Optional.of(user))
        `when`(mockPasswordEncoder.encode(nonEncodedPassword)).thenReturn(passwordEncoded)
        `when`(mockUserDao.save(user)).thenReturn(user)
        `when`(mockUserTransformer.transform(user)).thenReturn(buildUserResponse())
        userService.update(request)

        verifyNoInteractions(mockUserDetailsTransformer, mockAddUserTransformer)
        assertThat(user.id, `is`(id))
        assertThat(user.state, `is`(state))
        assertThat(user.password, `is`(passwordEncoded))
        assertThat(user.roles, equalTo(roles))
    }

    @Test
    fun `existsByName(username) should return true if username exists`() {
        val name = "test"
        val exists = true

        `when`(mockUserDao.existsByName(name)).thenReturn(exists)
        val result = userService.existsByName(name)

        verify(mockUserDao).existsByName(name)
        verifyNoInteractions(mockUserDetailsTransformer, mockAddUserTransformer, mockPasswordEncoder)
        assertThat(result, `is`(exists))
    }
}
