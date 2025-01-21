package pro.dionea.service.actions

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import pro.dionea.service.*

@Configuration
@Suppress("unused")
class ActionConfig(
    private val voteService: VoteService,
    private val contactService: ContactService,
    private val spamService: SpamService,
    private val chatService: ChatService,
    private val userService: UserService,
    private val encoding: PasswordEncoder,
    private val spamAnalysis: SpamAnalysis
) {

    @Bean
    fun actions(@Value("\${tg.name}") botUsername: String): List<UpdateAction> {
        return listOf(
            VoteCallBackAction(voteService, contactService, spamService, chatService),
            EmptyMessageAction(),
            JoinAction(contactService),
            ImageAttachedAction(contactService),
            KeyboardMarkUpAction(contactService),
            PrivateChatAction(userService, encoding),
            ReplyAction(botUsername),
            MentionBotAction(botUsername),
            TextMessageAction(contactService, spamService, spamAnalysis, chatService)
        )
    }
}
