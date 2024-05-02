package pro.dionea.service

import org.springframework.stereotype.Service
import pro.dionea.domain.Chat
import pro.dionea.repository.ChatRepository

@Service
class ChatService(val chatRepository: ChatRepository) {

    fun add(chat: Chat) : Chat = chatRepository.save(chat)

    fun findByChatId(chatId: Long): Chat?
            = chatRepository.findByChatId(chatId)

    fun getAll(): List<Chat>
            = chatRepository.findAll().toList()
}