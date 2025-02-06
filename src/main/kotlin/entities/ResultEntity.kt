package entities

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "result")
class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity? = null

    @Column(nullable = false)
    var timestamp: Date? = null

    @Column(nullable = false)
    var x: Double = 0.0

    @Column(nullable = false)
    var y: Double = 0.0

    @Column(nullable = false)
    var r: Double = 0.0

    @Column(nullable = false)
    var result: String = ""

    constructor()

    constructor(user: UserEntity, timestamp: Date, x: Double, y: Double, r: Double, result: String) {
        this.user = user
        this.timestamp = timestamp
        this.x = x
        this.y = y
        this.r = r
        this.result = result
    }
}
