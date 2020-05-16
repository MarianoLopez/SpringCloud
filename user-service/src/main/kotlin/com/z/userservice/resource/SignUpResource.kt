package com.z.userservice.resource

import com.z.userservice.dto.AddUserRequest
import com.z.userservice.service.user.UserSignUp
import com.z.userservice.utils.ResourceConstant.SIGN_UP_RESOURCE
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(SIGN_UP_RESOURCE)
class SignUpResource (private val userSignUpService: UserSignUp) {

    @PostMapping
    fun save(@RequestBody addUserRequest: AddUserRequest): ResponseEntity<Void> {
        this.userSignUpService.add(addUserRequest)
        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }

    @GetMapping
    fun confirm(@RequestParam confirmationToken:String): ResponseEntity<Void> {
        this.userSignUpService.confirm(confirmationToken)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}