package pro.dionea.service.actions

import org.telegram.telegrambots.meta.api.objects.Update

class EmptyMessageAction: UpdateAction {
    override fun check(update: Update): Boolean
            = !update.hasMessage()

    override fun execute(update: Update, remoteChat: RemoteChat) {
    }
}