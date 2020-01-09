package com.ryulth.worklifebell.api.service

import com.ryulth.worklifebell.api.dto.AttendanceResponse
import com.ryulth.worklifebell.api.dto.OnWorkTimeRequest
import com.ryulth.worklifebell.api.exception.AlreadyOffWorkException
import com.ryulth.worklifebell.api.exception.AlreadyOnWorkException
import com.ryulth.worklifebell.api.exception.AttendanceNotFoundException
import com.ryulth.worklifebell.api.model.AttendanceIdClass
import com.ryulth.worklifebell.api.repository.AttendanceRepository
import com.ryulth.worklifebell.api.util.UserInfoThreadLocal
import mu.KLogging
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.SQLIntegrityConstraintViolationException
import java.time.LocalDateTime
import java.time.ZoneId


@Service
class AttendanceService(
    val attendanceRepository: AttendanceRepository,
    val locationService: LocationService,
    val userService: UserService
) {
    companion object : KLogging() {
        val zoneId = ZoneId.of("Asia/Seoul")!!
    }
    fun onWork(): AttendanceResponse {
        val now = LocalDateTime.now(zoneId)
        val attendanceIdClass = AttendanceIdClass(
            UserInfoThreadLocal.getUserInfo().id,
            now.toLocalDate()
        )
        try {
            attendanceRepository.insert(attendanceIdClass.userId, now.toLocalDate(), now)
            val attendance = attendanceRepository.getOne(attendanceIdClass)
            return AttendanceResponse(
                onWorkDate = now.toLocalDate() ,
                onWorkDateTime = attendance?.onWorkDateTime,
                offWorkDateTime = attendance?.offWorkDateTime,
                weeklyWorkTime = calcWeeklyWorkTime(),
                monthlyWorkTime = calcMonthlyWorkTime()
            )
        } catch (e: Exception){
            when(e) {
                is DataIntegrityViolationException,
                is ConstraintViolationException,
                is SQLIntegrityConstraintViolationException ->
                    throw AlreadyOnWorkException("Today already on work $e")
                else -> throw e
            }
        }
    }
    fun fixOnWorkTime(onWorkTimeRequest: OnWorkTimeRequest): AttendanceResponse {
        val now = LocalDateTime.now(zoneId)
        val attendanceIdClass = AttendanceIdClass(
            UserInfoThreadLocal.getUserInfo().id,
            now.toLocalDate()
        )

        val attendance = attendanceRepository.findByIdOrNull(attendanceIdClass)
            ?: throw AttendanceNotFoundException("Today not attendance")

        attendance.onWorkDateTime = onWorkTimeRequest.onWorkTime

        return AttendanceResponse(
            onWorkDate = now.toLocalDate() ,
            onWorkDateTime = attendance?.onWorkDateTime,
            offWorkDateTime = attendance?.offWorkDateTime,
            weeklyWorkTime = calcWeeklyWorkTime(),
            monthlyWorkTime = calcMonthlyWorkTime()
        )
    }
    fun offWork(): AttendanceResponse {
        val now = LocalDateTime.now(zoneId)
        val attendanceIdClass = AttendanceIdClass(
            UserInfoThreadLocal.getUserInfo().id,
            now.toLocalDate()
        )
        val attendance = attendanceRepository.findByIdOrNull(attendanceIdClass)
            ?: throw AttendanceNotFoundException("Today not attendance")

        attendance.offWorkDateTime?.let {
            throw AlreadyOffWorkException("Today already off work")
        } ?: run {
            attendance.offWorkDateTime = now
            attendanceRepository.save(attendance)
        }

        return AttendanceResponse(
            onWorkDate = now.toLocalDate() ,
            onWorkDateTime = attendance?.onWorkDateTime,
            offWorkDateTime = attendance?.offWorkDateTime,
            weeklyWorkTime = calcWeeklyWorkTime(),
            monthlyWorkTime = calcMonthlyWorkTime()
        )
    }

    fun getAttendanceToday(): AttendanceResponse {
        val now = LocalDateTime.now(zoneId)
        val attendance = attendanceRepository.findByUserIdAndOnWorkDate(
            UserInfoThreadLocal.getUserInfo().id, now.toLocalDate()
        )
        return AttendanceResponse(
            onWorkDate = now.toLocalDate() ,
            onWorkDateTime = attendance?.onWorkDateTime,
            offWorkDateTime = attendance?.offWorkDateTime,
            weeklyWorkTime = calcWeeklyWorkTime(),
            monthlyWorkTime = calcMonthlyWorkTime()
        )
    }

    fun calcWeeklyWorkTime() = null
    fun calcMonthlyWorkTime() = null
}
