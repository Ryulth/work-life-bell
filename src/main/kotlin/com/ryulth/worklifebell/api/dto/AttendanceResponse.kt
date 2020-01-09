package com.ryulth.worklifebell.api.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime

data class AttendanceResponse (
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val onWorkDate: LocalDate,
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val onWorkDateTime: LocalDateTime ? = null,
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val offWorkDateTime: LocalDateTime ? = null,
    val weeklyWorkTime: String ? = null,
    val monthlyWorkTime: String ? = null
)