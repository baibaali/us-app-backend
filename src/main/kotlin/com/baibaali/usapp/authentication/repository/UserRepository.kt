package com.baibaali.usapp.authentication.repository

import com.baibaali.usapp.authentication.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

}