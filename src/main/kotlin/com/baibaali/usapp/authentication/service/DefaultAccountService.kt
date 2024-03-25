package com.baibaali.usapp.authentication.service

import com.baibaali.usapp.authentication.dto.UserRegistrationRequest
import com.baibaali.usapp.authentication.dto.toDefaultAccount
import com.baibaali.usapp.authentication.dto.toUser
import com.baibaali.usapp.authentication.excpetion.AccountAlreadyExistsException
import com.baibaali.usapp.authentication.model.DefaultAccount
import com.baibaali.usapp.authentication.model.toUser
import com.baibaali.usapp.authentication.repository.DefaultAccountRepository
import com.baibaali.usapp.authentication.repository.GoogleAccountRepository
import com.baibaali.usapp.user.model.User
import com.baibaali.usapp.user.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class DefaultAccountService(
    private val googleAccountRepository: GoogleAccountRepository,
    private val defaultAccountRepository: DefaultAccountRepository,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) {
    fun save(request: UserRegistrationRequest): User? {
        val account = request.toDefaultAccount()
        if (
            googleAccountRepository.existsById(account.id) ||
            defaultAccountRepository.existsByEmail(account.email)
        ){
            throw AccountAlreadyExistsException()
        }

        val withEncodedPassword = account.copy(password = passwordEncoder.encode(account.password))
        defaultAccountRepository.save(withEncodedPassword)

        return userService.save(request.toUser())
    }

    fun findByEmail(email: String): DefaultAccount? {
        return defaultAccountRepository.findByEmail(email)
    }
}