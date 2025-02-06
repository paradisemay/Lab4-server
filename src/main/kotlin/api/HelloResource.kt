package api

import beans.HelloService
import utils.TokenValidator
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class HelloResource {

    @Inject
    private lateinit var helloService: HelloService

    @Inject
    private lateinit var tokenValidator: TokenValidator

    @GET
    fun sayHello(@HeaderParam(HttpHeaders.AUTHORIZATION) authHeader: String?): Response {
        val login = tokenValidator.getLoginFromHeader(authHeader) ?: return Response.status(Response.Status.UNAUTHORIZED)
            .entity(mapOf("error" to "Неверный или истекший токен"))
            .build()

        return Response.ok(mapOf("message" to helloService.getHelloMessage(login))).build()
    }
}
