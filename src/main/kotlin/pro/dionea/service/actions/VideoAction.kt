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

class VideoAction(val contactService: ContactService): UpdateAction {

    override fun check(update: Update): Boolean {
        return update.message.hasVideo()
    }

    override fun execute(update: Update, remoteChat: RemoteChat) {
        val message = update.message
        val userContact = contactService.findIfNotCreate(message.from)
        if (userContact.ham == 0) {
            remoteChat.execute(DeleteMessage(message.chatId.toString(), message.messageId))
            remoteChat.execute(RestrictChatMember().apply {
                chatId = message.chatId.toString()
                userId = message.from.id
                permissions = ChatPermissions().apply {
                    canSendMessages = true
                }
            })
            val msgInfo = remoteChat.execute(
                SendMessage(
                message.chatId.toString(),
                "Здравствуйте, ${message.from.firstName}.\n" +
                        "Мы ограничили вам доступ на отправку изображений. \n" +
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