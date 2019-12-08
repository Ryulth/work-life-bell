package com.ryulth.offthework.api.service

import com.ryulth.offthework.api.model.Attendance
import com.ryulth.offthework.api.repository.AttendanceRepository
import org.springframework.stereotype.Service

@Service
class AttendanceService(
    val attendanceRepository: AttendanceRepository,
    val locationService: LocationService,
    val userService: UserService
) {
    fun goToWork(userId: Long) {
        if (locationService.isGoToWork(userService.getUserWorkLocation(userId))) {
            // TODO 앙 출근띠
            attendanceRepository.save(Attendance(userId = userId))
        }
    }
    fun getOffWork() {
        // TODO 앙퇴근띠
    }
}
