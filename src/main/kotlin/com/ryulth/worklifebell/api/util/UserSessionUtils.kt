package com.ryulth.worklifebell.api.util

import com.ryulth.worklifebell.api.model.User
import com.ryulth.worklifebell.api.model.UserSession

object UserSessionUtils {
    private val userSessionThreadLocal = ThreadLocal<UserSession>()
    private val userThreadLocal = ThreadLocal<User>()

    fun setCurrentUser(user: User) {
        userThreadLocal.set(user)
    }
    fun getCurrentUser(): User? {
        return userThreadLocal.get()
    }

    fun setCurrentUserSession(userSession: UserSession) {
        userSessionThreadLocal.set(userSession)
    }

    fun getCurrentUserSession(): UserSession? {
        return userSessionThreadLocal.get()
    }

    fun clear() {
        userThreadLocal.remove()
        userSessionThreadLocal.remove()
    }
}