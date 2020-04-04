package com.z.zcoreblocking.utils

import org.springframework.data.domain.AuditorAware
import java.util.*

class AuditorAwareImpl: AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        return Optional.of(AuthenticationUtils.getCurrentAuthentication().principal.toString())
    }
}