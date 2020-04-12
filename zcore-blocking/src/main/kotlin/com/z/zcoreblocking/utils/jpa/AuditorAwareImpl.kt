package com.z.zcoreblocking.utils.jpa

import com.z.zcoreblocking.utils.auth.AuthenticationUtils
import org.springframework.data.domain.AuditorAware
import java.util.*

class AuditorAwareImpl: AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        return Optional.of(AuthenticationUtils.getCurrentAuthentication().principal.toString())
    }
}