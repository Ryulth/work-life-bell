package com.ryulth.offthework.api.controller

import com.ryulth.offthework.api.model.Attendance
import com.ryulth.offthework.api.service.AttendanceService
import io.swagger.annotations.ApiOperation
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

    @ApiOperation("오늘 날자로 출근 찍기")
    @PostMapping
    fun goToWork(): Attendance {
        return attendanceService.goToWork()
    }

    @ApiOperation("오늘 날짜의 출근 기록")
    @GetMapping
    fun getTodayAttendance(): Attendance {
        return attendanceService.getAttendanceToday()
    }
}
