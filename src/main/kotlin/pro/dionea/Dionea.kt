package pro.dionea

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.ApplicationPidFileWriter
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import pro.dionea.service.Receiver
import pro.dionea.service.SpamAnalysis
import pro.dionea.service.SpamService
import pro.dionea.service.VoteService

@SpringBootApplication
class Dionea {
	@Bean
	fun encryptor() = BCryptPasswordEncoder()

	@Bean
	fun telegramApi(
		@Value("\${tg.name}") name: String,
		@Value("\${tg.token}") token: String,
		@Value("\${tg.use}") use: Boolean,
		analysis: SpamAnalysis,
		spamService: SpamService,
		voteService: VoteService
	) : TelegramBotsApi {
		val tg = TelegramBotsApi(DefaultBotSession::class.java)
		if (use) {
			tg.registerBot(Receiver(name, token, analysis, spamService, voteService, name))
		}
		return tg
	}
}

fun main(args: Array<String>) {
	val application = SpringApplication(Dionea::class.java)
	application.addListeners(ApplicationPidFileWriter("./dionea.pid"))
	application.run()
	println("The application has started. Please go to http://localhost:8080/ to access it.")
}
