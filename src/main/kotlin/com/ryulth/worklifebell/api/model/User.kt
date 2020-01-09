package com.ryulth.worklifebell.api.model

import javax.persistence.*

@Entity
@Table(name = "User")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val email: String,
    val password: String,
    @Column(length = 16) val workLatitude: String? = null, // 위도
    @Column(length = 16) val workLongitude: String? = null, // 경도
    @Enumerated(EnumType.STRING)val loginType: LoginType
)
