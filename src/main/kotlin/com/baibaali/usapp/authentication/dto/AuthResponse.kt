package com.baibaali.usapp.authentication.dto

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)
