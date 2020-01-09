package com.ryulth.worklifebell.api.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

data class OnWorkTimeRequest(
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val onWorkTime: LocalDateTime
)