package com.ryulth.worklifebell.api.model

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

/**
 * 출근이 인정 될 경우 등록되는 raw Data
 */
@Entity
@Table(name = "Attendance")
@IdClass(AttendanceIdClass::class)
data class Attendance(
    @Id val userId: Long,
    @Id
    @Column val onWorkDate: LocalDate,
    @Column var onWorkDateTime: LocalDateTime? = null,
    @Column var offWorkDateTime: LocalDateTime? = null
)

data class AttendanceIdClass(
    var userId: Long = 0L,
    var onWorkDate: LocalDate? = null
) : Serializable
