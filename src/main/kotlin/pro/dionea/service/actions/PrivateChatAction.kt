package pro.dionea.service.actions

import org.springframework.security.crypto.password.PasswordEncoder
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import pro.dionea.domain.User
import pro.dionea.service.KeyGen
import pro.dionea.service.UserService

class PrivateChatAction(val userService: UserService,
                        val encoding: PasswordEncoder) : UpdateAction {

    override fun check(update: Update): Boolean
            = update.message.chat.type == "private" && update.message.text.startsWith("/")

    override fun execute(update: Update, remoteChat: RemoteChat) {
        val message = update.message
        if (message.text.startsWith("/")) {
            if ("/start" == message.text) {
                remoteChat.execute(
                    SendMessage(
                        message.chatId.toString(),
                        """
                            Дионеа Бот - ваш универсальный помощник для групп в Telegram:

                            - **Удаление спама**: Автоматически анализирует и удаляет спам-сообщения, поддерживая чистоту в чате.
                            - **Анализ изображений**: Распознает и удаляет спам на изображениях, предотвращая визуальные атаки.
                            - **Фильтрация порнографического контента**: Обнаруживает и удаляет порнографические изображения, обеспечивая безопасность группы.
                            - **Голосования**: Поддерживает голосование за удаление сообщений или бан участников, предоставляя право голоса каждому. Чтобы проголосовать, просто ответьте на сообщение с упоминанием бота.
                            - **Настраиваемые фильтры**: Позволяет создавать и настраивать свои фильтры через удобный веб-интерфейс.
                            - **Планировщик публикаций**: Планируйте публикации и автоматически размещайте сообщения по расписанию.
                            
                            Подробнее на [job4j.ru/dionea](https://job4j.ru/dionea).
                            
                            Зарегистрируйтесь с помощью команды /reg.
                        """.trimIndent()
                    ).apply { parseMode = "Markdown" }
                )
            }
            if ("/reg" == message.text) {
                val pwd = KeyGen(8).generate()
                val user = userService.add(
                    User().apply {
                        username = message.from.userName
                        password = encoding.encode(pwd)
                        enabled = true
                    }
                )
                remoteChat.execute(
                    SendMessage(
                        message.chatId.toString(),
                        "Доступ к сайту https://job4j.ru/dionea\n" +
                                "Логин: ${user.username}\n" +
                                "Пароль: $pwd"
                    )
                )
            }
        }
    }
}