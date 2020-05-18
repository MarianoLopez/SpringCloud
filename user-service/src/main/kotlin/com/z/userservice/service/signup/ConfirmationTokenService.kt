package com.z.userservice.service.signup

import com.auth0.jwt.exceptions.JWTVerificationException
import com.z.jwt.dto.TokenRequest
import com.z.jwt.dto.TokenResponse
import com.z.jwt.service.JwtService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ConfirmationTokenService (private val jwtService: JwtService ) {
    private val tokenDurationInMinutes = 30L

    fun createConfirmationToken(id:Long): TokenResponse {
        val tokenRequest = TokenRequest(
                subject = id.toString(),
                expiration = LocalDateTime.now().plusMinutes(tokenDurationInMinutes)
        )
        return jwtService.createToken(tokenRequest)
    }

    @Throws(JWTVerificationException::class)
    fun getUserId(token:String): Long {
        return jwtService.decode(token).subject.toLong()
    }
}