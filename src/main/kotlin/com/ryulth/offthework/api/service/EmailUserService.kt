package com.ryulth.offthework.api.service

import com.ryulth.offthework.api.model.LoginType
import com.ryulth.offthework.api.model.User
import com.ryulth.offthework.api.model.request.EmailLoginRequest
import com.ryulth.offthework.api.model.request.EmailRegisterRequest
import com.ryulth.offthework.api.repository.UserRepository
import com.ryulth.offthework.api.util.EmailInvalidException
import com.ryulth.offthework.api.util.UserNotFoundException
import mu.KLogging
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class EmailUserService(
    val userRepository: UserRepository
) {
    companion object : KLogging() {
        val passwordEncoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        val emailPattern: Pattern = Pattern.compile(
            "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            Pattern.CASE_INSENSITIVE
        )
    }

    fun loginByEmail(emailLoginRequest: EmailLoginRequest): User {
        val user: User = userRepository.findByEmail(emailLoginRequest.email)
            ?: throw UserNotFoundException("User not found by email")
        if (!passwordEncoder.matches(emailLoginRequest.password, user.password)) {
            throw BadCredentialsException("Incorrect password")
        }
        return user
    }

    fun registerByEmail(emailRegisterRequest: EmailRegisterRequest): User {
        val newEmail = emailRegisterRequest.email
        if(!emailPattern.matcher(newEmail).matches()) {
            logger.warn { "email $newEmail pattern invalid" }
            throw EmailInvalidException("Email pattern invalid")
        }
        if(isDuplicateEmail(newEmail)) {
            logger.warn { "email $newEmail is duplicated" }
            throw EmailInvalidException("Email is duplicated")
        }

        val user = User(
            email = newEmail,
            password = passwordEncoder.encode(emailRegisterRequest.password),
            loginType = LoginType.EMAIL
        )
        userRepository.save(user)
        return user
    }

    fun isDuplicateEmail(userEmail: String): Boolean {
        return userRepository.existsByEmail(userEmail)
    }
}