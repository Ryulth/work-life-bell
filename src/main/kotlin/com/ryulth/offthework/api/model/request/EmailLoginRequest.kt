package com.ryulth.offthework.api.model.request

data class EmailLoginRequest (
    val email: String,
    val password: String
)