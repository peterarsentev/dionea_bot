package pro.dionea.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.security.crypto.password.PasswordEncoder
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import pro.dionea.domain.Spam
import pro.dionea.domain.User
import pro.dionea.domain.Vote
import java.sql.Timestamp


class Receiver(val name: String, val token: String,
               val analysis: SpamAnalysis,
               val spamService: SpamService,
               val voteService: VoteService,
               val userService: UserService,
               val encoding: PasswordEncoder,
               val tgBotName: String)
    : TelegramLongPollingBot() {

    private fun deleteByVoteMessage(replyMessage: Message, targetMessage: Message) {
        val spam = Spam().apply {
            text = replyMessage.text
            chatId = replyMessage.chat.id
            chatName = replyMessage.chat.title
            time = Timestamp(System.currentTimeMillis())
        }
        spamService.add(spam)
        execute(DeleteMessage(
            replyMessage.chatId.toString(),
            targetMessage.messageId
        ))
        execute(DeleteMessage(
            replyMessage.chatId.toString(),
            replyMessage.messageId
        ))
    }

    override fun onUpdateReceived(update: Update) {
        if (update.hasCallbackQuery()) {
            val replyMessage = update.callbackQuery.message.replyToMessage
            val targetMessage = update.callbackQuery.message
            val voteByContact = if (update.callbackQuery.data == "yes") Vote.YES else Vote.NO
            voteService.save(Vote().apply {
                chatId = replyMessage.chat.id
                messageId = replyMessage.messageId.toLong()
                userId = update.callbackQuery.from.id
                vote = voteByContact
            })
            val votes = voteService.findByMessageId(replyMessage.messageId.toLong())
            val votesYes = votes.count { it.vote == Vote.YES }
            val votesNo = votes.size - votesYes
            val updateVote = EditMessageReplyMarkup().apply {
                chatId = replyMessage.chat.id.toString()
                messageId = targetMessage.messageId
            }
            val markupInline = InlineKeyboardMarkup()
            val rowsInline: MutableList<List<InlineKeyboardButton>> = ArrayList()
            val rowInline: MutableList<InlineKeyboardButton> = ArrayList()
            rowInline.add(InlineKeyboardButton().apply {
                text = "Yes: $votesYes"
                callbackData = "yes"
            })
            rowInline.add(InlineKeyboardButton().apply {
                text = "No: $votesNo"
                callbackData = "no"
            })
            rowsInline.add(rowInline)
            markupInline.keyboard = rowsInline
            updateVote.replyMarkup = markupInline
            execute(updateVote)
            if (votesYes >= 3) {
                deleteByVoteMessage(replyMessage, targetMessage)
            }
            return
        }
        if (!(update.hasMessage() && update.message.text != null)) {
            return
        }
        val message = update.message
        if (message.chat.type == "private") {
            if (message.text.startsWith("/")) {
                if ("/start" == message.text) {
                    execute(
                        SendMessage(message.chatId.toString(),
                              "$tgBotName гибкий помощник для телеграмм групп:\n" +
                                      "- анализирует и удаляет спам сообщения\n" +
                                      "- поддерживает режим голосования за удаление сообщения или бан участника\n" +
                                      "- позволяет настроить собственные стоп-фильтры через веб-интерфейс\n" +
                                      "- публикация сообщений по расписанию\n\n" +
                                      "Подробнее тут https://job4j.ru/dionea\n" +
                                      "/reg - для регистрации пользователя"
                        )
                    )
                }
                if ("/reg" == message.text) {
                    val pwd = KeyGen(8).generate()
                    val user = userService.add(
                        User().apply {
                            username = message.from.userName
                            password = encoding.encode(pwd)
                            enabled = true
                        }
                    )
                    execute(
                        SendMessage(message.chatId.toString(),
                            "Доступ к сайту https://job4j.ru/dionea\n" +
                            "Логин: ${user.username}\n" +
                            "Пароль: $pwd"
                        )
                    )
                }
            }
        }
        if (message.text.contains(tgBotName) && message.isReply) {
            val voteMessage = SendMessage().apply {
                chatId = message.chatId.toString()
                text = "Is it spam? Vote Yes 3 times."
            }
            val markupInline = InlineKeyboardMarkup()
            val rowsInline: MutableList<List<InlineKeyboardButton>> = ArrayList()
            val rowInline: MutableList<InlineKeyboardButton> = ArrayList()
            rowInline.add(InlineKeyboardButton().apply {
                text = "Yes: 0"
                callbackData = "yes"
            })
            rowInline.add(InlineKeyboardButton().apply {
                text = "No: 0"
                callbackData = "no"
            })
            rowsInline.add(rowInline)
            markupInline.keyboard = rowsInline
            voteMessage.replyMarkup = markupInline
            voteMessage.replyToMessageId = message.replyToMessage.messageId
            execute(voteMessage)
            return
        }
        val spamReason = analysis.isSpam(message.text)
        if (spamReason.spam) {
            val spam = Spam().apply {
                text = message.text
                chatId = message.chat.id
                chatName = message.chat.title
                time = Timestamp(System.currentTimeMillis())
            }
            spamService.add(spam)
            val send = SendMessage(
                message.chatId.toString(), "Detected Spam. Reason:\n" +
                        "${spamReason.text}\n" +
                        "Message will be deleted in 10 seconds."
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

    override fun getBotToken(): String = token

    override fun getBotUsername(): String = name
}