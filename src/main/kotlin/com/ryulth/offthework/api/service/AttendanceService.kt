package com.ryulth.offthework.api.service

import com.ryulth.offthework.api.exception.AlreadyAttendanceException
import com.ryulth.offthework.api.exception.AttendanceNotFoundException
import com.ryulth.offthework.api.model.Attendance
import com.ryulth.offthework.api.model.AttendanceIdClass
import com.ryulth.offthework.api.repository.AttendanceRepository
import java.time.LocalDateTime
import java.time.ZoneId
import mu.KLogging
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.sql.SQLIntegrityConstraintViolationException

@Service
class AttendanceService(
    val attendanceRepository: AttendanceRepository,
    val locationService: LocationService,
    val userService: UserService
) {
    companion object : KLogging() {
        val zoneId = ZoneId.of("Asia/Seoul")!!
    }
    fun goToWork(): Attendance {
        val now = LocalDateTime.now(zoneId)
        val attendanceIdClass = AttendanceIdClass(
            UserInfoThreadLocal.getUserInfo().id,
            now.toLocalDate().toString()
        )
        try {
            attendanceRepository.insert(attendanceIdClass.userId, attendanceIdClass.goToWorkDate, now.toString())
            return attendanceRepository.getOne(attendanceIdClass)
        } catch (e: Exception){
            when(e) {
                is DataIntegrityViolationException,
                is ConstraintViolationException,
                is SQLIntegrityConstraintViolationException ->
                    throw AlreadyAttendanceException("Today already attendance")
                else -> throw e
            }
        }
    }
    fun getOffWork() {
        // TODO 앙퇴근띠
    }

    fun getAttendanceToday(): Attendance {
        val now = LocalDateTime.now(zoneId)
        val attendance = attendanceRepository.findByUserIdAndGoToWorkDate(
            UserInfoThreadLocal.getUserInfo().id, now.toLocalDate().toString()
        )

        return attendance ?: throw AttendanceNotFoundException("Today not attendance")
    }
}
