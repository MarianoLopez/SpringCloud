package com.z.userservice.configuration

import com.z.userservice.domain.rabbitmq.RabbitmqProperties
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableRabbit
@EnableConfigurationProperties(RabbitmqProperties::class)
class RabbitmqConfiguration(private val rabbitmqProperties: RabbitmqProperties) {
    private val logger = LoggerFactory.getLogger(RabbitmqConfiguration::class.java)

    init {
        logger.debug("Rabbit MQ properties: $rabbitmqProperties")
    }

    @Bean
    fun queue(): Queue {
        return Queue(rabbitmqProperties.queue, true)
    }

    @Bean
    fun exchange(): DirectExchange {
        return DirectExchange(rabbitmqProperties.exchange)
    }

    @Bean
    fun binding(queue: Queue, exchange: DirectExchange): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(rabbitmqProperties.routingKey)
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        return RabbitTemplate(connectionFactory).apply {
            messageConverter = producerJackson2MessageConverter()
        }
    }

    @Bean
    fun producerJackson2MessageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }
}