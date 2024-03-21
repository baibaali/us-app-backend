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
    private val userService: UserService,
    private val authManager: AuthenticationManager,
) {

    fun authenticateWithGoogle(request: GoogleAuthRequest): AuthResponse? {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(Collections.singletonList("354092612604-0lj95favg9j4hn8mco3nia43i4vvb0a0.apps.googleusercontent.com"))
            .setIssuer("https://accounts.google.com")
            .build()

        val idToken = verifier.verify(request.idToken) ?: return null

        val googleUser = idToken.payload.toGoogleUser()

        println(googleUser.toString())

        return try {
            val user = userDetailsService.loadUserByUsername(googleUser.email)
            generateAuthResponse(user)
        } catch (e: UsernameNotFoundException) {
            userService.save(googleUser.toUser())
            val newUser = userDetailsService.loadUserByUsername(googleUser.email)
            generateAuthResponse(newUser)
        }
    }

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

fun GoogleIdToken.Payload.toGoogleUser(): GoogleUser {
    return GoogleUser(
        subject,
        email,
        emailVerified,
        get("name") as String,
        get("picture") as String,
        get("family_name") as String,
        get("given_name") as String
    )
}
