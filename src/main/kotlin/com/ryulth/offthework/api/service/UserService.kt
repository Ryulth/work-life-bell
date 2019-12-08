package com.ryulth.offthework.api.service

import com.ryulth.offthework.api.model.Location
import com.ryulth.offthework.api.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun getUserWorkLocation(userId: Long): Location {
        val user = userRepository.getOne(userId)
        return Location(latitude = user.workLatitude, longitude = user.workLongitude)
    }
}
