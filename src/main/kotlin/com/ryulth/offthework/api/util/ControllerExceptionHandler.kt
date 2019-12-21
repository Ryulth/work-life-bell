package com.ryulth.offthework.api.util

import com.ryulth.offthework.api.exception.AlreadyAttendanceException
import com.ryulth.offthework.api.exception.AttendanceNotFoundException
import com.ryulth.offthework.api.exception.EmailInvalidException
import com.ryulth.offthework.api.exception.UserNotFoundException
import com.ryulth.offthework.api.model.response.ErrorResponse
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionHandler {

    companion object : KLogging()

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(AttendanceNotFoundException::class)
    fun handleAttendanceNotFoundException(e: AttendanceNotFoundException): ErrorResponse {
        logger.error { "AttendanceNotFoundException ${e.stackTrace}" }
        e.printStackTrace()
        val httpStatus = HttpStatus.NO_CONTENT
        return ErrorResponse(httpStatus.reasonPhrase, httpStatus.value(), e.toString())
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserNotFoundException::class)
    fun handleEmailInvalidException(e: UserNotFoundException): ErrorResponse {
        logger.error { "UserNotFoundException $e" }
        e.printStackTrace()
        val httpStatus = HttpStatus.UNAUTHORIZED
        return ErrorResponse(httpStatus.reasonPhrase, httpStatus.value(), e.toString())
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(EmailInvalidException::class)
    fun handleEmailInvalidException(e: EmailInvalidException): ErrorResponse {
        logger.error { "EmailInvalidException $e" }
        e.printStackTrace()
        val httpStatus = HttpStatus.UNAUTHORIZED
        return ErrorResponse(httpStatus.reasonPhrase, httpStatus.value(), e.toString())
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyAttendanceException::class)
    fun handleAlreadyAttendanceException(e: AlreadyAttendanceException): ErrorResponse {
        logger.error { "AlreadyAttendanceException $e" }
        e.printStackTrace()
        val httpStatus = HttpStatus.CONFLICT
        return ErrorResponse(httpStatus.reasonPhrase, httpStatus.value(), e.toString())
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ErrorResponse {
        logger.error { "Exception $e" }
        e.printStackTrace()
        val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        return ErrorResponse(httpStatus.reasonPhrase, httpStatus.value(), e.toString())
    }
}
