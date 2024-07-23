package pro.dionea.service.actions

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

class ReplyAction(val botName: String): UpdateAction {

    override fun check(update: Update): Boolean
     = update.message.text.contains(botName) && update.message.isReply

    override fun execute(update: Update, remoteChat: RemoteChat) {
        val message = update.message
        val voteMessage = SendMessage().apply {
            chatId = message.chatId.toString()
            text = "Это спам? Проголосуйте за 3 раза."
        }
        val markupInline = InlineKeyboardMarkup()
        val rowsInline: MutableList<List<InlineKeyboardButton>> = ArrayList()
        val rowInline: MutableList<InlineKeyboardButton> = ArrayList()
        rowInline.add(InlineKeyboardButton().apply {
            text = "Да: 0"
            callbackData = "yes ${message.messageId}"
        })
        rowInline.add(InlineKeyboardButton().apply {
            text = "Нет: 0"
            callbackData = "no ${message.messageId}"
        })
        rowsInline.add(rowInline)
        markupInline.keyboard = rowsInline
        voteMessage.replyMarkup = markupInline
        voteMessage.replyToMessageId = message.replyToMessage.messageId
        remoteChat.execute(voteMessage)
    }
}