package com.ryulth.offthework.api.controller

import com.ryulth.offthework.api.service.AttendanceService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/attendance")
class AttendanceController(
    val attendanceService: AttendanceService
) {
    @PostMapping("")
    fun goToWork() {
        attendanceService.goToWork(1L)
    }
}
