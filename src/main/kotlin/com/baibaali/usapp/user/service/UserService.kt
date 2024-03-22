package com.baibaali.usapp.user.service

import com.baibaali.usapp.user.model.User
import com.baibaali.usapp.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    fun save(user: User): User? {
        if (existsByEmail(user.email)) return null
        return userRepository.save(user)
    }

    fun findAll(): List<User> = userRepository.findAll()

    fun findById(id: Long): User? = userRepository.findById(id).orElse(null)

    fun findByEmail(email: String): User? = userRepository.findByEmail(email)

    fun existsByEmail(email: String): Boolean = userRepository.existsByEmail(email)
}