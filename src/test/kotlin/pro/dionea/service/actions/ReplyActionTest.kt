package pro.dionea.service.actions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup

class ReplyActionTest {

    private lateinit var remoteChat: RemoteChatFake
    private lateinit var replyAction: ReplyAction
    private val botName = "testBot"

    @BeforeEach
    fun setUp() {
        remoteChat = RemoteChatFake()
        replyAction = ReplyAction(botName)
    }

    @Test
    fun `when check with valid update then return true`() {
        val update = createUpdate("testBot", true)
        val result = replyAction.check(update)
        assertThat(result).isTrue()
    }

    @Test
    fun `when check with invalid update then return false`() {
        val update = createUpdate("someOtherBot", true)
        val result = replyAction.check(update)
        assertThat(result).isFalse()
    }

    @Test
    fun `when execute then send message with correct properties`() {
        val update = createUpdate(botName, true)
        replyAction.execute(update, remoteChat)
        val messages = remoteChat.getMessages()
        assertThat(messages).hasSize(1)
        val sendMessage = messages[0] as SendMessage
        with(sendMessage) {
            assertThat(chatId).isEqualTo("12345")
            assertThat(text).isEqualTo("Это спам? Проголосуйте за 3 раза.")
            assertThat(replyToMessageId).isEqualTo(2)

            val markup = replyMarkup as InlineKeyboardMarkup
            val keyboard = markup.keyboard
            assertThat(keyboard).hasSize(1)
            assertThat(keyboard[0]).hasSize(2)
            assertThat(keyboard[0][0].text).isEqualTo("Да: 0")
            assertThat(keyboard[0][0].callbackData).isEqualTo("yes 1")
            assertThat(keyboard[0][1].text).isEqualTo("Нет: 0")
            assertThat(keyboard[0][1].callbackData).isEqualTo("no 1")
        }
    }

    private fun createUpdate(botName: String, isReply: Boolean): Update {
        val update = Update()
        val message = Message()
        message.chat = Chat(12345L, "private")
        message.text = botName
        if (isReply) {
            val replyToMessage = Message()
            replyToMessage.messageId = 2
            message.replyToMessage = replyToMessage
        }
        message.messageId = 1
        update.message = message
        return update
    }
}
