package com.baibaali.usapp.authentication.service

import com.baibaali.usapp.authentication.dto.*
import com.baibaali.usapp.authentication.excpetion.AccountAlreadyExistsException
import com.baibaali.usapp.authentication.model.toUserDetails
import com.baibaali.usapp.user.model.User
import com.baibaali.usapp.user.model.toUserDetails
import com.baibaali.usapp.user.service.UserService
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class OAuth2Service(
    private val jwtService: JwtService,
    private val defaultAccountService: DefaultAccountService,
    private val googleAccountService: GoogleAccountService,
    private val userService: UserService
) {

    fun authenticateWithGoogle(request: GoogleAuthRequest): AuthResponse? {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(Collections.singletonList("354092612604-0lj95favg9j4hn8mco3nia43i4vvb0a0.apps.googleusercontent.com"))
            .setIssuer("https://accounts.google.com")
            .build()

        val idToken = verifier.verify(request.idToken) ?: return null

        val googleUser = idToken.payload.toGoogleUser()

        println(googleUser.toString())

        val googleAccount = googleAccountService.findByEmail(googleUser.email)

        println("Google Account: ${googleAccount.toString()}")

        val googleAccountExists = googleAccount != null

        if(googleAccountExists) {
            return jwtService.generateAuthResponse(googleAccount!!.toUserDetails())
        } else {
            val defaultAccount = defaultAccountService.findByEmail(googleUser.email)
            val defaultAccountExists = defaultAccount != null
            if (defaultAccountExists) {
                throw AccountAlreadyExistsException()
            }

            val saved = saveUser(googleUser)
            println("saved user: ${saved.toString()}")
            return if (saved != null) {
                jwtService.generateAuthResponse(saved.toUserDetails())
            } else {
                null
            }
        }
    }

    // TODO: Should we have it transactional, so we can rollback if one of the saves fails?
    private fun saveUser(googleUser: GoogleUser): User? {
        googleAccountService.save(googleUser.toGoogleAccount()) ?: return null
        return userService.save(googleUser.toUser())
    }

}

