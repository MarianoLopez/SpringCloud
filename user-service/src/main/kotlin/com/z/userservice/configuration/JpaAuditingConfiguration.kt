package com.z.userservice.configuration

import com.z.zcoreblocking.utils.jpa.AuditorAwareImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
class JpaAuditingConfiguration {
    @Bean
    fun auditorAwareImpl() = AuditorAwareImpl()
}