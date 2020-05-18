package com.z.zcoreblocking.dto.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "rabbitmq")
data class RabbitMqProperties(var exchange: String = "example-exchange",
                              var exchangeType: String = "DIRECT",
                              var queue: String = "example-queue",
                              var routingKey: String = "example-routing-key")