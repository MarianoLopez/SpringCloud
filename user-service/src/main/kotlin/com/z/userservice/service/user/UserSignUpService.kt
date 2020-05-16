package com.z.userservice.service.user

import com.z.jwt.dto.TokenRequest
import com.z.jwt.service.JwtService
import com.z.userservice.dao.UserDao
import com.z.userservice.dto.AddUserRequest
import com.z.userservice.dto.UpdateUserRequest
import com.z.userservice.dto.event.AddUserEvent
import com.z.userservice.service.EncoderService
import com.z.userservice.transformer.AddUserRequestTransformer
import com.z.userservice.transformer.UserTransformer
import com.z.userservice.utils.evaluate
import com.z.zcoreblocking.dto.queue.UserConfirmationToken
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.validation.Validator

@Service
class UserSignUpService(
        private val userManagement: UserManagement,
        private val userDao: UserDao,
        private val addUserRequestTransformer: AddUserRequestTransformer,
        private val userTransformer: UserTransformer,
        private val encoderService: EncoderService,
        private val applicationEventPublisher: ApplicationEventPublisher,
        private val jwtService: JwtService,
        private val validator: Validator
): UserSignUp {
    private val userKey = "id"

    @Transactional
    override fun add(addUserRequest: AddUserRequest) {
        validator.evaluate(addUserRequest)
        val user = this.addUserRequestTransformer.transform(addUserRequest).apply {
            password = encoderService.encodeIfNotEmpty(password)
        }

        userTransformer.transform(userDao.save(user)).apply {
            val tokenRequest = TokenRequest(
                    subject = this.name,
                    claims = mapOf(userKey to arrayOf(this.id.toString())),
                    expiration = LocalDateTime.now().plusMinutes(15)
            )
            val confirmationToken = UserConfirmationToken(userId = this.id, username = this.name,
                                            email = this.email, token = jwtService.createToken(tokenRequest).token)
            applicationEventPublisher.publishEvent(AddUserEvent(data = confirmationToken))
        }
    }

    @Throws(IllegalArgumentException::class)
    override fun confirm(token: String) {
        val response = jwtService.decode(token)
        if (response.claims.containsKey(userKey)) {
            val userId = response.claims.getValue(userKey).first().toLong()
            userManagement.update(UpdateUserRequest(id = userId, state = true))
        } else {
            throw IllegalArgumentException("User not found")
        }
    }
}