package com.z.userservice.service.signup

import com.nhaarman.mockitokotlin2.*
import com.z.userservice.dao.UserDao
import com.z.userservice.domain.User
import com.z.userservice.dto.AddUserRequest
import com.z.userservice.dto.event.AddUserEvent
import com.z.userservice.service.encrypt.EncoderService
import com.z.userservice.service.management.UserManagement
import com.z.userservice.transformer.AddUserRequestTransformer
import com.z.userservice.transformer.UserTransformer
import com.z.userservice.utils.UserTestUtils.buildUserResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.context.ApplicationEventPublisher
import javax.validation.ConstraintViolationException
import javax.validation.Validation


@DisplayName("User Sing up Service Unit Tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserSignUpServiceTest {
    private val validator = Validation.buildDefaultValidatorFactory().validator //static =(
    private lateinit var mockUserDao: UserDao
    private lateinit var mockUserTransformer: UserTransformer
    private lateinit var mockAddUserTransformer: AddUserRequestTransformer
    private lateinit var mockEncoderService: EncoderService
    private lateinit var mockApplicationEventPublisher: ApplicationEventPublisher
    private lateinit var mockConfirmationTokenService: ConfirmationTokenService
    private lateinit var mockUserManagement: UserManagement
    private lateinit var userSignUp: UserSignUp

    @BeforeEach
    fun setup() {
        mockUserDao = mock()
        mockUserTransformer = mock()
        mockAddUserTransformer = mock()
        mockApplicationEventPublisher = mock()
        mockEncoderService = mock()
        mockUserManagement = mock()
        mockConfirmationTokenService = mock()

        userSignUp = UserSignUpService(
                userManagement = mockUserManagement, userDao = mockUserDao,
                addUserRequestTransformer = mockAddUserTransformer, userTransformer = mockUserTransformer,
                encoderService = mockEncoderService, applicationEventPublisher = mockApplicationEventPublisher,
                confirmationTokenService = mockConfirmationTokenService, validator = validator
        )
    }

    @Test
    fun `add(addUserRequest) with valid request should encrypt the user's password, call the dao layer and publish the event`() {
        val username = "tester"
        val nonEncodedPassword = "nonEncryptedPassword"
        val email = "test@test.com"
        val addUserRequest = AddUserRequest(password = nonEncodedPassword, username = username, email = email)
        val passwordEncoded = "\$2a\$10\$ztBuLyQC9g8iGcS5w46RmeiTvnq8AVmo7KEVjIiMt8/OYOBihYRcG"
        val user =  User(password = nonEncodedPassword)
        val userResponse = buildUserResponse(name = username, email = email)

        whenever(mockAddUserTransformer.transform(addUserRequest)).thenReturn(user)
        whenever(mockEncoderService.encodeIfNotEmpty(nonEncodedPassword)).thenReturn(passwordEncoded)
        whenever(mockUserDao.save(user)).thenReturn(user)
        whenever(mockUserTransformer.transform(user)).thenReturn(userResponse)
        doNothing().`when`(mockApplicationEventPublisher).publishEvent(any<AddUserEvent>())

        userSignUp.add(addUserRequest)


        verify(mockApplicationEventPublisher).publishEvent(check<AddUserEvent> {
            assertThat(it.data, `is`(userResponse))
        })
        verify(mockAddUserTransformer).transform(addUserRequest)
        verify(mockEncoderService).encodeIfNotEmpty(nonEncodedPassword)
        verify(mockUserDao).save(user)
        verify(mockUserTransformer).transform(user)
        assertThat(user.password, `is`(passwordEncoded))
    }

    @Test
    fun `add(addUserRequest) with non valid request should throw ConstraintViolation`() {
        val username = "t"
        val nonEncodedPassword = "t"
        val email = "invalid"
        val addUserRequest = AddUserRequest(password = nonEncodedPassword, username = username, email = email)

        val exception = assertThrows(ConstraintViolationException::class.java) {
            userSignUp.add(addUserRequest)
        }

        verifyNoInteractions(mockAddUserTransformer, mockEncoderService, mockUserDao, mockUserTransformer)
        val violationFieldNames = exception.constraintViolations.map { it.propertyPath.toString() }
        assertThat(violationFieldNames, hasSize(3))
        assertThat(violationFieldNames, containsInAnyOrder("username", "password", "email"))
    }
}