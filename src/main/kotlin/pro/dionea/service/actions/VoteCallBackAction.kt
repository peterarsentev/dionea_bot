package pro.dionea.service.actions

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import pro.dionea.domain.Spam
import pro.dionea.domain.Vote
import pro.dionea.service.*
import java.sql.Timestamp

class VoteCallBackAction(
    val voteService: VoteService,
    val contactService: ContactService,
    val spamService: SpamService,
    val chatService: ChatService) : UpdateAction {

    override fun check(update: Update): Boolean
            = update.hasCallbackQuery()

    override fun execute(update: Update, remoteChat: RemoteChat) {
        val spamMessage = update.callbackQuery.message.replyToMessage ?: return
        val chtId = spamMessage.chat.id
        val replyMessage = update.callbackQuery.message
        val voteByContact = if (update.callbackQuery.data.startsWith("yes")) Vote.YES else Vote.NO
        val voted = voteService.findByChatIdAndMessageIdAndUserId(
            chtId,
            spamMessage.messageId.toLong(),
            userId = update.callbackQuery.from.id)
        if (voted != null &&  voted.vote == voteByContact) {
            return
        }
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
        val callBotMessageId = update.callbackQuery.data.split(" ")[1].toInt()
        rowInline.add(InlineKeyboardButton().apply {
            text = "Да: $votesYes"
            callbackData =  "yes $callBotMessageId"
        })
        rowInline.add(InlineKeyboardButton().apply {
            text = "Нет: $votesNo"
            callbackData =  "no $callBotMessageId"
        })
        rowsInline.add(rowInline)
        markupInline.keyboard = rowsInline
        updateVote.replyMarkup = markupInline
        remoteChat.execute(updateVote)
        if (votesYes >= Receiver.VOTE_SIZE_YES) {
            deleteByVoteMessage(spamMessage, replyMessage, callBotMessageId, remoteChat)
        }
        if (votesNo >= Receiver.VOTE_SIZE_NO) {
            remoteChat.execute(DeleteMessage(chtId.toString(), replyMessage.messageId))
            remoteChat.execute(DeleteMessage(chtId.toString(), callBotMessageId))
        }
    }

    private fun deleteByVoteMessage(spamMessage: Message, replyMessage: Message,
                                    callBotMessageId: Int, remoteChat: RemoteChat) {
        val chtId = spamMessage.chatId.toString()
        val spammer = contactService.findIfNotCreate(spamMessage.from)
        contactService.increaseCountOfMessages(spammer, true)
        val spam = Spam().apply {
            text = if (spamMessage.text != null) spamMessage.text else "Сообщение не текстовое"
            time = Timestamp(System.currentTimeMillis())
            contact = spammer
            chat = chatService.findOrCreate(spamMessage)
        }
        spamService.add(spam)
        remoteChat.execute(DeleteMessage(
            chtId,
            replyMessage.messageId
        ))
        remoteChat.execute(DeleteMessage(
            chtId,
            spamMessage.messageId
        ))
        remoteChat.execute(DeleteMessage(
            chtId,
            callBotMessageId
        ))
    }

    private fun Message.isMessageWithImage(): Boolean {
        return photo != null && photo.isNotEmpty()
    }
}