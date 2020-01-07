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
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AccountController(
    val emailUserService: EmailUserService,
    val tokenPublisher: TokenPublisher
) {
    companion object : KLogging()

    @ApiOperation(value = "Email 로그인 API", notes = "Authorization Header 필요 없습니다. Swagger 전역 설정 원인")
    @PostMapping("/login/email")
    fun loginWithEmail(@RequestBody emailLoginRequest: EmailLoginRequest): ResponseEntity<Token> {
        logger.info { "login payload : $emailLoginRequest" }
        val user: User = emailUserService.loginByEmail(emailLoginRequest)
        return ResponseEntity(tokenPublisher.publishToken(user.id!!, user.email), HttpStatus.OK)
    }

    @ApiOperation(value = "Email 회원가입 API", notes = "Authorization Header 필요 없습니다. Swagger 전역 설정 원인")
    @PostMapping("/register/email")
    fun registerWithEmail(@RequestBody emailRegisterRequest: EmailRegisterRequest): ResponseEntity<Token> {
        logger.info { "register payload : $emailRegisterRequest" }
        val user: User = emailUserService.registerByEmail(emailRegisterRequest)
        return ResponseEntity(tokenPublisher.publishToken(user.id!!, user.email), HttpStatus.OK)
    }

    @ApiOperation(value = "Update Access Token API", notes = "refresh_token 을 보내면 access_token 을 반환해주는 API. (Authorization Header 필요 없습니다. Swagger 전역 설정 원인)")
    @PostMapping("/refresh/{token:.+}")
    fun refreshToken(@PathVariable("token") refreshToken: String?): Token? {
        return refreshToken?.let { tokenPublisher.refreshToken(it) }
    }
}
