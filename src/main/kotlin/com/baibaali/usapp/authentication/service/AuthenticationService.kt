package com.baibaali.usapp.authentication.service

import com.baibaali.usapp.authentication.dto.*
import com.baibaali.usapp.user.service.UserService
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class AuthenticationService(
    private val jwtService: JwtService,
    private val jwtProperties: JwtProperties,
    private val userDetailsService: UserDetailsServiceImpl,
    private val authManager: AuthenticationManager,
) {
    fun authenticate(request: AuthRequest): AuthResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )

        try {
            val user = userDetailsService.loadUserByUsername(request.email)
            return jwtService.generateAuthResponse(user)
        } catch (e: UsernameNotFoundException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }
    }

    fun refreshAccessToken(token: String): String? {
        val email = jwtService.extractEmail(token)
        return email?.let { e ->
            val user = userDetailsService.loadUserByUsername(e)
            if (jwtService.isTokenValid(token, user)) {
                jwtService.generate(
                    user,
                    Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration),
                    mapOf("type" to "access") as Map<String, Any>
                )
            } else {
                null
            }
        }
    }

    fun revokeRefreshToken(refreshToken: String) {

    }
}
