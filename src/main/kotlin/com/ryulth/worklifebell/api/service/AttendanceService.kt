package com.ryulth.worklifebell.api.service

import com.ryulth.worklifebell.api.exception.AlreadyOffWorkException
import com.ryulth.worklifebell.api.exception.AlreadyOnWorkException
import com.ryulth.worklifebell.api.exception.AttendanceNotFoundException
import com.ryulth.worklifebell.api.model.Attendance
import com.ryulth.worklifebell.api.model.AttendanceIdClass
import com.ryulth.worklifebell.api.model.request.OnWorkTimeRequest
import com.ryulth.worklifebell.api.repository.AttendanceRepository
import com.ryulth.worklifebell.api.util.DateFormatUtils
import com.ryulth.worklifebell.api.util.UserInfoThreadLocal
import java.time.LocalDateTime
import java.time.ZoneId
import mu.KLogging
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
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
    fun onWork(): Attendance {
        val now = LocalDateTime.now(zoneId)
        val attendanceIdClass = AttendanceIdClass(
            UserInfoThreadLocal.getUserInfo().id,
            now.toLocalDate().toString()
        )
        try {
            attendanceRepository.insert(attendanceIdClass.userId, attendanceIdClass.onWorkDate, now.toString())
            return attendanceRepository.getOne(attendanceIdClass)
        } catch (e: Exception){
            when(e) {
                is DataIntegrityViolationException,
                is ConstraintViolationException,
                is SQLIntegrityConstraintViolationException ->
                    throw AlreadyOnWorkException("Today already on work")
                else -> throw e
            }
        }
    }
    fun fixOnWorkTime(onWorkTimeRequest: OnWorkTimeRequest): Attendance {
        DateFormatUtils.validateDateTime(onWorkTimeRequest.onWorkTime)

        val now = LocalDateTime.now(zoneId)
        val attendanceIdClass = AttendanceIdClass(
            UserInfoThreadLocal.getUserInfo().id,
            now.toLocalDate().toString()
        )

        val attendance = attendanceRepository.findByIdOrNull(attendanceIdClass)
            ?: throw AttendanceNotFoundException("Today not attendance")

        attendance.onWorkDateTime = onWorkTimeRequest.onWorkTime

        return attendance
    }
    fun offWork(): Attendance {
        val now = LocalDateTime.now(zoneId)
        val attendanceIdClass = AttendanceIdClass(
            UserInfoThreadLocal.getUserInfo().id,
            now.toLocalDate().toString()
        )
        val attendance = attendanceRepository.findByIdOrNull(attendanceIdClass)
            ?: throw AttendanceNotFoundException("Today not attendance")

        attendance.offWorkDateTime?.let {
            throw AlreadyOffWorkException("Today already off work")
        } ?: run {
            attendance.offWorkDateTime = now.toString()
            attendanceRepository.save(attendance)
        }

        return attendance
    }

    fun getAttendanceToday(): Attendance {
        val now = LocalDateTime.now(zoneId)
        val attendance = attendanceRepository.findByUserIdAndOnWorkDate(
            UserInfoThreadLocal.getUserInfo().id, now.toLocalDate().toString()
        )

        return attendance ?: throw AttendanceNotFoundException("Today not attendance")
    }
}
