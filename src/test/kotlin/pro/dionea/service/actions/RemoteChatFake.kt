package pro.dionea.service.actions

import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message
import java.io.Serializable

class RemoteChatFake : RemoteChat {
    private val messages = ArrayList<BotApiMethod<out Serializable>>()

    override fun <T : Serializable, M : BotApiMethod<T>> execute(method: M): T {
        messages.add(method)
        return Message() as T
    }

    fun getMessages(): List<BotApiMethod<out Serializable>> {
        return messages
    }
}
