package com.baibaali.usapp.authentication.service

import com.baibaali.usapp.authentication.dto.AuthResponse
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService(
    private val jwtProperties: JwtProperties
) {

    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    fun generateAuthResponse(user: UserDetails): AuthResponse {
        val accessToken = generateAccessToken(user)
        val refreshToken = generateRefreshToken(user)
        return AuthResponse(accessToken, refreshToken)
    }

    fun generateAccessToken(userDetails: UserDetails): String {
        val expiration = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
        return generate(userDetails, expiration, mapOf("type" to "access"))
    }

    fun generateRefreshToken(userDetails: UserDetails): String {
        val expiration = Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)
        return generate(userDetails, expiration, mapOf("type" to "refresh"))
    }

    fun generate(userDetails: UserDetails, expiration: Date, claims: Map<String, Any> = emptyMap()): String {
        return Jwts.builder()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expiration)
            .add(claims)
            .and()
            .signWith(secretKey)
            .compact()
    }

    fun isTokenValid(jwt: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(jwt)

        return !isTokenExpired(jwt) && email == userDetails.username
    }

    fun isTokenExpired(jwt: String): Boolean {
        return extractExpiration(jwt).before(Date())
    }

    fun extractEmail(jwt: String): String? {
        return extractClaim(jwt, Claims::getSubject)
    }

    fun extractExpiration(jwt: String): Date {
        return extractClaim(jwt, Claims::getExpiration)
    }

    fun extractType(jwt: String): String? {
        return extractClaim(jwt) { claims -> claims["type"] as String? }
    }

    private fun <T> extractClaim(jwt: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(jwt)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(jwt: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(jwt)
            .payload
    }

}
