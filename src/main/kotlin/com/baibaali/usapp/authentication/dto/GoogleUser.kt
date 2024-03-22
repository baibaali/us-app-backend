package com.baibaali.usapp.authentication.dto

import com.baibaali.usapp.authentication.model.GoogleAccount
import com.baibaali.usapp.user.model.User
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken

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
        name = givenName,
    )
}

fun GoogleUser.toGoogleAccount(): GoogleAccount {
    return GoogleAccount(
        id = 0L,
        email = email,
    )
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
