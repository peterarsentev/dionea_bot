package pro.dionea.domain

import jakarta.persistence.*
import java.sql.Timestamp

@Entity(name = "dionea_spam")
class Spam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    var text: String = ""

    var time: Timestamp? = null

    @ManyToOne
    @JoinColumn(name = "contact_id")
    var contact: Contact = Contact()

    @ManyToOne
    @JoinColumn(name = "chat_id")
    var chat: Chat = Chat()
}