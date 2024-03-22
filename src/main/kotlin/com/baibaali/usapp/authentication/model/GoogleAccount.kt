package com.baibaali.usapp.authentication.model

import jakarta.persistence.*
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "google_user")
data class GoogleAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(name = "email", unique = true, nullable = false)
    var email: String,
)

fun GoogleAccount.toUserDetails(): UserDetails {
    return User(
        email,
        "",
        listOf(SimpleGrantedAuthority("USER"))
    )
}

fun GoogleAccount.toUser(): AppUser {
    return AppUser(
        id = id,
        email = email,
    )
}