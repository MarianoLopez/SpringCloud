package com.z.zcoreblocking

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.z.jwt.property.JwtProperties
import com.z.jwt.service.JwtService
import com.z.jwt.service.JwtSpringService
import com.z.jwt.utils.defaultDateTimeFormat
import com.z.zcoreblocking.dto.property.RequestAuthProperties
import com.z.zcoreblocking.security.filter.AuthTokenFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.text.SimpleDateFormat
import java.util.*

@Configuration
@ConditionalOnClass(WebMvcConfigurer::class, WebSecurityConfigurerAdapter::class)
@EnableConfigurationProperties(JwtProperties::class, RequestAuthProperties::class)
class WebMvcSecurityBootstrapper {
    private val logger: Logger = LoggerFactory.getLogger(WebMvcSecurityBootstrapper::class.java)

    @Bean
    @ConditionalOnMissingBean(value = [JwtService::class])
    fun jwtService(jwtProperties: JwtProperties): JwtService {
        logger.debug("Bootstrapping JwtSpringService Bean with $jwtProperties")
        return JwtService(jwtProperties)
    }

    @Bean
    @ConditionalOnMissingBean(value = [JwtSpringService::class])
    @ConditionalOnBean(JwtService::class)
    fun jwtSpringService(jwtService: JwtService): JwtSpringService {
        return JwtSpringService(jwtService)
    }

    @Bean
    @ConditionalOnMissingBean(AuthTokenFilter::class)
    @ConditionalOnBean(JwtSpringService::class)
    fun tokenFilter(jwtSpringService: JwtSpringService): AuthTokenFilter {
        logger.debug("Bootstrapping AuthTokenFilter Bean")
        val mapper = ObjectMapper().apply {
            registerModule(JavaTimeModule())
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            enable(SerializationFeature.INDENT_OUTPUT)
            dateFormat = SimpleDateFormat(defaultDateTimeFormat)
            setTimeZone(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"))
        }
        return AuthTokenFilter(jwtSpringService, mapper)

    }

    @ConditionalOnMissingBean(WebSecurityConfigurerAdapter::class)
    @Configuration
    @EnableWebSecurity
    class WebSecurityBootstrapper(private val authTokenFilter: AuthTokenFilter,
                                  private val requestAuthProperties: RequestAuthProperties): WebSecurityConfigurerAdapter(), WebMvcConfigurer {
        private val logger: Logger = LoggerFactory.getLogger(WebMvcSecurityBootstrapper::class.java)

        @Throws(Exception::class)
        override fun configure(http: HttpSecurity) {
            logger.debug("Bootstrapping HttpSecurity with: $requestAuthProperties")
            http
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers(*requestAuthProperties.doNotEval).permitAll().and()
                .authorizeRequests().anyRequest().authenticated().and()
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
        }

        override fun addCorsMappings(registry: CorsRegistry) {
            logger.debug("Bootstrapping CORS")
            registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD","OPTIONS")
                .maxAge(3600)
        }
    }
}