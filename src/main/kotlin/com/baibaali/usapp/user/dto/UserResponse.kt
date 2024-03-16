package com.baibaali.usapp.user.dto

data class UserResponse(
    val id: Long,
    val email: String,
    val name: String,
    val role: String
)
