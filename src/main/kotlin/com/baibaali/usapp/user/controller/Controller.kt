package com.baibaali.usapp.user.controller

import com.baibaali.usapp.authentication.service.JwtService
import com.baibaali.usapp.user.dto.UserResponse
import com.baibaali.usapp.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/users")
class Controller(
    private val userService: UserService,
    private val jwtService: JwtService,
) {

    @GetMapping("/me")
    fun getMe(@RequestHeader("Authorization") authHeader: String): ResponseEntity<UserResponse> {
        if (authHeader.startsWith("Bearer ")) {
            val token = authHeader.substringAfter("Bearer ")
            val email = jwtService.extractEmail(token)

            email?.let { e ->
                val user = userService.findByEmail(e) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                val ur = UserResponse(user.id, user.email, user.name, user.role.name)
                return ResponseEntity(ur, HttpStatus.OK)
            }
        }
        throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")
    }
}