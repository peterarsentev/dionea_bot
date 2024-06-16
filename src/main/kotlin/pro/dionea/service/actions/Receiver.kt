package pro.dionea.service.actions

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import pro.dionea.service.*

@Service
class Receiver(
    @Value("\${tg.name}") val name: String,
    @Value("\${tg.token}") val token: String,
    spamAnalysis: SpamAnalysis,
    spamService: SpamService,
    voteService: VoteService,
    userService: UserService,
    encoding: PasswordEncoder,
    contactService: ContactService,
    chatService: ChatService,
) : TelegramLongPollingBot(), RemoteChat {

    companion object {
        const val VOTE_SIZE_YES = 3
        const val VOTE_SIZE_NO = 3
    }

    private val actions = listOf(
        VoteCallBackAction(voteService, contactService, spamService, chatService, this),
        EmptyMessageAction(),
        JoinAction(contactService, this),
        ImageAttachedAction(contactService, this),
        PrivateChatAction(userService, encoding, this),
        ReplyAction(this, botUsername),
        TextMessageAction(contactService, spamService, spamAnalysis, chatService,this)
    )

    override fun onUpdateReceived(update: Update) {
        for (action in actions) {
            if (action.check(update)) {
                action.execute(update)
                return
            }
        }
    }

    override fun getBotToken(): String = token

    override fun getBotUsername(): String = name
}