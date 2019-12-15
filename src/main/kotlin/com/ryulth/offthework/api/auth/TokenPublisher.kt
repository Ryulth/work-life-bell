package com.ryulth.offthework.api.auth

import com.ryulth.offthework.api.auth.jwt.JwtProvider
import com.ryulth.offthework.api.service.UserService
import org.springframework.stereotype.Service

@Service
class TokenPublisher(
    val jwtProvider: JwtProvider,
    val userService: UserService
) {
    fun publishToken(userId: Long, userEmail: String): Token {
        val accessToken = jwtProvider.publishToken(userId , userEmail, true)
        val refreshToken = jwtProvider.publishToken(userId, userEmail, false)
        return Token(accessToken = accessToken, refreshToken = refreshToken)
    }

    fun updateToken(refreshToken: String): Token {
        val idAndEmailPair= jwtProvider.getUserIdAndEmailPair(refreshToken, false)
        if (userService.isUserExistById(idAndEmailPair.first)){
            return publishToken(idAndEmailPair.first, idAndEmailPair.second)
        }
        throw UnauthorizedException("User not found")
    }
}