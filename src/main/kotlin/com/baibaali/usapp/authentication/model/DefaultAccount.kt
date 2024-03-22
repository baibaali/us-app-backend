package com.baibaali.usapp.authentication.model

import jakarta.persistence.*
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "default_user")
data class DefaultAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,
)

fun DefaultAccount.toUserDetails(): UserDetails {
    return User(
        email,
        password,
        listOf(SimpleGrantedAuthority("USER"))
    )
}

typealias AppUser = com.baibaali.usapp.user.model.User
fun DefaultAccount.toUser(): AppUser {
    return AppUser(
        id = id,
        email = email,
    )
}