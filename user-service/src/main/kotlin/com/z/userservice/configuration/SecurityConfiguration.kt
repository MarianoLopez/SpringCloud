package com.z.userservice.configuration

import com.z.userservice.utils.ResourceConstant.API_V1
import com.z.zcoreblocking.domain.property.RequestAuthProperties
import com.z.zcoreblocking.security.filter.AuthTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
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
            .formLogin().disable()
            .logout().disable()
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests().antMatchers(*requestAuthProperties.doNotEval).permitAll().and()
            .authorizeRequests().antMatchers(API_V1.plus("**")).hasAnyRole("USER").and()
            .authorizeRequests().anyRequest().authenticated().and()
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}