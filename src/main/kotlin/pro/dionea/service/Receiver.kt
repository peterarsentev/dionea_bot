package pro.dionea.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import pro.dionea.domain.*
import java.sql.Timestamp

@Service
class Receiver(
    @Value("\${tg.name}") val name: String,
    @Value("\${tg.token}") val token: String,
    val analysis: SpamAnalysis,
    val spamService: SpamService,
    val voteService: VoteService,
    val userService: UserService,
    val encoding: PasswordEncoder,
    val contactService: ContactService,
    val chatService: ChatService
) : TelegramLongPollingBot() {

    override fun onUpdateReceived(update: Update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update)
            return
        }

        if (!update.hasMessage() || update.message.text == null) {
            return
        }

        val message = update.message
        val chatStatistics = findChat(message)

        when {
            message.chat.type == "private" && message.text.startsWith("/") -> handlePrivateCommand(message)
            message.text.contains(name) && message.isReply -> handleSpamVoteRequest(message)
            else -> handlePotentialSpam(message, chatStatistics)
        }
    }

    private fun handleCallbackQuery(update: Update) {
        val replyMessage = update.callbackQuery.message.replyToMessage ?: return
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
            text = "Да: $votesYes"
            callbackData = "yes"
        })
        rowInline.add(InlineKeyboardButton().apply {
            text = "Нет: $votesNo"
            callbackData = "no"
        })
        rowsInline.add(rowInline)
        markupInline.keyboard = rowsInline
        updateVote.replyMarkup = markupInline
        execute(updateVote)
        if (votesYes >= 3) {
            deleteByVoteMessage(replyMessage, targetMessage)
        }
    }

    private fun handlePrivateCommand(message: Message) {
        if (message.text.startsWith("/")) {
            if ("/start" == message.text) {
                execute(
                    SendMessage(
                        message.chatId.toString(),
                        "$name гибкий помощник для телеграмм групп:\n" +
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
                    SendMessage(
                        message.chatId.toString(),
                        "Доступ к сайту https://job4j.ru/dionea\n" +
                                "Логин: ${user.username}\n" +
                                "Пароль: $pwd"
                    )
                )
            }
        }
    }

    private fun handleSpamVoteRequest(message: Message) {
        val voteMessage = SendMessage().apply {
            chatId = message.chatId.toString()
            text = "Это спам? Проголосуйте за 3 раза."
        }
        val markupInline = InlineKeyboardMarkup()
        val rowsInline: MutableList<List<InlineKeyboardButton>> = ArrayList()
        val rowInline: MutableList<InlineKeyboardButton> = ArrayList()
        rowInline.add(InlineKeyboardButton().apply {
            text = "Да: 0"
            callbackData = "yes"
        })
        rowInline.add(InlineKeyboardButton().apply {
            text = "Нет: 0"
            callbackData = "no"
        })
        rowsInline.add(rowInline)
        markupInline.keyboard = rowsInline
        voteMessage.replyMarkup = markupInline
        voteMessage.replyToMessageId = message.replyToMessage.messageId
        execute(voteMessage)
    }

    private fun handlePotentialSpam(message: Message, chatStatistics: Chat) {
        val userContact = contactService.findIfNotCreate(message)
        val spamReason = analysis.isSpam(message.text)
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
            val infoMsg = execute(send)
            GlobalScope.launch {
                delay(10000)
                execute(DeleteMessage(message.chatId.toString(), message.messageId))
                execute(DeleteMessage(message.chatId.toString(), infoMsg.messageId))
            }
        }
    }

    private fun findChat(message: Message) : Chat
            = chatService.findByChatId(message.chatId)
        ?: chatService.add(
            Chat().apply {
                chatId = message.chatId
                username = message.chat.userName
                title = message.chat.title
            }
        )

    private fun deleteByVoteMessage(replyMessage: Message, targetMessage: Message) {
        val name = replyMessage.from.userName ?: "unknown"
        val spammer = contactService.findByName(name)
            ?: contactService.add(
                Contact().apply {
                    tgUserId = replyMessage.from.id
                    username = name
                    firstName = replyMessage.from.firstName
                    lastName = replyMessage.from.lastName  ?: ""
                }
            )
        val spam = Spam().apply {
            text = replyMessage.text
            time = Timestamp(System.currentTimeMillis())
            contact = spammer
            chat = findChat(replyMessage)
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

    override fun getBotToken(): String = token

    override fun getBotUsername(): String = name
}