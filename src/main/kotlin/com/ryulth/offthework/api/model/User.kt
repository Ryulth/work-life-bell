package com.ryulth.offthework.api.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "User")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long,
    val email: String,
    val password: String,
    val workLatitude: String, // 위도
    val workLongitude: String, // 경도
    val loginType: LoginType
)
