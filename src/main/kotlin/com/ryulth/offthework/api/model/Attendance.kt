package com.ryulth.offthework.api.model

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
    @Id @Column(length = 64) val goToWorkDate: String,
    @Column(length = 64) val goToWorkDateTime: String,
    @Column(length = 64) val getOffWorkDateTime: String ? = null
)

data class AttendanceIdClass(
    val userId: Long,
    val goToWorkDate: String
) : Serializable
