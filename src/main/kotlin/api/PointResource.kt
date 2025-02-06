package api

import beans.PointService
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/point")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PointResource {

    @Inject
    private lateinit var pointService: PointService

    @POST
    fun checkPoint(
        @HeaderParam(HttpHeaders.AUTHORIZATION) authHeader: String?,
        request: PointRequest
    ): Response {
        if (!request.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Некорректные значения x, y или r"))
                .build()
        }

        val message = pointService.getPointResult(authHeader, request)
            ?: return Response.status(Response.Status.UNAUTHORIZED)
                .entity(mapOf("error" to "Неверный или истекший токен"))
                .build()

        return Response.ok(mapOf("message" to message)).build()
    }
}

data class PointRequest @JsonCreator constructor(
    @JsonProperty("x") val x: Double,
    @JsonProperty("y") val y: Double,
    @JsonProperty("r") val r: Double
) {
    fun isValid(): Boolean = x in -5.0..3.0 && y in -5.0..3.0 && r in 0.0..5.0
}