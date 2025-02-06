package utils

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
open class TokenValidator {

    open fun validateToken(token: String): String? {
        return JwtUtils.validateToken(token)
    }

    // Новый метод для извлечения логина из заголовка
    open fun getLoginFromHeader(authHeader: String?): String? {
        return authHeader
            ?.removePrefix("Bearer ")
            ?.let { token -> validateToken(token) }
    }
}
