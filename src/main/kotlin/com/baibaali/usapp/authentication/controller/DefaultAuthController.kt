package com.baibaali.usapp.authentication.controller

import com.baibaali.usapp.authentication.dto.*
import com.baibaali.usapp.authentication.excpetion.AccountAlreadyExistsException
import com.baibaali.usapp.authentication.service.AuthenticationService
import com.baibaali.usapp.authentication.service.DefaultAccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class DefaultAuthController(
    private val defaultAccountService: DefaultAccountService,
    private val authService: AuthenticationService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: UserRegistrationRequest): ResponseEntity<Unit> {
        return try {
            defaultAccountService.save(request) ?:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (e: AccountAlreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
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