package com.baibaali.usapp.authentication.repository

import com.baibaali.usapp.authentication.model.GoogleAccount
import org.springframework.data.jpa.repository.JpaRepository

interface GoogleAccountRepository: JpaRepository<GoogleAccount, Long> {
    fun findByEmail(email: String): GoogleAccount?
    fun existsByEmail(email: String): Boolean
}