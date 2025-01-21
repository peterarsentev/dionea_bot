package pro.dionea.service.actions

import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.ChatPermissions
import org.telegram.telegrambots.meta.api.objects.Update
import pro.dionea.service.ContactService

class KeyboardMarkUpAction(val contactService: ContactService) : UpdateAction {

    override fun check(update: Update): Boolean
            = update.hasMessage() && update.message.hasReplyMarkup()

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
        }
    }
}