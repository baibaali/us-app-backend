package com.baibaali.usapp.authentication.dto

import com.baibaali.usapp.authentication.model.DefaultAccount
import com.baibaali.usapp.user.model.User

data class UserRegistrationRequest(
    val email: String,
    val password: String,
    val name: String
)

fun UserRegistrationRequest.toDefaultAccount() = DefaultAccount(
    0L,
    email = email,
    password = password,
)

fun UserRegistrationRequest.toUser() = User(
    email = email,
    name = name
)