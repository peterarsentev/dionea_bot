package pro.dionea.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "dionea_contact")
class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(name = "tg_user_id")
    var tgUserId: Long = 0L

    var username: String = ""

    @Column(name = "first_name")
    var firstName: String = ""

    @Column(name = "last_name")
    var lastName: String = ""

    var ham: Int = 0

    var spam: Int = 0

    var restrict: Boolean = false

    fun isHammer(): Boolean = ham > spam
}
