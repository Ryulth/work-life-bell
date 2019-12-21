package com.ryulth.offthework.api.auth

import com.ryulth.offthework.api.auth.jwt.JwtProvider
import com.ryulth.offthework.api.exception.UnauthorizedException
import com.ryulth.offthework.api.service.UserService
import org.springframework.stereotype.Service

@Service
class TokenPublisher(
    val jwtProvider: JwtProvider,
    val userService: UserService
) {
    fun publishToken(userId: Long, userEmail: String): Token {
        val accessToken = jwtProvider.publishToken(userId, userEmail, true)
        val refreshToken = jwtProvider.publishToken(userId, userEmail, false)
        return Token(accessToken = accessToken, refreshToken = refreshToken)
    }

    fun updateToken(refreshToken: String): Token {
        val userInfo = jwtProvider.getUserInfo(refreshToken, false)
        if (userService.isUserExistById(userInfo.id)) {
            return publishToken(userInfo.id, userInfo.email)
        }
        throw UnauthorizedException("User not found")
    }
}
