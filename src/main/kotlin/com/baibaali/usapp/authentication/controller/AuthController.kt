package com.baibaali.usapp.authentication.controller

import com.baibaali.usapp.authentication.dto.*
import com.baibaali.usapp.authentication.service.AuthenticationService
import com.baibaali.usapp.user.model.toUserDetails
import com.baibaali.usapp.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val authService: AuthenticationService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: UserRegistrationRequest): ResponseEntity<AuthResponse> {
        val user = userService.save(request.toUser()) ?:
            return ResponseEntity.status(HttpStatus.CONFLICT).build()

        val response = authService.generateAuthResponse(user.toUserDetails())
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/login")
    fun authenticate(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        return try {
            val response = authService.authenticate(request)
            ResponseEntity.ok(response)
        } catch (e: AuthenticationException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @PostMapping("/refresh-token")
    fun refreshAccessToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<RefreshTokenResponse> {
        val accessToken = authService.refreshAccessToken(request.refreshToken) ?:
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        return ResponseEntity.ok(RefreshTokenResponse(accessToken))
    }

    @PostMapping("/logout")
    fun logout(@RequestBody request: LogoutRequest): ResponseEntity<Unit> {
        authService.revokeRefreshToken(request.refreshToken)
        return ResponseEntity.ok().build()
    }
}