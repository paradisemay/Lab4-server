package utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.Claims
import java.util.*

object JwtUtils {
    private const val SECRET_KEY = "super_secret_key"
    const val EXPIRATION_TIME = 30 * 60 * 1000 // 30 минут

    fun generateToken(login: String): String {
        val now = Date()
        val expiryDate = Date(now.time + EXPIRATION_TIME)

        return Jwts.builder()
            .setSubject(login)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact()
    }

    fun validateToken(token: String?): String? {
        return try {
            val claims: Claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .body

            // Выполняем ручную проверку expiration
            if (claims.expiration.before(Date())) {
                null  // Токен просрочен
            } else {
                claims.subject  // Возвращаем логин пользователя, если токен действителен
            }
        } catch (e: Exception) {
            null  // В случае ошибки возвращаем null
        }
    }
}
