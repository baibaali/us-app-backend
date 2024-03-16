package com.baibaali.usapp.authentication.service

import com.baibaali.usapp.authentication.model.User
import com.baibaali.usapp.authentication.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    fun save(user: User): User? {
        val found = findByEmail(user.email)

        return if (found == null) {
            userRepository.save(user)
            user
        } else
            null
    }

    fun findAll(): List<User> = userRepository.findAll()

    fun findById(id: Long): User? = userRepository.findById(id).orElse(null)

    fun findByEmail(email: String): User? = userRepository.findByEmail(email)

}