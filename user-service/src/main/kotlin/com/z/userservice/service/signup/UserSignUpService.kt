package com.z.userservice.service.signup

import com.auth0.jwt.exceptions.JWTVerificationException
import com.z.userservice.dao.UserDao
import com.z.userservice.dto.AddUserRequest
import com.z.userservice.dto.UpdateUserRequest
import com.z.userservice.dto.event.AddUserEvent
import com.z.userservice.service.encrypt.EncoderService
import com.z.userservice.service.management.UserManagement
import com.z.userservice.transformer.AddUserRequestTransformer
import com.z.userservice.transformer.UserTransformer
import com.z.userservice.utils.evaluate
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.validation.Validator

@Service
class UserSignUpService(
        private val userManagement: UserManagement,
        private val userDao: UserDao,
        private val addUserRequestTransformer: AddUserRequestTransformer,
        private val userTransformer: UserTransformer,
        private val encoderService: EncoderService,
        private val applicationEventPublisher: ApplicationEventPublisher,
        private val confirmationTokenService: ConfirmationTokenService,
        private val validator: Validator
): UserSignUp {

    @Transactional
    override fun add(addUserRequest: AddUserRequest) {
        validator.evaluate(addUserRequest)
        val user = this.addUserRequestTransformer.transform(addUserRequest).apply {
            password = encoderService.encodeIfNotEmpty(password)
        }

        val userResponse = userTransformer.transform(userDao.save(user))

        applicationEventPublisher.publishEvent(AddUserEvent(data = userResponse))
    }

    @Throws(JWTVerificationException::class, IllegalArgumentException::class)
    override fun confirm(token: String) {
        userManagement.update(UpdateUserRequest(id = confirmationTokenService.getUserId(token), state = true))
    }
}