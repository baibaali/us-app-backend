package com.baibaali.usapp.authentication.dto

data class UserRegistrationRequest(
    val email: String,
    val password: String,
    val name: String
)