package com.z.userservice.service

import com.z.userservice.domain.rabbitmq.RabbitmqProperties
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpException
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RabbitMqService (private val rabbitTemplate: RabbitTemplate,
                       private val rabbitmqProperties: RabbitmqProperties) {
    private val logger = LoggerFactory.getLogger(RabbitMqService::class.java)

    fun publish(data: Any) {
        try {
            rabbitTemplate.convertAndSend(rabbitmqProperties.exchange, rabbitmqProperties.routingKey, data)
        } catch (amqpException: AmqpException) {
            logger.error(amqpException.localizedMessage)
        }
    }
}