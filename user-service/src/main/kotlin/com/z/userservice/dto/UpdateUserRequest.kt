package com.z.userservice.dto

import com.z.jwt.domain.UserRoles
import com.z.userservice.utils.Regex.ALPHANUMERIC_5_20
import javax.validation.constraints.Pattern


data class UpdateUserRequest (
    val id:Long,

    @field:Pattern(regexp = ALPHANUMERIC_5_20,
        message = "User password should be alphanumeric between 5 and 20 characters (with no special characters)")
    var password:String? = null, //null to avoid the Pattern validation

    val state:Boolean = true,

    val roles:Set<UserRoles> = emptySet()
)