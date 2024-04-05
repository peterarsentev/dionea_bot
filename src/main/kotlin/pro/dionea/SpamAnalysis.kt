package pro.dionea

import org.springframework.stereotype.Service

@Service
class SpamAnalysis {
    val jobSynonym = setOf("работа", "заработок",
        "зaрaбoтoк", "поднимать", "заработка", "зароботка",
        "заработать", "работу", "доход",
        "трейтинг", "трейдингу")

    val messageSynonym = setOf(
        "пиши", "напиши", "напишите", "пишите", "лс",
        "детали", "детaли"
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