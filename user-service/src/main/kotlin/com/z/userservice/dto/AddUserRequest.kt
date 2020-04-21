package com.z.userservice.dto

import com.z.userservice.utils.Regex.ALPHANUMERIC_5_20
import com.z.userservice.utils.Regex.ONLY_LETTERS_5_20
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern


data class AddUserRequest (
    @field:Pattern(regexp = ALPHANUMERIC_5_20, message = "Should be alphanumeric between 5 and 10 characters")
    @field:NotBlank(message = "Should be not blank")
    val password:String = "",

    @field:Pattern(regexp = ONLY_LETTERS_5_20, message = "Should be only letters between 5 and 20 characters")
    @field:NotBlank(message = "Should be not blank")
    val username:String = "",

    @field:Email(message = "Should be a well-formed email address")
    val email:String = ""
)