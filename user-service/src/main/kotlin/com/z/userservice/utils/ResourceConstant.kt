package com.z.userservice.utils

object ResourceConstant {
    private const val API_V1 = "/api/v1/"

    const val USER_RESOURCE = API_V1.plus("user")
    const val LOGIN_RESOURCE = API_V1.plus("login")
    const val SIGN_UP_RESOURCE = API_V1.plus("sign-up")
}