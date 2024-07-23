package pro.dionea.service.actions

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.ChatPermissions
import org.telegram.telegrambots.meta.api.objects.Update
import pro.dionea.service.ContactService

class JoinAction(
    val contactService: ContactService): UpdateAction {

    override fun check(update: Update): Boolean
            = update.message.newChatMembers.isNotEmpty()

    override fun execute(update: Update, remoteChat: RemoteChat) {
        val message = update.message
        val newMembers = message.newChatMembers
        for (member in newMembers) {
            val contact = contactService.findIfNotCreate(member)
            if (member.userName == null || member.userName.isEmpty()) {
                contactService.save(
                    contact.apply {
                        restrict = true
                    }
                )
                remoteChat.execute(RestrictChatMember().apply {
                    chatId = message.chatId.toString()
                    userId = member.id
                    permissions = ChatPermissions().apply {
                        canSendMessages = false
                    }
                })
                val msgInfo = remoteChat.execute(
                    SendMessage(
                        message.chatId.toString(),
                        "Здравствуйте, ${member.firstName}.\n" +
                                "В вашем аккаунте скрыты контактные данные. \n" +
                                "Обычно такие аккаунты рассылают спам сообщения, " +
                                "поэтому мы ограничили вам доступ. \n" +
                                "Сообщение будет удалено через 10 секунд."
                    )
                )
                GlobalScope.launch {
                    delay(10000)
                    remoteChat.execute(DeleteMessage(message.chatId.toString(), msgInfo.messageId))
                }
            }
        }
    }
}