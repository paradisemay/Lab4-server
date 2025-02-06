package entities

import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, unique = true)
    lateinit var login: String

    @Column(nullable = false)
    lateinit var passwordHash: String

    @Column(nullable = false)
    lateinit var salt: String

    @Column(name = "token", length = 512)
    var token: String? = null

    @Column(name = "token_expiration")
    @Temporal(TemporalType.TIMESTAMP)
    var tokenExpiration: Date? = null

    constructor()

    constructor(login: String, passwordHash: String, salt: String, token: String?, expirationTime: Date?) {
        this.login = login
        this.passwordHash = passwordHash
        this.salt = salt
        this.token = token
        this.tokenExpiration = expirationTime
    }
}
