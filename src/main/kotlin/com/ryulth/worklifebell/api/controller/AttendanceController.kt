package com.ryulth.worklifebell.api.controller

import com.ryulth.worklifebell.api.dto.AttendanceResponse
import com.ryulth.worklifebell.api.model.Attendance
import com.ryulth.worklifebell.api.dto.OnWorkTimeRequest
import com.ryulth.worklifebell.api.service.AttendanceService
import io.swagger.annotations.ApiOperation
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/attendance")
class AttendanceController(
    val attendanceService: AttendanceService
) {
    companion object : KLogging()

    @ApiOperation("오늘 날짜의 출근 기록")
    @GetMapping
    fun getTodayAttendance(): AttendanceResponse {
        return attendanceService.getAttendanceToday()
    }

    @ApiOperation("오늘 날짜로 출근 찍기")
    @PostMapping("/onwork")
    fun onWork(): AttendanceResponse {
        return attendanceService.onWork()
    }

    @ApiOperation("오늘 날짜로 출근 시간 조정")
    @PutMapping("/onwork")
    fun fixOnWorkTime(@RequestBody onWorkTimeRequest: OnWorkTimeRequest): AttendanceResponse {
        return attendanceService.fixOnWorkTime(onWorkTimeRequest)
    }
    @ApiOperation("오늘 날짜로 퇴근 찍기")
    @PostMapping("/offwork")
    fun offWork(): AttendanceResponse {
        return attendanceService.offWork()
    }
}
