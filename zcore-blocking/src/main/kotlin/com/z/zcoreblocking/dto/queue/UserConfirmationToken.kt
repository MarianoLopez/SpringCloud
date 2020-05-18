package com.z.zcoreblocking.dto.queue

data class UserConfirmationToken (val userId:Long, val username:String, val email: String, val token: String)