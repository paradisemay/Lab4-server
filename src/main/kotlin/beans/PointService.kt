package beans

import api.PointRequest
import entities.ResultEntity
import entities.UserEntity
import utils.JwtUtils
import java.util.*
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Stateless
class PointService {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun getPointResult(token: String?, request: PointRequest): String? {
        val jwt = token?.removePrefix("Bearer ") ?: return null
        val login = JwtUtils.validateToken(jwt) ?: return null

        val (x, y, r) = request
        val hit = checkCircle(x, y, r) || checkTriangle(x, y, r) || checkRect(x, y, r)
        val result = if (hit) "hit" else "miss"

        // Получаем объект пользователя по логину
        val user: UserEntity = entityManager.createQuery(
            "SELECT u FROM UserEntity u WHERE u.login = :login", UserEntity::class.java
        ).setParameter("login", login)
            .resultList.firstOrNull() ?: return result

        // Создаем новую сущность результата и сохраняем её в БД
        val resultEntity = ResultEntity(
            user = user,
            timestamp = Date(),
            x = x,
            y = y,
            r = r,
            result = result
        )
        entityManager.persist(resultEntity)

        return result
    }

    private fun checkCircle(x: Double, y: Double, r: Double): Boolean {
        return x <= 0 && y <= 0 && (x * x + y * y <= r * r)
    }

    private fun checkTriangle(x: Double, y: Double, r: Double): Boolean {
        return x <= 0 && y >= 0 && (x + r >= 2 * y)
    }

    private fun checkRect(x: Double, y: Double, r: Double): Boolean {
        return x >= 0 && y >= 0 && x <= 2 * r && y <= r
    }
}
