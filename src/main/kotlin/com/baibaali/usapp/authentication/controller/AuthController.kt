package com.baibaali.usapp.authentication.controller

import com.baibaali.usapp.authentication.dto.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AuthController {

    @PostMapping("/auth/register")
    fun register(@RequestBody request: UserRegistrationRequest): ResponseEntity<AuthResponse> {
        throw NotImplementedError()
    }

    @PostMapping("/auth/login")
    fun authenticate(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        throw NotImplementedError()
    }

    @PostMapping("/auth/refresh-token")
    fun refreshAccessToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<RefreshTokenResponse> {
        throw NotImplementedError()
    }

}