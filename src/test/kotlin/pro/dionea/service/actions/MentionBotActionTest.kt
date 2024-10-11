package pro.dionea.service.actions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

class MentionBotActionTest {

    private lateinit var mentionBotAction: MentionBotAction
    private lateinit var remoteChat: RemoteChatFake
    private val botName = "MyBot"

    @BeforeEach
    fun setUp() {
        mentionBotAction = MentionBotAction(botName)
        remoteChat = RemoteChatFake()
    }

    @Test
    fun `when check with valid update containing botName then return true`() {
        val update = createUpdate("Hello @MyBot", isReply = false)
        val result = mentionBotAction.check(update)
        assertThat(result).isTrue()
    }

    @Test
    fun `when check with null message text then return false`() {
        val update = createUpdate(null, isReply = false)
        val result = mentionBotAction.check(update)
        assertThat(result).isFalse()
    }

    @Test
    fun `when check with message that does not contain botName then return false`() {
        val update = createUpdate("Hello World", isReply = false)
        val result = mentionBotAction.check(update)
        assertThat(result).isFalse()
    }

    @Test
    fun `when execute then send message with correct chatId and markdown formatting`() {
        val chatId = 123456L
        val update = createUpdate("Hello @MyBot", isReply = false, chatId = chatId)

        mentionBotAction.execute(update, remoteChat)

        val sentMessages = remoteChat.getMessages()
        assertThat(sentMessages).hasSize(1)

        val sentMessage = sentMessages[0] as SendMessage
        assertThat(sentMessage.chatId).isEqualTo(chatId.toString())
        assertThat(sentMessage.parseMode).isEqualTo("Markdown")
        assertThat(sentMessage.text.trimIndent()).isEqualTo(
            """
            Извините, бот не смог обнаружить спам-сообщение.

            Вы можете:
            1. **Процитировать** сообщение со спамом и **проголосовать за его удаление**.
            2. **Создать issue** с текстом спама на нашем GitHub:
               
               [https://github.com/peterarsentev/dionea_bot/issues](https://github.com/peterarsentev/dionea_bot/issues)

            Спасибо за вашу помощь! Мы продолжаем совершенствовать наш анализатор спама.
            """.trimIndent()
        )
    }

    private fun createUpdate(text: String?, isReply: Boolean, chatId: Long = 0L): Update {
        val update = Update()
        val message = Message()
        val chat = Chat()

        chat.id = chatId
        message.chat = chat
        message.text = text
        if (isReply) {
            message.replyToMessage = Message()
        }
        update.message = message

        return update
    }
}
