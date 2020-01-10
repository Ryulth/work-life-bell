package com.ryulth.worklifebell.api.repository

import com.ryulth.worklifebell.api.model.Attendance
import com.ryulth.worklifebell.api.model.AttendanceIdClass
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

interface AttendanceRepository : JpaRepository<Attendance, AttendanceIdClass> {
    @Transactional
    @Modifying
    @Query(
        value = " INSERT INTO attendance (user_id, on_work_date, on_work_date_time) VALUES (?1, ?2, ?3)",
        nativeQuery = true
    )
    fun insert(userId: Long, onWorkDate: LocalDate, onWorkDateTime: LocalDateTime)
    fun findByUserIdAndOnWorkDate(userId: Long, onWorkDate: LocalDate): Attendance?
    fun findByUserIdAndOnWorkDateBetween(userId: Long, start: LocalDate, end: LocalDate): List<Attendance>
}
