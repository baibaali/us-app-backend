package com.baibaali.usapp.authentication.dto

import com.baibaali.usapp.user.model.User

data class GoogleUser(
    val userId: String,
    val email: String,
    val emailVerified: Boolean,
    val name: String,
    val pictureUrl: String,
    val familyName: String,
    val givenName: String
) {
    override fun toString(): String {
        val builder = StringBuilder()
        builder
            .append("{\n")
            .append("  \"userId\": \"$userId\",\n")
            .append("  \"email\": \"$email\",\n")
            .append("  \"emailVerified\": \"$emailVerified\",\n")
            .append("  \"name\": \"$name\",\n")
            .append("  \"pictureUrl\": \"$pictureUrl\",\n")
            .append("  \"familyName\": \"$familyName\",\n")
            .append("  \"givenName\": \"$givenName\"\n")
            .append("}")
        return builder.toString()
    }
}

fun GoogleUser.toUser(): User {
    return User(
        email = email,
        password = "google",
        name = name,
    )
}