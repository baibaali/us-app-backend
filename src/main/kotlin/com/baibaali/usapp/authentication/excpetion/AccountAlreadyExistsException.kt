package com.baibaali.usapp.authentication.excpetion

class AccountAlreadyExistsException: RuntimeException() {
    override val message: String
        get() = "Account already exists"
}