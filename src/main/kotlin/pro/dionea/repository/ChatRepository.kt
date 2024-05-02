package pro.dionea.repository

import org.springframework.data.repository.CrudRepository
import pro.dionea.domain.Chat

interface ChatRepository : CrudRepository<Chat, Int>{
    fun findByChatId(chatId: Long): Chat?
}