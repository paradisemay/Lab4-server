package api

import beans.AuthService
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class LoginResource {

    @Inject
    private lateinit var authService: AuthService

    @POST
    fun loginUser(request: LoginRequest): Response {
        return authService.authenticateUser(request.login, request.password)
    }
}

data class LoginRequest(val login: String = "", val password: String = "")
