package pro.dionea.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "dionea_vote")
class Vote {

    constructor()

    constructor(chatId: Long, messageId: Long, userId: Long, vote: Int) {
        this.chatId = chatId
        this.messageId = messageId
        this.userId = userId
        this.vote = vote
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    var chatId: Long? = null

    var messageId: Long? = null

    var userId: Long? = null

    var vote: Int = 1

    companion object {
        const val YES: Int = 1
        const val NO: Int = 0
    }
}