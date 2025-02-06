package beans

import entities.UserEntity
import utils.JwtUtils
import utils.PasswordUtils
import java.security.SecureRandom
import java.time.Instant
import java.util.*
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.ws.rs.core.Response

@Stateless
class RegisterService {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun registerUser(login: String, password: String): Response {
        val validationError = validateCredentials(login, password)
        if (validationError != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to validationError))
                .build()
        }

        if (isLoginTaken(login)) {
            return Response.status(Response.Status.CONFLICT)
                .entity(mapOf("error" to "Логин уже занят"))
                .build()
        }

        val salt = generateSalt()
        val passwordHash = PasswordUtils.hashPassword(password, salt)
        val token = JwtUtils.generateToken(login) // Теперь используем JWT

        val user = UserEntity(
            login = login,
            passwordHash = passwordHash,
            salt = salt,
            token = token,
            expirationTime = Date(System.currentTimeMillis() + JwtUtils.EXPIRATION_TIME)
        )
        entityManager.persist(user)

        return Response.ok(mapOf("message" to "Регистрация успешна", "token" to token)).build()
    }

    private fun validateCredentials(login: String, password: String): String? {
        val loginRegex = "^[a-zA-Z0-9_-]{5,}$".toRegex()
        if (login.length < 5) return "Логин должен содержать минимум 5 символов"
        if (!login.matches(loginRegex)) return "Логин должен содержать только латинские буквы, цифры, _ или -"

        if (password.length < 8) return "Пароль должен содержать минимум 8 символов"

        val allowedPasswordRegex = "^[a-zA-Z0-9@#$%^&+=!]+$".toRegex()
        if (!password.matches(allowedPasswordRegex))
            return "Пароль содержит недопустимые символы"

        if (!password.any { it.isUpperCase() })
            return "Пароль должен содержать хотя бы 1 заглавную букву"
        if (!password.any { it.isLowerCase() })
            return "Пароль должен содержать хотя бы 1 строчную букву"
        if (!password.any { it.isDigit() })
            return "Пароль должен содержать хотя бы 1 цифру"
        val specialChars = "@#$%^&+=!"
        if (!password.any { specialChars.contains(it) })
            return "Пароль должен содержать хотя бы 1 спецсимвол (${specialChars})"

        return null
    }


    private fun isLoginTaken(login: String): Boolean {
        return entityManager.createQuery(
            "SELECT COUNT(u) FROM UserEntity u WHERE u.login = :login", java.lang.Long::class.java
        ).setParameter("login", login).singleResult > 0
    }

    private fun generateSalt(): String {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }
}
