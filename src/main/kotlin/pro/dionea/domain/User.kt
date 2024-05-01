package pro.dionea.domain

import jakarta.persistence.*

@Entity(name = "dionea_user")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    var username: String = ""

    var password: String = ""

    var enabled: Boolean = false

    @ManyToOne
    @JoinColumn(name = "role_id")
    var role: Role = Role()
}