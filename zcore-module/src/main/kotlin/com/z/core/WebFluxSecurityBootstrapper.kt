package com.z.core

import com.z.core.security.configuration.SecurityRepository
import com.z.jwt.property.JwtProperties
import com.z.jwt.service.JwtService
import com.z.jwt.service.JwtSpringService
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.Authentication

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class WebFluxSecurityBootstrapper {
    private val logger: Logger = LoggerFactory.getLogger(WebFluxSecurityBootstrapper::class.java)

    @Bean
    @ConditionalOnClass(Authentication::class)
    @ConditionalOnMissingBean(value = [JwtSpringService::class])
    fun jwtSpringService(jwtProperties: JwtProperties): JwtSpringService {
        logger.debug("Bootstrapping JwtSpringService Bean with $jwtProperties")
        return JwtSpringService(JwtService(jwtProperties))
    }

    @Bean
    @ConditionalOnClass(Publisher::class)
    @ConditionalOnMissingBean(SecurityRepository::class)
    @ConditionalOnBean(value = [JwtSpringService::class])
    fun securityRepository(jwtSpringService: JwtSpringService): SecurityRepository {
        logger.debug("Bootstrapping SecurityRepository Bean")
        return SecurityRepository(jwtSpringService)
    }
}