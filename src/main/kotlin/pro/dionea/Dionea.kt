package pro.dionea

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.ApplicationPidFileWriter
import org.springframework.context.annotation.Bean
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import pro.dionea.service.Receiver
import pro.dionea.service.SpamAnalysis
import pro.dionea.service.SpamService

@SpringBootApplication
class DeoneaApplication {
	@Bean
	fun telegramApi(
		@Value("\${tg.name}") name: String,
		@Value("\${tg.token}") token: String,
		analysis: SpamAnalysis,
		spamService: SpamService
	) : TelegramBotsApi {
		val tg = TelegramBotsApi(DefaultBotSession::class.java)
		tg.registerBot(Receiver(name, token, analysis, spamService))
		return tg
	}
}

fun main(args: Array<String>) {
	val application = SpringApplication(DeoneaApplication::class.java)
	application.addListeners(ApplicationPidFileWriter("./dionea.pid"))
	application.run()
	println("Go to http://localhost:8080/")
}
