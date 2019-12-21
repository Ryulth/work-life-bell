package com.ryulth.offthework.api.auth.jwt

import com.ryulth.offthework.api.exception.UnauthorizedException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import java.io.UnsupportedEncodingException
import java.util.Date
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JwtProvider {
    companion object : KLogging()

    @Value("\${jwt.secret.base64.access}")
    val accessSecretKey: String? = null
    @Value("\${jwt.secret.base64.refresh}")
    val refreshSecretKey: String? = null
    @Value("\${jwt.expiration-seconds.access}")
    val accessTokenExpirationSeconds = 0
    @Value("\${jwt.expiration-seconds.refresh}")
    val refreshTokenExpirationSeconds = 0

    private val AUTHORITIES_ID = "userId"
    private val AUTHORITIES_Email = "userEmail"

    fun publishToken(userId: Long, userEmail: String, isAccessToken: Boolean): String {
        val now: Long = Date().time
        val validity: Date =
            if (isAccessToken) Date(now + accessTokenExpirationSeconds * 1000) else Date(now + refreshTokenExpirationSeconds * 1000)
        val key = if (isAccessToken) accessSecretKey else refreshSecretKey
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .claim(AUTHORITIES_ID, userId)
            .claim(AUTHORITIES_Email, userEmail)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, generateKey(key))
            .compact()
    }

    fun getUserIdAndEmailPair(authToken: String, isAccess: Boolean): Pair<Long, String> {
        val key = if (isAccess) accessSecretKey else refreshSecretKey
        var message = ""
        try {
            Jwts.parser().setSigningKey(generateKey(key)).parseClaimsJws(authToken)
            val claims = Jwts.parser().setSigningKey(generateKey(key))
                .parseClaimsJws(authToken).body
            return Pair(claims[AUTHORITIES_ID] as Long, claims[AUTHORITIES_Email].toString())
        } catch (e: SecurityException) {
            logger.error { "Invalid JWT signature. $e" }
            message = e.message!!
        } catch (e: MalformedJwtException) {
            logger.error { "Invalid JWT signature. $e" }
            message = e.message!!
        } catch (e: ExpiredJwtException) {
            logger.error { "Expired JWT token. $e" }
            message = e.message!!
        } catch (e: UnsupportedJwtException) {
            logger.error { "Unsupported JWT token $e" }
            message = e.message!!
        } catch (e: IllegalArgumentException) {
            logger.error { "JWT token compact of handler are invalid. $e" }
            message = e.message!!
        } catch (e: JwtException) {
            logger.error { "JWT token are invalid. $e" }
            message = e.message!!
        }
        throw UnauthorizedException(message)
    }

    private fun generateKey(secretKey: String?): ByteArray? {
        var key: ByteArray? = null
        try {
            key = secretKey!!.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return key
    }
}
