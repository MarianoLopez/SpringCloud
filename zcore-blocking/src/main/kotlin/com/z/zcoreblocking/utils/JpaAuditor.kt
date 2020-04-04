package com.z.zcoreblocking.utils

import com.fasterxml.jackson.annotation.JsonFormat
import com.z.jwt.utils.defaultDateTimeFormat
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass //Designates a class whose mapping information is applied to the entities that inherit from it. A mapped superclass has no separate table defined for it.
@EntityListeners(AuditingEntityListener::class)
abstract class JpaAuditor {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = defaultDateTimeFormat)
    var createdDate: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = defaultDateTimeFormat)
    var lastModifiedDate: LocalDateTime = LocalDateTime.now()

    @CreatedBy
    @Column(nullable = false, updatable = false)
    var createdBy: String = ""

    @LastModifiedBy
    @Column(nullable = false)
    var modifiedBy: String = ""
}