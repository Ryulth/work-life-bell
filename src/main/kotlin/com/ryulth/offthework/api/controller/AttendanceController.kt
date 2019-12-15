package com.ryulth.offthework.api.controller

import com.ryulth.offthework.api.model.Attendance
import com.ryulth.offthework.api.model.response.ErrorResponse
import com.ryulth.offthework.api.service.AttendanceService
import com.ryulth.offthework.api.util.AttendanceNotFoundException
import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/attendance")
class AttendanceController(
    val attendanceService: AttendanceService
) {
    companion object: KLogging() {
        val httpHeaders = HttpHeaders()
    }
    @PostMapping
    fun goToWork() {
        attendanceService.goToWork(1L)
    }

    @GetMapping
    fun getTodayAttendance(): ResponseEntity<Any> {
        return try {
            val attendance: Attendance = attendanceService.getAttendanceToday(1L)
            ResponseEntity(attendance, httpHeaders, HttpStatus.OK)
        } catch (e: Exception) {
            logger.error { e }
            when(e) {
                is AttendanceNotFoundException-> {
                    val errorResponse = ErrorResponse(HttpStatus.NO_CONTENT.value(), e.toString())
                    ResponseEntity(errorResponse, httpHeaders, HttpStatus.NO_CONTENT)
                }
                else -> {
                    ResponseEntity(
                        ErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString()), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR
                    )
                }
            }
        }
    }

}
