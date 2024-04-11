package pro.dionea.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Update

class Receiver(val name: String, val token: String, val analysis: SpamAnalysis) : TelegramLongPollingBot() {

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage()) {
            val message = update.message
            if (analysis.isSpam(message.text)) {
                val send = SendMessage(
                    message.chatId.toString(), "Обнаружен спам. " +
                            "Сообщение будет удалено через 10 секунд."
                )
                send.replyToMessageId = message.messageId
                execute(send)
                GlobalScope.launch {
                    delay(10000)
                    execute(DeleteMessage(message.chatId.toString(), message.messageId))
                }
            }
        }
    }

    override fun getBotToken(): String = token

    override fun getBotUsername(): String = name
}