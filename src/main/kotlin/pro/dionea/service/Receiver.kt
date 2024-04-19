package pro.dionea.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Update
import pro.dionea.domain.Spam
import java.sql.Timestamp

class Receiver(val name: String, val token: String,
               val analysis: SpamAnalysis,
               val spamService: SpamService)
    : TelegramLongPollingBot() {


    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.text != null) {
            val message = update.message
            if (analysis.isSpam(message.text)) {
                val spam = Spam().apply {
                    text = message.text
                    chatId = message.chat.id
                    chatName = message.chat.title
                    time = Timestamp(System.currentTimeMillis())
                }
                spamService.add(spam)
                val send = SendMessage(
                    message.chatId.toString(), "Обнаружен спам. " +
                            "Сообщение будет удалено через 10 секунд."
                )
                send.replyToMessageId = message.messageId
                val infoMsg = execute(send)
                GlobalScope.launch {
                    delay(10000)
                    execute(DeleteMessage(message.chatId.toString(), message.messageId))
                    execute(DeleteMessage(message.chatId.toString(), infoMsg.messageId))
                }
            }
        }
    }

    override fun getBotToken(): String = token

    override fun getBotUsername(): String = name
}