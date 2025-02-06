package api

import beans.HistoryService
import utils.TokenValidator
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/history")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class HistoryResource {

    @Inject
    private lateinit var historyService: HistoryService

    @Inject
    private lateinit var tokenValidator: TokenValidator

    @GET
    fun getHistory(@HeaderParam(HttpHeaders.AUTHORIZATION) authHeader: String?): Response {
        val login = tokenValidator.getLoginFromHeader(authHeader) ?: return Response.status(Response.Status.UNAUTHORIZED)
            .entity(mapOf("error" to "Неверный или истекший токен"))
            .build()

        val history = historyService.getLast10Results(login)
        return Response.ok(history).build()
    }

    @DELETE
    fun deleteHistory(@HeaderParam(HttpHeaders.AUTHORIZATION) authHeader: String?): Response {
        val login = tokenValidator.getLoginFromHeader(authHeader) ?: return Response.status(Response.Status.UNAUTHORIZED)
            .entity(mapOf("error" to "Неверный или истекший токен"))
            .build()

        val deletedCount = historyService.deleteResults(login)
        return Response.ok(mapOf("message" to "Удалено записей: $deletedCount")).build()
    }
}
