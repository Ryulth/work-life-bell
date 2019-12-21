package com.ryulth.offthework.api.auth

data class Token(
    val accessToken: String,
    val refreshToken: String
)
