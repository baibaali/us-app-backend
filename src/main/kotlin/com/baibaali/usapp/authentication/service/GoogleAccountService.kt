package com.baibaali.usapp.authentication.service

import com.baibaali.usapp.authentication.model.GoogleAccount
import com.baibaali.usapp.authentication.model.toUser
import com.baibaali.usapp.authentication.repository.DefaultAccountRepository
import com.baibaali.usapp.authentication.repository.GoogleAccountRepository
import com.baibaali.usapp.user.model.User
import com.baibaali.usapp.user.service.UserService
import org.springframework.stereotype.Service

@Service
class GoogleAccountService(
    private val googleAccountRepository: GoogleAccountRepository,
    private val defaultAccountRepository: DefaultAccountRepository,
    private val userService: UserService
) {

    fun save(account: GoogleAccount): GoogleAccount? {
        if (
            googleAccountRepository.existsById(account.id) ||
            defaultAccountRepository.existsByEmail(account.email)
        ){
            return null
        }

        return googleAccountRepository.save(account)
    }

    fun findByEmail(email: String): GoogleAccount? {
        return googleAccountRepository.findByEmail(email)
    }

    fun existsByEmail(email: String): Boolean {
        return googleAccountRepository.existsByEmail(email)
    }

}