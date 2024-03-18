package com.baibaali.usapp.user.service

import com.baibaali.usapp.user.model.User
import com.baibaali.usapp.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun save(user: User): User? {
        val found = findByEmail(user.email)

        println("Found user is: $found")

        return if (found == null) {
            val withEncodedPassword = user.copy(password = passwordEncoder.encode(user.password))
            userRepository.save(withEncodedPassword)
            withEncodedPassword
        } else
            null
    }

    fun findAll(): List<User> = userRepository.findAll()

    fun findById(id: Long): User? = userRepository.findById(id).orElse(null)

    fun findByEmail(email: String): User? = userRepository.findByEmail(email)

}