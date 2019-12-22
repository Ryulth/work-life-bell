package com.ryulth.worklifebell.api.repository

import com.ryulth.worklifebell.api.model.Attendance
import com.ryulth.worklifebell.api.model.AttendanceIdClass
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface AttendanceRepository : JpaRepository<Attendance, AttendanceIdClass> {
    @Transactional
    @Modifying
    @Query(
        value = " INSERT INTO attendance (user_id, go_to_work_date, go_to_work_date_time) VALUES (?1, ?2, ?3)",
        nativeQuery = true
    )
    fun insert(userId: Long, goToWorkDate: String, gotoWorkDateTime: String)
    fun findByUserIdAndGoToWorkDate(userId: Long, goToWorkDate: String): Attendance?
}
