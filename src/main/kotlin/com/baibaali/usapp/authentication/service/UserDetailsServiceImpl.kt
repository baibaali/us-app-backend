package com.baibaali.usapp.authentication.service

import com.baibaali.usapp.authentication.model.toUserDetails
import com.baibaali.usapp.authentication.repository.DefaultAccountRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserDetailsServiceImpl(
    private val defaultAccountRepository: DefaultAccountRepository,
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val defaultAccount = defaultAccountRepository.findByEmail(username) ?:
            throw UsernameNotFoundException("User not found")
        return defaultAccount.toUserDetails()
    }
}