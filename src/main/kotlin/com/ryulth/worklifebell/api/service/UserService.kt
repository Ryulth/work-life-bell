package com.ryulth.worklifebell.api.service

import com.ryulth.worklifebell.api.model.Location
import com.ryulth.worklifebell.api.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {

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
}
