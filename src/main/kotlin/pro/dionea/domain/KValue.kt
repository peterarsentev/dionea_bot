package pro.dionea.domain

import jakarta.persistence.*

@Entity(name = "dionea_key_value")
class KValue {
    constructor()

    constructor(id: Int, key: Key, value: String) {
        this.id = id
        this.key = key
        this.value = value
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    var value: String = ""

    @ManyToOne
    @JoinColumn(name = "key_id")
    var key: Key = Key()
}