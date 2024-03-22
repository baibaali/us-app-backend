package com.baibaali.usapp.authentication.controller

import com.baibaali.usapp.authentication.dto.AuthResponse
import com.baibaali.usapp.authentication.dto.GoogleAuthRequest
import com.baibaali.usapp.authentication.excpetion.AccountAlreadyExistsException
import com.baibaali.usapp.authentication.service.OAuth2Service
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/oauth2")
class OAuth2Controller(
    private val oAuth2Service: OAuth2Service
) {
    @PostMapping("/google")
    fun googleLogin(@RequestBody request: GoogleAuthRequest): ResponseEntity<AuthResponse> {
        try{
            val response = oAuth2Service.authenticateWithGoogle(request)
                ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            return ResponseEntity.ok(response)
        } catch (e: AccountAlreadyExistsException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }
}