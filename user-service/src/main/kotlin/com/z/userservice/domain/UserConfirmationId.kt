package com.z.userservice.domain

import java.io.Serializable
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
data class UserConfirmationId(
        var userId: Long = 0L,

        @Enumerated(EnumType.STRING)
        var status: ConfirmationStatus = ConfirmationStatus.NOT_SENT
): Serializable