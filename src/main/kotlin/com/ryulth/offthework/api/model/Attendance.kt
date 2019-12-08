package com.ryulth.offthework.api.model

import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

/**
 * 출근이 인정 될 경우 등록되는 raw Data
 */
@Entity
@Table(name = "Attendance",
    uniqueConstraints = [UniqueConstraint(columnNames = ["userId", "goToWorkDateTime"])]
)
data class Attendance(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val userId: Long,
    val goToWorkDateTime: OffsetDateTime = OffsetDateTime.now(),
    val getOffWorkDateTime: OffsetDateTime ? = null,
    val timeOffset: String = OffsetDateTime.now().offset.toString()
)
