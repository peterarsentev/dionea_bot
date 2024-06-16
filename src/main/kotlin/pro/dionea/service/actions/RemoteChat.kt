package pro.dionea.service.actions

import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import java.io.Serializable

interface RemoteChat {
    fun <T : Serializable, M : BotApiMethod<T>> execute(method: M): T
}