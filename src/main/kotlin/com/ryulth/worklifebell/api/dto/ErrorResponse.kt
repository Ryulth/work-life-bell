package com.ryulth.worklifebell.api.dto

data class ErrorResponse(
    val error: String,
    val code: Int,
    val message: String
)
