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

    @Column(name = "chat_id")
    var chatId: Long? = null

    @Column(name = "chat_name")
    var chatName: String = ""

    @ManyToOne
    @JoinColumn(name = "contact_id")
    var contact: Contact = Contact()
}