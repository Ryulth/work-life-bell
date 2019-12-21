package com.ryulth.offthework.api.auth

import com.ryulth.offthework.api.auth.jwt.JwtProvider
import com.ryulth.offthework.api.exception.UnauthorizedException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import mu.KLogging
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class TokenInterceptor(
    val jwtProvider: JwtProvider
) : HandlerInterceptor {

    companion object : KLogging()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any?): Boolean {
        if (request.method == "OPTIONS") {
            return true
        }
        try {
            val token = request.getHeader("Authorization").split("Bearer ").toTypedArray()[1]
            val accessEmail: String = jwtProvider.getUserIdAndEmailPair(token, true).second
            request.session.setAttribute("accessEmail", accessEmail)
            return true
        } catch (e: Exception) {
            when (e) {
                is ArrayIndexOutOfBoundsException -> {
                    response.sendError(
                        401, "{ \"error\" : \"Authorization Format Invalid," +
                            " Must Be {TokenType} {AccessToken}\" }"
                    )
                }
                is NullPointerException -> {
                    response.sendError(401, "{ \"error\" : \"Need Access Token in Authorization Header\" }")
                }
                is UnauthorizedException -> {
                    response.sendError(401, "{ \"error\" : \"$e\" }")
                }
                is IllegalStateException -> {
                    response.sendError(401, "{ \"error\" : \"$e\" }")
                }
                else -> {
                    response.sendError(500, "{ \"error\" : \"server error $e\" }")
                }
            }
            logger.error { "Interceptor error $e" }
            return false
        }
    }
}
