package com.ryulth.worklifebell.api.auth

import com.ryulth.worklifebell.api.auth.jwt.JwtProvider
import com.ryulth.worklifebell.api.exception.UnauthorizedException
import com.ryulth.worklifebell.api.service.UserService
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

    fun refreshToken(refreshToken: String): Token {
        val userInfo = jwtProvider.getUserSession(refreshToken, false)
        if (userService.isUserExistById(userInfo.id)) {
            return publishToken(userInfo.id, userInfo.email)
        }
        throw UnauthorizedException("User not found")
    }
}
