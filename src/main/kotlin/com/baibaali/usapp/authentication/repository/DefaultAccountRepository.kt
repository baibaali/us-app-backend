package com.baibaali.usapp.authentication.repository

import com.baibaali.usapp.authentication.model.DefaultAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DefaultAccountRepository: JpaRepository<DefaultAccount, Long> {
    fun findByEmail(email: String): DefaultAccount?
    fun existsByEmail(email: String): Boolean
}