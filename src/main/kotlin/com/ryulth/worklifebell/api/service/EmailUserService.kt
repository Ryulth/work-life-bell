package com.ryulth.worklifebell.api.service

import com.ryulth.worklifebell.api.exception.EmailInvalidException
import com.ryulth.worklifebell.api.exception.UserNotFoundException
import com.ryulth.worklifebell.api.model.LoginType
import com.ryulth.worklifebell.api.model.User
import com.ryulth.worklifebell.api.model.request.EmailLoginRequest
import com.ryulth.worklifebell.api.model.request.EmailRegisterRequest
import com.ryulth.worklifebell.api.repository.UserRepository
import java.util.regex.Pattern
import mu.KLogging
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

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
        if (!emailPattern.matcher(newEmail).matches()) {
            logger.warn { "email $newEmail pattern invalid" }
            throw EmailInvalidException("Email pattern invalid")
        }
        if (isDuplicateEmail(newEmail)) {
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
