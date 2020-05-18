package com.z.userservice.service.management

import com.z.jwt.domain.UserRoles
import com.z.userservice.dao.UserDao
import com.z.userservice.domain.User
import com.z.userservice.dto.UpdateUserRequest
import com.z.userservice.service.encrypt.EncoderService
import com.z.userservice.transformer.AddUserRequestTransformer
import com.z.userservice.transformer.UserDetailsTransformer
import com.z.userservice.transformer.UserTransformer
import com.z.userservice.utils.UserTestUtils.buildUserDetails
import com.z.userservice.utils.UserTestUtils.buildUserResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.mockito.Mockito.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*
import javax.validation.Validation


@DisplayName("User Management Service Unit Tests")
@TestInstance(PER_CLASS)
class UserManagementServiceTest {

    private val validator = Validation.buildDefaultValidatorFactory().validator //static =(
    private lateinit var mockUserDao: UserDao
    private lateinit var mockUserTransformer: UserTransformer
    private lateinit var mockAddUserTransformer: AddUserRequestTransformer
    private lateinit var mockUserDetailsTransformer: UserDetailsTransformer
    private lateinit var mockEncoderService: EncoderService
    private lateinit var mockApplicationEventPublisher: ApplicationEventPublisher
    private lateinit var mockUserSearch: UserSearch
    private lateinit var userManagementService: UserManagementService

    @BeforeEach
    fun setup() {
        mockUserDao = mock(UserDao::class.java)
        mockUserTransformer = mock(UserTransformer::class.java)
        mockAddUserTransformer = mock(AddUserRequestTransformer::class.java)
        mockUserDetailsTransformer = mock(UserDetailsTransformer::class.java)
        mockApplicationEventPublisher = mock(ApplicationEventPublisher::class.java)
        mockEncoderService = mock(EncoderService::class.java)
        mockUserSearch = mock(UserSearch::class.java)

        userManagementService = UserManagementService(
                userDao = mockUserDao, userDetailsTransformer = mockUserDetailsTransformer,
                userTransformer = mockUserTransformer, encoderService = mockEncoderService,
                validator = validator, userSearchService = mockUserSearch)
    }

    @Test
    fun `loadUserByUsername(username) with existent username should return UserDetails`() {
        val username = "test"
        val user = User()
        val optionalUser: Optional<User> = Optional.of(user)
        val userDetails = buildUserDetails()

        `when`(mockUserDao.findByName(username)).thenReturn(optionalUser)
        `when`(mockUserDetailsTransformer.transform(user)).thenReturn(userDetails)
        val result = userManagementService.loadUserByUsername(username)

        verify(mockUserDao).findByName(username)
        verify(mockUserDetailsTransformer).transform(user)
        verifyNoInteractions(mockAddUserTransformer, mockEncoderService, mockUserTransformer)
        assertThat(result, `is`(userDetails))
    }

    @Test
    fun `loadUserByUsername(username) with nonexistent username should throw UsernameNotFoundException`() {
        val username = "test"
        val empty: Optional<User> = Optional.empty()

        `when`(mockUserDao.findByName(username)).thenReturn(empty)

        val exception = assertThrows(UsernameNotFoundException::class.java) {
            userManagementService.loadUserByUsername(username)
        }

        verify(mockUserDao).findByName(username)
        verifyNoInteractions(mockAddUserTransformer, mockUserDetailsTransformer, mockEncoderService, mockUserTransformer)
        assertThat(exception.localizedMessage, `is`("user $username not found"))
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

        `when`(mockUserSearch.findUserById(request.id)).thenReturn(user)
        `when`(mockEncoderService.encodeIfNotEmpty(nonEncodedPassword)).thenReturn(passwordEncoded)
        `when`(mockUserDao.save(user)).thenReturn(user)
        `when`(mockUserTransformer.transform(user)).thenReturn(buildUserResponse())
        userManagementService.update(request)

        verifyNoInteractions(mockUserDetailsTransformer, mockAddUserTransformer)
        verify(mockUserSearch).findUserById(request.id)
        verify(mockEncoderService).encodeIfNotEmpty(nonEncodedPassword)
        verify(mockUserDao).save(user)
        verify(mockUserTransformer).transform(user)
        assertThat(user.id, `is`(id))
        assertThat(user.state, `is`(state))
        assertThat(user.password, `is`(passwordEncoded))
        assertThat(user.roles, equalTo(roles))
    }
}
