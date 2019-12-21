package com.ryulth.offthework.api.service

import com.ryulth.offthework.api.model.UserInfo

class UserInfoThreadLocal {
    companion object {
        private val threadLocal: ThreadLocal<UserInfo> = object : ThreadLocal<UserInfo>() {}

        fun setUserInfo(userInfo: UserInfo) {
            threadLocal.set(userInfo)
        }
        fun getUserInfo(): UserInfo {
            return threadLocal.get()
        }

        fun remove() {
            threadLocal.remove()
        }
    }
}
