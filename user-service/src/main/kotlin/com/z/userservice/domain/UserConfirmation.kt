package com.z.userservice.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.z.jwt.utils.defaultDateTimeFormat
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity


@Entity
@DynamicUpdate
data class UserConfirmation(
        @EmbeddedId
        var id: UserConfirmationId = UserConfirmationId(),

        val username:String = "",

        val email: String = "",

        @Column(nullable = false, updatable = true)
        @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = defaultDateTimeFormat)
        var date: LocalDateTime = LocalDateTime.now()
)