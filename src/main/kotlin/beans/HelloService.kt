package beans

import utils.JwtUtils
import javax.ejb.Stateless

@Stateless
class HelloService {

    fun getHelloMessage(login: String?): String? {
        return "Hello, $login"
    }
}
