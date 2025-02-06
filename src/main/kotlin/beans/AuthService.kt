package beans

import entities.UserEntity
import utils.JwtUtils
import utils.PasswordUtils
import java.util.Date
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.ws.rs.core.Response

@Stateless
class AuthService {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun authenticateUser(login: String, password: String): Response {
        val user = entityManager.createQuery(
            "SELECT u FROM UserEntity u WHERE u.login = :login", UserEntity::class.java
        ).setParameter("login", login)
            .resultList
            .firstOrNull() ?: return Response.status(Response.Status.UNAUTHORIZED)
            .entity(mapOf("error" to "Неверный логин или пароль"))
            .build()

        val inputPasswordHash = PasswordUtils.hashPassword(password, user.salt)
        if (inputPasswordHash != user.passwordHash) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(mapOf("error" to "Неверный логин или пароль"))
                .build()
        }

        // Генерируем новый JWT
        val jwtToken = JwtUtils.generateToken(login)
        val tokenExpiration = Date(System.currentTimeMillis() + JwtUtils.EXPIRATION_TIME)

        // Обновляем токен и его срок действия в БД
        user.token = jwtToken
        user.tokenExpiration = tokenExpiration
        entityManager.merge(user)

        return Response.ok(mapOf("message" to "Аутентификация успешна", "token" to jwtToken)).build()
    }
}

