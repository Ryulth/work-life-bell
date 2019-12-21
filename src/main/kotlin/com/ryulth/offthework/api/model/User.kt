package com.ryulth.offthework.api.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "User")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val email: String,
    val password: String,
    @Column(length = 16) val workLatitude: String? = null, // 위도
    @Column(length = 16) val workLongitude: String? = null, // 경도
    val loginType: LoginType
)
