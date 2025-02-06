package utils

import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordUtils {

    fun hashPassword(password: String, salt: String): String {
        val iterations = 65536
        val keyLength = 256
        val saltBytes = Base64.getDecoder().decode(salt)

        val spec = PBEKeySpec(password.toCharArray(), saltBytes, iterations, keyLength)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = factory.generateSecret(spec).encoded

        return Base64.getEncoder().encodeToString(hash)
    }
}