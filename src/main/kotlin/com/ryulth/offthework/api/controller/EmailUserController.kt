package com.ryulth.offthework.api.controller

import com.ryulth.offthework.api.auth.Token
import com.ryulth.offthework.api.auth.TokenPublisher
import com.ryulth.offthework.api.model.User
import com.ryulth.offthework.api.model.request.EmailLoginRequest
import com.ryulth.offthework.api.model.request.EmailRegisterRequest
import com.ryulth.offthework.api.model.response.ErrorResponse
import com.ryulth.offthework.api.service.EmailUserService
import com.ryulth.offthework.api.service.UserService
import com.ryulth.offthework.api.util.EmailInvalidException
import com.ryulth.offthework.api.util.UserNotFoundException
import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
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
    companion object: KLogging() {
        val httpHeaders = HttpHeaders()
    }

    @PostMapping("/login")
    fun login(@RequestBody emailLoginRequest: EmailLoginRequest): ResponseEntity<Any> {
        logger.info{ "login payload : $emailLoginRequest" }
        return try {
            val user: User = emailUserService.loginByEmail(emailLoginRequest)
            ResponseEntity(tokenPublisher.publishToken(user.id!!,user.email), httpHeaders, HttpStatus.OK)
        } catch (e: Exception) {
            return when(e) {
                is UserNotFoundException, is BadCredentialsException -> {
                    ResponseEntity(ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                        e.toString()), httpHeaders, HttpStatus.UNAUTHORIZED)
                }
                else -> {
                    ResponseEntity(ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        e.toString()), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody emailRegisterRequest: EmailRegisterRequest): ResponseEntity<Any> {
        logger.info { "register payload : $emailRegisterRequest" }
        return try {
            val user: User = emailUserService.registerByEmail(emailRegisterRequest)
            ResponseEntity(tokenPublisher.publishToken(user.id!!,user.email), httpHeaders, HttpStatus.OK)
        } catch (e: Exception) {
            return when(e) {
                is EmailInvalidException, is BadCredentialsException -> {
                    ResponseEntity(ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                        e.toString()), httpHeaders, HttpStatus.UNAUTHORIZED)
                }
                else -> {
                    ResponseEntity(ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        e.toString()), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }
    }
}