package pro.dionea.service.actions

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

class MentionBotAction(val botName: String): UpdateAction {

    override fun check(update: Update): Boolean
            = update.message.text != null
            && update.message.text.contains(botName)
            && !update.message.isReply

    override fun execute(update: Update, remoteChat: RemoteChat) {
        val message = SendMessage(
            update.message.chatId.toString(),
            """
        Извините, бот не смог обнаружить спам-сообщение.

        Вы можете:
        1. **Процитировать** сообщение со спамом и **проголосовать за его удаление**.
        2. **Создать issue** с текстом спама на нашем GitHub:
           
           [https://github.com/peterarsentev/dionea_bot/issues](https://github.com/peterarsentev/dionea_bot/issues)

        Спасибо за вашу помощь! Мы продолжаем совершенствовать наш анализатор спама.
        """.trimIndent()
        )
        message.parseMode = "Markdown"

        // Send the message
        remoteChat.execute(message)
    }
}