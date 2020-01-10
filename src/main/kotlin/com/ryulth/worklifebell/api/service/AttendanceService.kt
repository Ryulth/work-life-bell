package com.ryulth.worklifebell.api.service

import com.ryulth.worklifebell.api.dto.AttendanceResponse
import com.ryulth.worklifebell.api.dto.OnWorkTimeRequest
import com.ryulth.worklifebell.api.exception.AlreadyOffWorkException
import com.ryulth.worklifebell.api.exception.AlreadyOnWorkException
import com.ryulth.worklifebell.api.exception.AttendanceNotFoundException
import com.ryulth.worklifebell.api.model.Attendance
import com.ryulth.worklifebell.api.model.AttendanceIdClass
import com.ryulth.worklifebell.api.repository.AttendanceRepository
import com.ryulth.worklifebell.api.util.DateFormatUtils
import com.ryulth.worklifebell.api.util.sumByLong
import mu.KLogging
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.SQLIntegrityConstraintViolationException
import java.time.*
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters.*


@Service
class AttendanceService(
    val attendanceRepository: AttendanceRepository,
    val locationService: LocationService,
    val userService: UserService,
    val userSessionService: UserSessionService
) {
    companion object : KLogging() {
        val zoneId = ZoneId.of("Asia/Seoul")!!
    }
    fun onWork(): AttendanceResponse {
        val now = LocalDateTime.now(zoneId).withNano(0)
        val attendanceIdClass = AttendanceIdClass(
            userSessionService.getCurrentUserSession().id,
            now.toLocalDate()
        )
        try {
            attendanceRepository.insert(attendanceIdClass.userId, now.toLocalDate(), now)
            val attendance = attendanceRepository.getOne(attendanceIdClass)
            return AttendanceResponse(
                onWorkDate = now.toLocalDate() ,
                onWorkDateTime = attendance?.onWorkDateTime,
                offWorkDateTime = attendance?.offWorkDateTime,
                weeklyWorkTime = calcWeeklyWorkTime(now.toLocalDate()),
                monthlyWorkTime = calcMonthlyWorkTime(now.toLocalDate())
            )
        } catch (e: Exception){
            when(e) {
                is DataIntegrityViolationException,
                is ConstraintViolationException,
                is SQLIntegrityConstraintViolationException ->
                    throw AlreadyOnWorkException("Today already on work e: $e")
                else -> throw e
            }
        }
    }
    fun fixOnWorkTime(onWorkTimeRequest: OnWorkTimeRequest): AttendanceResponse {
        val now = LocalDateTime.now(zoneId).withNano(0)
        val attendanceIdClass = AttendanceIdClass(
            userSessionService.getCurrentUserSession().id,
            now.toLocalDate()
        )

        val attendance = attendanceRepository.findByIdOrNull(attendanceIdClass)
            ?: throw AttendanceNotFoundException("Today not attendance")

        attendance.onWorkDateTime = onWorkTimeRequest.onWorkTime

        return AttendanceResponse(
            onWorkDate = now.toLocalDate() ,
            onWorkDateTime = attendance?.onWorkDateTime,
            offWorkDateTime = attendance?.offWorkDateTime,
            weeklyWorkTime = calcWeeklyWorkTime(now.toLocalDate()),
            monthlyWorkTime = calcMonthlyWorkTime(now.toLocalDate())
        )
    }
    fun offWork(): AttendanceResponse {
        val now = LocalDateTime.now(zoneId).withNano(0)
        val attendanceIdClass = AttendanceIdClass(
            userSessionService.getCurrentUserSession().id,
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
            weeklyWorkTime = calcWeeklyWorkTime(now.toLocalDate()),
            monthlyWorkTime = calcMonthlyWorkTime(now.toLocalDate())
        )
    }

    fun getAttendanceToday(): AttendanceResponse {
        val now = LocalDateTime.now(zoneId).withNano(0)
        val attendance = attendanceRepository.findByUserIdAndOnWorkDate(
            userSessionService.getCurrentUserSession().id, now.toLocalDate()
        )
        return AttendanceResponse(
            onWorkDate = now.toLocalDate() ,
            onWorkDateTime = attendance?.onWorkDateTime,
            offWorkDateTime = attendance?.offWorkDateTime,
            weeklyWorkTime = calcWeeklyWorkTime(now.toLocalDate()),
            monthlyWorkTime = calcMonthlyWorkTime(now.toLocalDate())
        )
    }

    fun calcWeeklyWorkTime(target: LocalDate): String {
        val start = target.with(previousOrSame(DayOfWeek.MONDAY))
        val end = target.with(nextOrSame(DayOfWeek.SUNDAY))
        val attendances = attendanceRepository.findByUserIdAndOnWorkDateBetween(
            userId = userSessionService.getCurrentUserSession().id,
            start = start,
            end = end
        )
        return DateFormatUtils.localTimeOfSecondOfDay(calcWorkTime(attendances))
    }
    fun calcMonthlyWorkTime(target: LocalDate): String {
        val start = target.with(firstDayOfMonth())
        val end = target.with(lastDayOfMonth())
        val attendances = attendanceRepository.findByUserIdAndOnWorkDateBetween(
            userId = userSessionService.getCurrentUserSession().id,
            start = start,
            end = end
        )
        return DateFormatUtils.localTimeOfSecondOfDay(calcWorkTime(attendances))
    }
    private fun calcWorkTime(attendances: List<Attendance>): Long {
        return attendances.sumByLong { it.onWorkDateTime?.until(it.offWorkDateTime?: it.onWorkDateTime, ChronoUnit.SECONDS)!! }
    }
}
