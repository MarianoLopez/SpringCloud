package com.z.zcoreblocking

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.z.jwt.utils.defaultDateTimeFormat
import com.z.zcoreblocking.dto.property.RabbitMqProperties
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat

@Configuration
@EnableConfigurationProperties(RabbitMqProperties::class)
class RabbitMqBootstrapper {

    @ConditionalOnClass(RabbitTemplate::class)
    class RabbitmqConfiguration(private val rabbitMqProperties: RabbitMqProperties) {
        private val logger = LoggerFactory.getLogger(RabbitMqBootstrapper::class.java)

        init {
            logger.debug("Rabbit MQ properties: $rabbitMqProperties")
        }

        @Bean
        @ConditionalOnMissingBean(Queue::class)
        fun queue(): Queue {
            return Queue(rabbitMqProperties.queue, true).apply {
                logger.debug("Bootstrapping: $this")
            }
        }

        @Bean
        @ConditionalOnMissingBean(Exchange::class)
        fun exchange(): Exchange {
            val defaultExchange = DirectExchange(rabbitMqProperties.exchange)
            return when(rabbitMqProperties.exchangeType.trim().toUpperCase()) {
                "DIRECT" -> defaultExchange
                "TOPIC"  -> TopicExchange(rabbitMqProperties.exchange)
                "FANOUT" -> FanoutExchange(rabbitMqProperties.exchange)
                else -> defaultExchange
            }.apply {
                logger.debug("Bootstrapping: $this")
            }
        }

        @Bean
        @ConditionalOnMissingBean(Binding::class)
        @ConditionalOnBean(Queue::class, Exchange::class)
        fun binding(queue: Queue, exchange: Exchange): Binding {
            return BindingBuilder.bind(queue).to(exchange).with(rabbitMqProperties.routingKey).noargs().apply {
                logger.debug("Bootstrapping: $this")
            }
        }

        @Bean
        @ConditionalOnMissingBean(Jackson2JsonMessageConverter::class)
        fun producerJackson2MessageConverter(): Jackson2JsonMessageConverter {
            val mapper = jacksonObjectMapper().apply {
                registerModule(JavaTimeModule())
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                dateFormat = SimpleDateFormat(defaultDateTimeFormat)
            }
            return Jackson2JsonMessageConverter(mapper).apply {
                logger.debug("Bootstrapping Jackson2JsonMessageConverter")
            }
        }

        @Bean
        @ConditionalOnBean(Jackson2JsonMessageConverter::class)
        fun rabbitTemplate(connectionFactory: ConnectionFactory,
                           jackson2JsonMessageConverter: Jackson2JsonMessageConverter): RabbitTemplate {
            return RabbitTemplate(connectionFactory).apply {
                messageConverter = jackson2JsonMessageConverter
            }.apply {
                logger.debug("Bootstrapping RabbitTemplate with Jackson2JsonMessageConverter")
            }
        }
    }
}