package com.ryulth.worklifebell.api.util

import com.ryulth.worklifebell.api.model.UserInfo

class UserInfoThreadLocal {
    companion object {
        private val userInfoThreadLocal = ThreadLocal<UserInfo>()

        fun setUserInfo(userInfo: UserInfo) {
            userInfoThreadLocal.set(userInfo)
        }
        fun getUserInfo(): UserInfo {
            return userInfoThreadLocal.get()
        }
        fun remove() {
            userInfoThreadLocal.remove()
        }
    }
}
