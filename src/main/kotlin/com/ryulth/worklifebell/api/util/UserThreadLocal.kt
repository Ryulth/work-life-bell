package com.ryulth.worklifebell.api.util

import com.ryulth.worklifebell.api.model.User

class UserThreadLocal {
    companion object {
        private val userThreadLocal = ThreadLocal<User>()

        fun setUser(user: User) {
            userThreadLocal.set(user)
        }
        fun getUser(): User? {
            return userThreadLocal.get()
        }
        fun remove() {
            userThreadLocal.remove()
        }
    }
}