package com.ryulth.worklifebell.api.repository

import com.ryulth.worklifebell.api.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmail(userEmail: String): Boolean
    fun findByEmail(userEmail: String): User?
}
