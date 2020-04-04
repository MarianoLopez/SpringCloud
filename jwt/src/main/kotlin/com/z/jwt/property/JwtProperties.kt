package com.z.jwt.property

import org.springframework.boot.context.properties.ConfigurationProperties

private const val DEFAULT_TOKEN_PREFIX = "Bearer "
private const val DEFAULT_TOKEN_ISSUER = "change me"
private const val DEFAULT_TOKEN_EXPIRATION_IN_SECONDS = 604800L
private const val DEFAULT_TOKEN_AUDIENCE = ""
private const val DEFAULT_TOKEN_SECRET = "change me"

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(var issuer: String = DEFAULT_TOKEN_ISSUER,
                         var expirationInSeconds: Long = DEFAULT_TOKEN_EXPIRATION_IN_SECONDS,
                         var audience: String = DEFAULT_TOKEN_AUDIENCE,
                         var secretKey: String = DEFAULT_TOKEN_SECRET,
                         var prefix: String = DEFAULT_TOKEN_PREFIX)