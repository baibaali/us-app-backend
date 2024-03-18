package com.baibaali.usapp.authentication.dto

import com.baibaali.usapp.user.model.User

data class UserRegistrationRequest(
    val email: String,
    val password: String,
    val name: String
)

fun UserRegistrationRequest.toUser() = User(
    email = email,
    password = password,
    name = name
)