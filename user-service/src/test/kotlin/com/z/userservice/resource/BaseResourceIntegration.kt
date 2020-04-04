package com.z.userservice.resource

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseResourceIntegration {
    @Autowired
    lateinit var context: WebApplicationContext

    lateinit var webTestClient: MockMvc
    
    @BeforeAll
    internal fun setup() {
        webTestClient = MockMvcBuilders.webAppContextSetup(context).apply {
            alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
        }.build()
    }
}