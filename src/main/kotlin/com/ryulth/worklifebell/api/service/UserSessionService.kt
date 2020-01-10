package com.ryulth.worklifebell.api.service

import com.ryulth.worklifebell.api.exception.UserNotFoundException
import com.ryulth.worklifebell.api.model.User
import com.ryulth.worklifebell.api.model.UserSession
import com.ryulth.worklifebell.api.util.UserSessionUtils
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class UserSessionService (
    private val userService: UserService
) {
    companion object: KLogging()

    fun getCurrentUser(): User {
        return UserSessionUtils.getCurrentUser() ?: run {
            val user = userService.findUserById(this.getCurrentUserSession().id)
            this.setCurrentUser(user)
            user
        }
    }

    fun setCurrentUser(user: User) {
        UserSessionUtils.setCurrentUser(user)
    }

    fun getCurrentUserSession(): UserSession {
        return UserSessionUtils.getCurrentUserSession()?: throw UserNotFoundException("UserSession invalid")
    }

    fun setCurrentUserSession(userSession: UserSession) {
        UserSessionUtils.setCurrentUserSession(userSession)
    }

    fun clear() {
        UserSessionUtils.clear()
    }
}