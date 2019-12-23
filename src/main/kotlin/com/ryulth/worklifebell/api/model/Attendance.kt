package com.ryulth.worklifebell.api.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

/**
 * 출근이 인정 될 경우 등록되는 raw Data
 */
@Entity
@Table(name = "Attendance")
@IdClass(AttendanceIdClass::class)
data class Attendance(
    @Id val userId: Long,
    @Id @Column(length = 64) val onWorkDate: String,
    @Column(length = 64) var onWorkDateTime: String,
    @Column(length = 64) var offWorkDateTime: String ? = null
)

data class AttendanceIdClass(
    var userId: Long= 0L,
    var onWorkDate: String= ""
) : Serializable
