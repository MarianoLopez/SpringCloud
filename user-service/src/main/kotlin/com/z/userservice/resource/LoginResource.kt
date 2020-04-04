package com.z.userservice.resource

import com.z.jwt.dto.TokenResponse
import com.z.userservice.dto.LoginRequest
import com.z.userservice.service.LoginService
import com.z.userservice.utils.ResourceConstant.LOGIN_RESOURCE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(LOGIN_RESOURCE)
class LoginResource(private val loginService: LoginService) {

    @PostMapping
    fun login(@RequestBody loginRequest: LoginRequest): TokenResponse {
        return loginService.authenticate(loginRequest)
    }
}