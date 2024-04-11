package pro.dionea.service

import org.springframework.stereotype.Service
import pro.dionea.domain.KValue

@Service
class SpamAnalysis(
    val filterService: FilterService,
    val keyService: KeyService,
    val kvalueService: KValueService) {

//    val jobSynonym = setOf("пoдрaбoткa", "работа", "рaбoтa", "заработок",
//        "зaрaбoтoк", "поднимать", "заработка", "зароботка",
//        "заработать", "работу", "доход",
//        "трейдинг", "трейдингу", "трейдингом",
//        "деятельность", "деятельностью",
//        "aрбитражу", "aрбитраж",
//        "прибыли", "биржах", "лаве", "сотрудничества")
//
//    val messageSynonym = setOf(
//        "пиши", "напиши", "напишите", "пишите", "лс",
//        "детали", "детaли", "сообщениях", "поиск", "пoиске", "бесплатно",
//        "обращайся"
//    )

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
        for (filter in filterService.getAll()) {
            val keys = keyService.findByFilterId(filter.id!!)
            if (keys.isEmpty()) {
                continue
            }
            val result = true
            for (key in keys) {
                if (!lex.containsAny(kvalueService.findByKeyId(key.id!!))) {
                    return false
                }
            }
            return result
        }
        return false
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