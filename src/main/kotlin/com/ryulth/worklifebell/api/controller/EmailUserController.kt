package com.ryulth.worklifebell.api.controller

import com.ryulth.worklifebell.api.auth.Token
import com.ryulth.worklifebell.api.auth.TokenPublisher
import com.ryulth.worklifebell.api.model.User
import com.ryulth.worklifebell.api.model.request.EmailLoginRequest
import com.ryulth.worklifebell.api.model.request.EmailRegisterRequest
import com.ryulth.worklifebell.api.service.EmailUserService
import io.swagger.annotations.ApiOperation
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

    @ApiOperation(value = "Email 로그인 API", notes = "Authorization Header 필요 없습니다. Swagger 전역 설정 원인")
    @PostMapping("/login")
    fun login(@RequestBody emailLoginRequest: EmailLoginRequest): ResponseEntity<Token> {
        logger.info { "login payload : $emailLoginRequest" }
        val user: User = emailUserService.loginByEmail(emailLoginRequest)
        return ResponseEntity(tokenPublisher.publishToken(user.id!!, user.email), HttpStatus.OK)
    }

    @ApiOperation(value = "Email 회원가입 API", notes = "Authorization Header 필요 없습니다. Swagger 전역 설정 원인")
    @PostMapping("/register")
    fun register(@RequestBody emailRegisterRequest: EmailRegisterRequest): ResponseEntity<Token> {
        logger.info { "register payload : $emailRegisterRequest" }
        val user: User = emailUserService.registerByEmail(emailRegisterRequest)
        return ResponseEntity(tokenPublisher.publishToken(user.id!!, user.email), HttpStatus.OK)
    }
}
