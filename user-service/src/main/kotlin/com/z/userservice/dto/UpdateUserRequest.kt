package com.z.userservice.dto

import com.z.jwt.domain.UserRoles
import com.z.userservice.utils.Regex.ALPHANUMERIC_5_10
import javax.validation.constraints.Pattern


data class UpdateUserRequest (
    val id:Long,

    @field:Pattern(regexp = ALPHANUMERIC_5_10, message = "User password should be alphanumeric between 5 and 10 characters")
    var password:String? = null, //null to avoid the Pattern validation

    val state:Boolean = true,

    val roles:Set<UserRoles> = emptySet()
)