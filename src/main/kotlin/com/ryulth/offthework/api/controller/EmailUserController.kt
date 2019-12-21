package com.ryulth.offthework.api.controller

import com.ryulth.offthework.api.auth.Token
import com.ryulth.offthework.api.auth.TokenPublisher
import com.ryulth.offthework.api.model.User
import com.ryulth.offthework.api.model.request.EmailLoginRequest
import com.ryulth.offthework.api.model.request.EmailRegisterRequest
import com.ryulth.offthework.api.service.EmailUserService
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth/email")
class EmailUserController(
    val emailUserService: EmailUserService,
    val tokenPublisher: TokenPublisher
) {
    companion object : KLogging()

    @PostMapping("/login")
    fun login(@RequestBody emailLoginRequest: EmailLoginRequest): ResponseEntity<Token> {
        logger.info { "login payload : $emailLoginRequest" }
        val user: User = emailUserService.loginByEmail(emailLoginRequest)
        return ResponseEntity(tokenPublisher.publishToken(user.id!!, user.email), HttpStatus.OK)
    }

    @PostMapping("/register")
    fun register(@RequestBody emailRegisterRequest: EmailRegisterRequest): ResponseEntity<Token> {
        logger.info { "register payload : $emailRegisterRequest" }
        val user: User = emailUserService.registerByEmail(emailRegisterRequest)
        return ResponseEntity(tokenPublisher.publishToken(user.id!!, user.email), HttpStatus.OK)
    }
}
