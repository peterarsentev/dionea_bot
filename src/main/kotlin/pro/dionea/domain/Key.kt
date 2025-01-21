package pro.dionea.domain

import jakarta.persistence.*

@Entity(name = "dionea_key")
class Key {
    constructor()

    constructor(id: Int, filter: Filter) {
        this.id = id
        this.filter = filter
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    var name: String = ""

    @ManyToOne
    @JoinColumn(name = "filter_id")
    var filter: Filter = Filter()
}
