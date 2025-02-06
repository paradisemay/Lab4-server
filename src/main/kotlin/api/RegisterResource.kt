package api

import beans.RegisterService
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class RegisterResource {

    @Inject
    private lateinit var registerService: RegisterService

    @POST
    fun registerUser(request: RegisterRequest): Response {
        return registerService.registerUser(request.login, request.password)
    }
}

data class RegisterRequest(val login: String = "", val password: String = "")
