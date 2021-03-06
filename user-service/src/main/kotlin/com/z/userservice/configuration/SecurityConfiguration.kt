package com.z.userservice.configuration

import com.z.userservice.utils.ResourceConstant.LOGIN_RESOURCE
import com.z.userservice.utils.ResourceConstant.SIGN_UP_RESOURCE
import com.z.userservice.utils.ResourceConstant.USER_RESOURCE
import com.z.zcoreblocking.dto.property.RequestAuthProperties
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

@Configuration
@EnableWebSecurity
class SecurityConfiguration(private val authTokenFilter: AuthTokenFilter,
                            private val requestAuthProperties: RequestAuthProperties) : WebSecurityConfigurerAdapter() {

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
                .authorizeRequests().antMatchers(SIGN_UP_RESOURCE).permitAll().and()
                .authorizeRequests().antMatchers(HttpMethod.POST, LOGIN_RESOURCE).permitAll().and()
                .authorizeRequests().antMatchers(USER_RESOURCE.plus("/**")).hasAnyRole("ADMIN").and()
                .authorizeRequests().anyRequest().authenticated().and()
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}