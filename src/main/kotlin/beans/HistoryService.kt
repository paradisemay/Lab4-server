package beans

import entities.ResultEntity
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Stateless
class HistoryService {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun getLast10Results(login: String): List<Map<String, Any>> {
        val query = entityManager.createQuery(
            "SELECT r FROM ResultEntity r WHERE r.user.login = :login ORDER BY r.timestamp DESC",
            ResultEntity::class.java
        )
        query.setParameter("login", login)
        query.maxResults = 10
        val results = query.resultList
        return results.map { r ->
            mapOf<String, Any>(
                "time" to (r.timestamp as Any),
                "x" to (r.x as Any),
                "y" to (r.y as Any),
                "r" to (r.r as Any),
                "result" to (r.result as Any)
            )
        }
    }

    fun deleteResults(login: String): Int {
        val deleteQuery = entityManager.createQuery(
            "DELETE FROM ResultEntity r WHERE r.user.id IN (SELECT u.id FROM UserEntity u WHERE u.login = :login)"
        )
        deleteQuery.setParameter("login", login)
        return deleteQuery.executeUpdate()
    }
}
