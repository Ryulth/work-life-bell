package com.ryulth.worklifebell.api.service

import com.ryulth.worklifebell.api.exception.UserNotFoundException
import com.ryulth.worklifebell.api.model.Location
import com.ryulth.worklifebell.api.model.User
import com.ryulth.worklifebell.api.repository.UserRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {
    companion object: KLogging()
    fun getUserWorkLocation(userId: Long): Location {
        val user = userRepository.getOne(userId)
        return Location(latitude = user.workLatitude!!, longitude = user.workLongitude!!)
    }

    fun isUserExistById(userId: Long): Boolean {
        return userRepository.existsById(userId)
    }

    fun isUserExistByEmail(userEmail: String): Boolean {
        return userRepository.existsByEmail(userEmail)
    }

    fun findUserById(userId: Long): User {
        return userRepository.findById(userId).orElseThrow { UserNotFoundException("User not found by id: $userId") }
    }
}
