package com.z.userservice.dto

import com.z.userservice.utils.Regex.ALPHANUMERIC_5_10
import com.z.userservice.utils.Regex.ONLY_LETTERS_5_20
import javax.validation.constraints.Pattern


data class AddUserRequest (
    @field:Pattern(regexp = ALPHANUMERIC_5_10, message = "User password should be alphanumeric between 5 and 10 characters")
    var password:String = "",

    @field:Pattern(regexp = ONLY_LETTERS_5_20, message = "User name should be only letters between 5 and 20 characters")
    val name:String = ""
)