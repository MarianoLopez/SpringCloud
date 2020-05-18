package com.z.userservice.configuration

import org.mockito.Mockito.mock
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary


@TestConfiguration
class MockRabbitMqConfiguration {
    @Bean
    @Primary
    fun mockConnectionFactory(): CachingConnectionFactory {
        return mock(CachingConnectionFactory::class.java)
    }

    @Bean
    @Primary
    fun mockRabbitTemplate(): RabbitTemplate {
        return mock(RabbitTemplate::class.java)
    }
}