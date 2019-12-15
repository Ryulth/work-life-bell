package com.ryulth.offthework.api.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "User",
    uniqueConstraints = [UniqueConstraint(columnNames = ["email"])]
)
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val email: String,
    val password: String,
    val workLatitude: String? = null, // 위도
    val workLongitude: String? = null, // 경도
    val loginType: LoginType
)
