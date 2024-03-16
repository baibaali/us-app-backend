package com.baibaali.usapp.authentication.service

import com.baibaali.usapp.authentication.model.toUserDetails
import com.baibaali.usapp.authentication.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserDetailsServiceImpl(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByEmail(username)?.toUserDetails()
            ?: throw UsernameNotFoundException("User not found")
    }
}