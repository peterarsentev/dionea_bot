package pro.dionea.service.actions

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Update
import pro.dionea.domain.Spam
import pro.dionea.service.ChatService
import pro.dionea.service.ContactService
import pro.dionea.service.SpamAnalysis
import pro.dionea.service.SpamService
import java.sql.Timestamp

class TextMessageAction(val contactService: ContactService,
                        val spamService: SpamService,
                        val spamAnalysis: SpamAnalysis,
                        val chatService: ChatService): UpdateAction {
    override fun check(update: Update): Boolean
            = update.message.text.isNullOrEmpty()

    override fun execute(update: Update, remoteChat: RemoteChat) {
        val message = update.message
        val chatStatistics = chatService.findOrCreate(message)
        val userContact = contactService.findIfNotCreate(message.from)
        val spamReason = spamAnalysis.isSpam(message.text)
        contactService.increaseCountOfMessages(userContact, spamReason.spam)
        if (userContact.isHammer()) {
            return
        }
        if (spamReason.spam) {
            val spam = Spam().apply {
                text = message.text
                time = Timestamp(System.currentTimeMillis())
                contact = userContact
                chat = chatStatistics
            }
            spamService.add(spam)
            val send = SendMessage(
                message.chatId.toString(), "Обнаружен спам:\n" +
                        "${spamReason.text}\n" +
                        "Сообщение будет удалено через 10 секунд."
            )
            send.replyToMessageId = message.messageId
            val infoMsg = remoteChat.execute(send)
            GlobalScope.launch {
                delay(10000)
                remoteChat.execute(DeleteMessage(message.chatId.toString(), message.messageId))
                remoteChat.execute(DeleteMessage(message.chatId.toString(), infoMsg.messageId))
            }
        }
    }
}