package com.ryulth.worklifebell.api.util

import com.ryulth.worklifebell.api.exception.AlreadyOffWorkException
import com.ryulth.worklifebell.api.exception.AlreadyOnWorkException
import com.ryulth.worklifebell.api.exception.AttendanceNotFoundException
import com.ryulth.worklifebell.api.exception.DateFormatException
import com.ryulth.worklifebell.api.exception.EmailInvalidException
import com.ryulth.worklifebell.api.exception.UserNotFoundException
import com.ryulth.worklifebell.api.model.response.ErrorResponse
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionHandler {

    companion object : KLogging()

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AttendanceNotFoundException::class)
    fun handleAttendanceNotFoundException(e: AttendanceNotFoundException): ErrorResponse {
        logger.error { "AttendanceNotFoundException $e" }
        e.printStackTrace()
        val httpStatus = HttpStatus.BAD_REQUEST
        return ErrorResponse(httpStatus.reasonPhrase, httpStatus.value(), e.toString())
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateFormatException::class)
    fun handleDateFormatException(e: DateFormatException): ErrorResponse {
        logger.error { "DateFormatException $e" }
        e.printStackTrace()
        val httpStatus = HttpStatus.BAD_REQUEST
        return ErrorResponse(httpStatus.reasonPhrase, httpStatus.value(), e.toString())
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException::class)
    fun handleEmailInvalidException(e: UserNotFoundException): ErrorResponse {
        logger.error { "UserNotFoundException $e" }
        e.printStackTrace()
        val httpStatus = HttpStatus.BAD_REQUEST
        return ErrorResponse(httpStatus.reasonPhrase, httpStatus.value(), e.toString())
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailInvalidException::class)
    fun handleEmailInvalidException(e: EmailInvalidException): ErrorResponse {
        logger.error { "EmailInvalidException $e" }
        e.printStackTrace()
        val httpStatus = HttpStatus.BAD_REQUEST
        return ErrorResponse(httpStatus.reasonPhrase, httpStatus.value(), e.toString())
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyOnWorkException::class)
    fun handleAlreadyAttendanceException(e: AlreadyOnWorkException): ErrorResponse {
        logger.error { "AlreadyAttendanceException $e" }
        e.printStackTrace()
        val httpStatus = HttpStatus.BAD_REQUEST
        return ErrorResponse(httpStatus.reasonPhrase, httpStatus.value(), e.toString())
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyOffWorkException::class)
    fun handleAlreadyOffWorkException(e: AlreadyOffWorkException): ErrorResponse {
        logger.error { "AlreadyOffWorkException $e" }
        e.printStackTrace()
        val httpStatus = HttpStatus.BAD_REQUEST
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
