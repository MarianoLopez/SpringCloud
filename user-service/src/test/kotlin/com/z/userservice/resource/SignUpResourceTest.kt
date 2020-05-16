package com.z.userservice.resource

import com.z.userservice.dto.AddUserRequest
import com.z.userservice.utils.ResourceConstant.SIGN_UP_RESOURCE
import com.z.zcoreblocking.utils.test.body
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class SignUpResourceTest:  BaseResourceIntegration() {

    @DisplayName("POST user")
    @Nested
    inner class PostUser {
        @Test
        fun `POST valid user without authentication should return http 201`() {
            val name = "Tester"
            val password = "tester"
            val email = "tester@tester.com"
            val addUserRequest = AddUserRequest(password, name, email)

            webTestClient
                    .perform(post(SIGN_UP_RESOURCE).body(addUserRequest))
                    .andExpect(status().isCreated)
        }
    }

}