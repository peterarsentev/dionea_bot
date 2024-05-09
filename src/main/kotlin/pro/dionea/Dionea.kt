package pro.dionea

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.ApplicationPidFileWriter
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import pro.dionea.service.*

@SpringBootApplication
class Dionea {
	@Bean
	fun encryptor() = BCryptPasswordEncoder()

	@Bean
	fun telegramApi(
		@Value("\${tg.use}") use: Boolean,
		receiver: Receiver
	) : TelegramBotsApi {
		val tg = TelegramBotsApi(DefaultBotSession::class.java)
		if (use) {
			tg.registerBot(receiver)
		}
		return tg
	}
}

fun main() {
	val application = SpringApplication(Dionea::class.java)
	application.addListeners(ApplicationPidFileWriter("./dionea.pid"))
	application.run()
	println("The application has started. Please go to http://localhost:8080/ to access it.")
}
