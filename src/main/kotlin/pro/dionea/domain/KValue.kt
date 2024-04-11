package pro.dionea.domain

import jakarta.persistence.*

@Entity(name = "dionea_key_value")
class KValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    var value: String = ""

    @ManyToOne
    @JoinColumn(name = "key_id")
    val key: Key = Key()
}