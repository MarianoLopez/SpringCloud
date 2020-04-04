package com.z.userservice.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.z.userservice.dto.UserResponse
import com.z.userservice.utils.ResourceConstant.USER_RESOURCE
import com.z.userservice.utils.UserTestUtils.buildUserResponse
import com.z.zcoreblocking.utils.get
import org.exparity.hamcrest.date.LocalDateTimeMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("test")
class UserResourceTest : BaseResourceIntegration() {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @WithMockUser(roles = ["USER"])
    fun `GET users with USER role should return http 200`() {
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

    @Test
    @WithMockUser(roles = ["USER"])
    fun `GET user by id with USER role should return http 200`() {
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
}