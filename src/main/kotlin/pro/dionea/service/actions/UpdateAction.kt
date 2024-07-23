package pro.dionea.service.actions

import org.telegram.telegrambots.meta.api.objects.Update

interface UpdateAction {
    fun check(update: Update): Boolean
    fun execute(update: Update, remoteChat: RemoteChat)
}