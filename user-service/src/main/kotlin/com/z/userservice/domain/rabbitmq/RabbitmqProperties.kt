package com.z.userservice.domain.rabbitmq

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "rabbitmq")
data class RabbitmqProperties(var exchange: String = "example-exchange",
                              var queue: String = "example-queue",
                              var routingKey: String = "example-routing-key")