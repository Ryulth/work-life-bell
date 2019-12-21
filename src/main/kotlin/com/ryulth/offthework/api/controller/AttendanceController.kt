package com.ryulth.offthework.api.controller

import com.ryulth.offthework.api.model.Attendance
import com.ryulth.offthework.api.service.AttendanceService
import mu.KLogging
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
    companion object : KLogging()
    @PostMapping
    fun goToWork() {
        attendanceService.goToWork(1L)
    }

    @GetMapping
    fun getTodayAttendance(): ResponseEntity<Attendance> {
        val attendance: Attendance = attendanceService.getAttendanceToday(1L)
        return ResponseEntity(attendance, HttpStatus.OK)
    }
}
