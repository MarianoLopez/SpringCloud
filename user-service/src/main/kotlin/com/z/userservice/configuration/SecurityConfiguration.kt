package com.z.userservice.configuration

import com.z.userservice.utils.ResourceConstant.USER_RESOURCE
import com.z.zcoreblocking.domain.property.RequestAuthProperties
import com.z.zcoreblocking.security.filter.AuthTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
class SecurityConfiguration(private val authTokenFilter: AuthTokenFilter,
                            private val requestAuthProperties: RequestAuthProperties):
    WebSecurityConfigurerAdapter(), WebMvcConfigurer {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .headers().frameOptions().sameOrigin().and()
            .formLogin().disable()
            .logout().disable()
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests().antMatchers(*requestAuthProperties.doNotEval).permitAll().and()
            .authorizeRequests().antMatchers(HttpMethod.POST, USER_RESOURCE).permitAll().and()
            .authorizeRequests().antMatchers(USER_RESOURCE.plus("/**")).hasAnyRole("ADMIN").and()
            .authorizeRequests().anyRequest().authenticated().and()
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD","OPTIONS")
                .maxAge(3600)
    }
}