package pro.dionea.service.actions

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class Receiver(
    @Value("\${tg.name}") val name: String,
    @Value("\${tg.token}") val token: String,
    private val actions: List<UpdateAction>
) : TelegramLongPollingBot(), RemoteChat {

    companion object {
        const val VOTE_SIZE_YES = 3
        const val VOTE_SIZE_NO = 3
    }

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