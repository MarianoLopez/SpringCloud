package com.z.userservice.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.z.userservice.dto.AddUserRequest
import com.z.userservice.dto.UpdateUserRequest
import com.z.userservice.dto.UserResponse
import com.z.userservice.utils.ResourceConstant.USER_RESOURCE
import com.z.userservice.utils.UserTestUtils.buildUserResponse
import com.z.zcoreblocking.utils.test.body
import com.z.zcoreblocking.utils.test.get
import org.exparity.hamcrest.date.LocalDateTimeMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserResourceTest : BaseResourceIntegration() {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @DisplayName("GET users")
    @Nested
    inner class GetUsers {

        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun `GET users with ADMIN role should return http 200`() {
            webTestClient
                    .perform(get(USER_RESOURCE))
                    .andExpect(status().isOk)
        }

        @Test
        fun `GET users without role should return http 403`() {
            webTestClient
                    .perform(get(USER_RESOURCE))
                    .andExpect(status().isForbidden)
        }

        @Test
        @WithMockUser(roles = ["Anonymous"])
        fun `GET users with ANONYMOUS role should return http 403`() {
            webTestClient
                    .perform(get(USER_RESOURCE))
                    .andExpect(status().isForbidden)
        }
    }

    @DisplayName("GET user by ID")
    @Nested
    inner class GetUserById {
        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun `GET user by id with ADMIN role should return http 200`() {
            val expectedResponse = buildUserResponse()

            val response:UserResponse = webTestClient
                    .perform(get("$USER_RESOURCE/${expectedResponse.id}"))
                    .andExpect(status().isOk)
                    .get(objectMapper)

            assertThat(response.id, `is`(expectedResponse.id))
            assertThat(response.name, `is`(expectedResponse.name))
            assertThat(response.state, `is`(expectedResponse.state))
            assertThat(response.roles, `is`(expectedResponse.roles))
            assertThat(response.createdBy, `is`(expectedResponse.createdBy))
            assertThat(response.modifiedBy, `is`(expectedResponse.modifiedBy))
            assertThat(response.createdDate, LocalDateTimeMatchers.isToday())
            assertThat(response.lastModifiedDate, LocalDateTimeMatchers.isToday())
        }

        @Test
        @WithMockUser(roles = ["USER"])
        fun `GET user by id with USER role should return http 403`() {
            val expectedResponse = buildUserResponse()

            webTestClient
                    .perform(get("$USER_RESOURCE/${expectedResponse.id}"))
                    .andExpect(status().isForbidden)
        }
    }



    @DisplayName("PUT user")
    @Nested
    inner class PutUser {
        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun `PUT with ADMIN role should return http 200`() {
            val id = 1L
            val state = false
            val updateUserRequest = UpdateUserRequest(id = id, state = state)

            val response:UserResponse = webTestClient
                    .perform(put(USER_RESOURCE).body(updateUserRequest))
                    .andExpect(status().isOk)
                    .get(objectMapper)

            assertThat(response, notNullValue())
            assertThat(response.id, `is`(id))
            assertThat(response.state, `is`(false))
        }

        @Test
        @WithMockUser(roles = ["USER"])
        fun `PUT with USER role should return http 403`() {
            val id = 1L
            val state = false
            val updateUserRequest = UpdateUserRequest(id = id, state = state)

            webTestClient
                    .perform(put(USER_RESOURCE).body(updateUserRequest))
                    .andExpect(status().isForbidden)
        }
    }

    @DisplayName("DELETE user")
    @Nested
    inner class DeleteUser {
        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun `DELETE with ADMIN role should return http 200`() {
            val id = 1L

            val response:UserResponse = webTestClient
                    .perform(delete("$USER_RESOURCE/$id"))
                    .andExpect(status().isOk)
                    .get(objectMapper)

            assertThat(response, notNullValue())
            assertThat(response.id, `is`(id))
            assertThat(response.state, `is`(false))
        }

        @Test
        @WithMockUser(roles = ["USER"])
        fun `DELETE with USER role should return http 403`() {
            val id = 1L

            webTestClient
                    .perform(delete("$USER_RESOURCE/$id"))
                    .andExpect(status().isForbidden)
        }
    }
}