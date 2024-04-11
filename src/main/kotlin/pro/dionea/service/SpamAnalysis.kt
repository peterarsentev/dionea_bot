package pro.dionea.service

import org.springframework.stereotype.Service

@Service
class SpamAnalysis {
    val jobSynonym = setOf("пoдрaбoткa", "работа", "рaбoтa", "заработок",
        "зaрaбoтoк", "поднимать", "заработка", "зароботка",
        "заработать", "работу", "доход",
        "трейдинг", "трейдингу", "трейдингом",
        "деятельность", "деятельностью",
        "aрбитражу", "aрбитраж",
        "прибыли", "биржах", "лаве", "сотрудничества")

    val messageSynonym = setOf(
        "пиши", "напиши", "напишите", "пишите", "лс",
        "детали", "детaли", "сообщениях", "поиск", "пoиске", "бесплатно",
        "обращайся"
    )

    fun isSpam(text: String): Boolean {
        val lex = text
            .replace(".", "")
            .replace(",", " ")
            .replace("+", " ")
            .replace("!", " ")
            .replace("?", " ")
            .replace(":", " ")
            .replace("\n", " ")
            .split(" ")
            .map { it.lowercase() }
            .toSet();

        val job = lex.containsAny(jobSynonym)
        val message = lex.containsAny(messageSynonym)
        return job && message
    }

    fun <T> Set<T>.containsAny(set: Set<T>) : Boolean {
        for (el in this) {
            if (set.contains(el)) {
                return true
            }
        }
        return false
    }
}