package com.ryulth.offthework.api.model.response

data class ErrorResponse(
    val error: String,
    val code: Int,
    val message: String
)
