package com.baibaali.usapp.authentication.service

import com.baibaali.usapp.authentication.dto.AuthRequest
import com.baibaali.usapp.authentication.dto.AuthResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
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

        val user = userDetailsService.loadUserByUsername(request.email)

        return generateAuthResponse(user)
    }

    fun generateAuthResponse(user: UserDetails): AuthResponse {
        val accessToken = jwtService.generate(
            user,
            Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration),
            mapOf("type" to "access") as Map<String, Any>

        )

        val refreshToken = jwtService.generate(
            user,
            Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration),
            mapOf("type" to "refresh") as Map<String, Any>
        )

        return AuthResponse(accessToken, refreshToken)
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
