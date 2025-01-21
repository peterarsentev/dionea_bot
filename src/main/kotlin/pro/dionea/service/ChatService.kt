package pro.dionea.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import pro.dionea.domain.Chat
import pro.dionea.repository.ChatRepository

@Service
class ChatService(val chatRepository: ChatRepository) {

    fun add(chat: Chat) : Chat = chatRepository.save(chat)

    fun findByChatId(chatId: Long): Chat?
            = chatRepository.findByChatId(chatId)

    fun getAll(): List<Chat>
            = chatRepository.findAll().toCollection(ArrayList<Chat>())

    fun findOrCreate(message : Message) : Chat
            = findByChatId(message.chatId)
        ?: add(
            Chat().apply {
                chatId = message.chatId
                username = message.from.firstName
                title = message.chat.title ?: "untitled"
            }
        )
}