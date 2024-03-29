package com.baibaali.usapp.authentication.config

import com.baibaali.usapp.authentication.repository.DefaultAccountRepository
import com.baibaali.usapp.authentication.repository.GoogleAccountRepository
import com.baibaali.usapp.authentication.service.JwtProperties
import com.baibaali.usapp.user.repository.UserRepository
import com.baibaali.usapp.authentication.service.UserDetailsServiceImpl
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class Config {

    @Bean
    fun userDetailsService(
        defaultAccountRepository: DefaultAccountRepository,
    ): UserDetailsServiceImpl {
        return UserDetailsServiceImpl(defaultAccountRepository)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(
        defaultAccountRepository: DefaultAccountRepository,
    ): AuthenticationProvider {
        return DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService(defaultAccountRepository))
            setPasswordEncoder(passwordEncoder())
        }
    }

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager {
        return authConfig.authenticationManager
    }

}