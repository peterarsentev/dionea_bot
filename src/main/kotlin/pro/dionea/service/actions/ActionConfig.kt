package pro.dionea.service.actions

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import pro.dionea.service.*

@Configuration
class ActionConfig(
    private val voteService: VoteService,
    private val contactService: ContactService,
    private val spamService: SpamService,
    private val chatService: ChatService,
    private val userService: UserService,
    private val encoding: PasswordEncoder,
    private val spamAnalysis: SpamAnalysis,
) {

    @Bean
    fun actions(remoteChat: RemoteChat, botUsername: String): List<UpdateAction> {
        return listOf(
            VoteCallBackAction(voteService, contactService, spamService, chatService, remoteChat),
            EmptyMessageAction(),
            JoinAction(contactService, remoteChat),
            ImageAttachedAction(contactService, remoteChat),
            PrivateChatAction(userService, encoding, remoteChat),
            ReplyAction(remoteChat, botUsername),
            TextMessageAction(contactService, spamService, spamAnalysis, chatService, remoteChat)
        )
    }
}
