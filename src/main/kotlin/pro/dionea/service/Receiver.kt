package pro.dionea.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.ChatPermissions
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import pro.dionea.domain.*
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.net.URL
import java.sql.Timestamp
import javax.imageio.ImageIO

@Service
class Receiver(
    @Value("\${tg.name}") val name: String,
    @Value("\${tg.token}") val token: String,
    val spamAnalysis: SpamAnalysis,
    val spamService: SpamService,
    val voteService: VoteService,
    val userService: UserService,
    val encoding: PasswordEncoder,
    val contactService: ContactService,
    val chatService: ChatService,
    val detectImageService: DetectImageService,
    val textExtractionService: TextExtractionService
) : TelegramLongPollingBot() {

    private val log = LoggerFactory.getLogger(Receiver::class.java)

    override fun onUpdateReceived(update: Update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update)
            return
        }

        if (!update.hasMessage()) {
            return
        }

        val message = update.message
        if (message.newChatMembers.isNotEmpty()) {
            handleNewChatMembers(message)
            return
        }
        when {
            message.isMessageWithImage() -> handleRacyImageRequest(message)
            message.text == null -> return
            message.chat.type == "private" && message.text.startsWith("/") -> handlePrivateCommand(message)
            message.text.contains(name) && message.isReply -> handleSpamVoteRequest(message)
            else -> handlePotentialSpam(message, findChat(message))
        }
    }

    private fun handleRacyImageRequest(message: Message) {
        val userContact = contactService.findIfNotCreate(message.from)
        for (photo in message.photo) {
            val img = getBufferedImageFromTelegramPhoto(photo.fileId)
            val category = detectImageService.detect(img)
            val textImg = textExtractionService.extract(img)
            val resultSpam = spamAnalysis.isSpam(textImg)
            if (category == ImageCategory.PORN
                || category == ImageCategory.SEXY
                || resultSpam.spam) {
                val spam = Spam().apply {
                    text = if (resultSpam.spam) { resultSpam.text } else { "Изображение содержит $category" }
                    time = Timestamp(System.currentTimeMillis())
                    contact = userContact
                    chat = findChat(message)
                }
                spamService.add(spam)
                val send = SendMessage(
                    message.chatId.toString(),
                    """
                            Сообщение включает оскорбительное изображение. 
                            Оно будет удалено через 10 секунд.
                         """.trimIndent()
                )
                send.replyToMessageId = message.messageId
                val infoMsg = execute(send)
                GlobalScope.launch {
                    delay(10000)
                    execute(DeleteMessage(message.chatId.toString(), message.messageId))
                    execute(DeleteMessage(message.chatId.toString(), infoMsg.messageId))
                }
                break
            }
        }
    }

    private fun getBufferedImageFromTelegramPhoto(flId: String): BufferedImage {
        val file = execute(
            GetFile().apply {
                fileId = flId
            }
        )
        URL("https://api.telegram.org/file/bot$token/${file.filePath}")
            .openStream().use { io ->
                return ImageIO.read(
                    ByteArrayInputStream(io.readAllBytes())
                )
            }
    }

    private fun handleNewChatMembers(message: Message) {
        val newMembers = message.newChatMembers
        for (member in newMembers) {
            val contact = contactService.findIfNotCreate(member)
            if (member.userName == null || member.userName.isEmpty()) {
                contactService.save(
                    contact.apply {
                        restrict = true
                    }
                )
                execute(RestrictChatMember().apply {
                    chatId = message.chatId.toString()
                    userId = member.id
                    permissions = ChatPermissions().apply {
                        canSendMessages = false
                    }
                })
                val msgInfo =  execute(SendMessage(
                    message.chatId.toString(),
                    "Здравствуйте, ${member.firstName}.\n" +
                            "В вашем аккаунте скрыты контактные данные. \n" +
                            "Обычно такие аккаунты рассылают спам сообщения, " +
                            "поэтому мы ограничили вам доступ. \n" +
                            "Сообщение будет удалено через 10 секунд."
                ))
                GlobalScope.launch {
                    delay(10000)
                    execute(DeleteMessage(message.chatId.toString(), msgInfo.messageId))
                }
            }
        }
    }

    private fun handleCallbackQuery(update: Update) {
        val spamMessage = update.callbackQuery.message.replyToMessage ?: return
        val chtId = spamMessage.chat.id
        val replyMessage = update.callbackQuery.message
        val voteByContact = if (update.callbackQuery.data == "yes") Vote.YES else Vote.NO
        voteService.save(Vote().apply {
            chatId = chtId
            messageId = spamMessage.messageId.toLong()
            userId = update.callbackQuery.from.id
            vote = voteByContact
        })
        val votes = voteService.findByMessageId(spamMessage.messageId.toLong())
        val votesYes = votes.count { it.vote == Vote.YES }
        val votesNo = votes.size - votesYes
        val updateVote = EditMessageReplyMarkup().apply {
            chatId = chtId.toString()
            messageId = replyMessage.messageId
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
            deleteByVoteMessage(spamMessage, replyMessage)
        }
        if (votesNo >= 3) {
            execute(DeleteMessage(chtId.toString(), replyMessage.messageId))
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

    private fun deleteByVoteMessage(spamMessage: Message, replyMessage: Message) {
        val chtId = spamMessage.chatId.toString()
        val spammer = contactService.findIfNotCreate(spamMessage.from)
        contactService.increaseCountOfMessages(spammer, true)
        val spam = Spam().apply {
            text = if (spamMessage.isMessageWithImage()) "Содержит фото" else spamMessage.text
            time = Timestamp(System.currentTimeMillis())
            contact = spammer
            chat = findChat(spamMessage)
        }
        spamService.add(spam)
        execute(DeleteMessage(
            chtId,
            replyMessage.messageId
        ))
        execute(DeleteMessage(
            chtId,
            spamMessage.messageId
        ))
    }

    private fun Message.isMessageWithImage(): Boolean {
        return photo != null && photo.isNotEmpty()
    }

    override fun getBotToken(): String = token

    override fun getBotUsername(): String = name
}