package com.z.userservice.dao

import com.z.userservice.domain.UserConfirmation
import com.z.userservice.domain.UserConfirmationId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ConfirmationDao: JpaRepository<UserConfirmation, UserConfirmationId> {
    @Query("SELECT uc FROM UserConfirmation  uc WHERE uc.id.status = 'NOT_SENT' AND uc.id.userId NOT IN " +
            "(SELECT sub.id.userId FROM UserConfirmation sub WHERE sub.id.status <> 'NOT_SENT')")
    fun findAllNotSent(): List<UserConfirmation>
}