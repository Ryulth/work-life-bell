package com.ryulth.offthework.api.repository

import com.ryulth.offthework.api.model.Attendance
import org.springframework.data.jpa.repository.JpaRepository

interface AttendanceRepository : JpaRepository<Attendance, Long>
