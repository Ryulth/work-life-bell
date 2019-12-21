package com.ryulth.offthework.api.service

import com.ryulth.offthework.api.exception.AttendanceNotFoundException
import com.ryulth.offthework.api.model.Attendance
import com.ryulth.offthework.api.repository.AttendanceRepository
import java.time.LocalDateTime
import java.time.ZoneId
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class AttendanceService(
    val attendanceRepository: AttendanceRepository,
    val locationService: LocationService,
    val userService: UserService
) {
    companion object : KLogging() {
        val zoneId = ZoneId.of("Asia/Seoul")!!
    }
    fun goToWork(userId: Long) {
        val now = LocalDateTime.now(zoneId)
        val attendance = Attendance(
            userId = userId,
            goToWorkDate = now.toLocalDate().toString(),
            goToWorkDateTime = now.toString()
        )
        attendanceRepository.save(attendance)
    }
    fun getOffWork() {
        // TODO 앙퇴근띠
    }

    fun getAttendanceToday(userId: Long): Attendance {
        val now = LocalDateTime.now(zoneId)
        val attendance = attendanceRepository.findByUserIdAndGoToWorkDate(userId, now.toLocalDate().toString())

        return attendance ?: throw AttendanceNotFoundException("Today not attendance")
    }
}
