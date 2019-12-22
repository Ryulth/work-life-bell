package com.ryulth.worklifebell.api.auth

data class Token(
    val accessToken: String,
    val refreshToken: String
)
