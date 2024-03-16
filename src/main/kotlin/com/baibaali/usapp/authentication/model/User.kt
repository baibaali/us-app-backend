package com.baibaali.usapp.authentication.model

import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "app_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    var email: String = "",
    var password: String = "",
    var name: String = "",
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER
)

typealias SpringUser = org.springframework.security.core.userdetails.User
fun User.toUserDetails(): UserDetails = SpringUser.builder()
    .username(email)
    .password(password)
    .roles(role.name)
    .build()