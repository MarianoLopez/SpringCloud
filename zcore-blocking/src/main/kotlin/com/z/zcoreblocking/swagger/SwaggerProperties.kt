package com.z.zcoreblocking.swagger

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "swagger")
data class SwaggerProperties(var basePath:String? = null, var globalAuthentication:Boolean = false)