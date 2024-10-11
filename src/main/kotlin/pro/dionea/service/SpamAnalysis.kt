package pro.dionea.service

import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Service
import pro.dionea.dto.SpamReason
import java.util.regex.Pattern

@Service
class SpamAnalysis(
    val filterService: FilterService,
    val keyService: KeyService,
    val kvalueService: KValueService) {

    companion object {
        val CONTACT_PATTERN = Pattern.compile("@\\w+")
        const val MIN_SIZE_OF_MESSAGE = 35
    }

    fun isSpam(text: String): SpamReason {
        if (text.length < MIN_SIZE_OF_MESSAGE) {
            return SpamReason(false, "Сообщение короткое.")
        }
        val emojis = EmojiParser.extractEmojis(text)
        if (emojis.size >= 3) {
            return SpamReason(true, "Содержит более 3 эмоджи.")
        }
        val count = CONTACT_PATTERN.matcher(text).results().count()
        if (count > 1) {
            return SpamReason(true, "Содержит множественные контактные данные.")
        }
        if (emojis.isNotEmpty() && count > 0) {
            return SpamReason(true, "Содержит эмодзи и контактный логин.")
        }
        val lex = EmojiParser.removeAllEmojis(text)
            .replace("[.,+~?!:;(){}\n/]".toRegex(), " ")
            .split("\\s+".toRegex())
            .asSequence()
            .filter { it.length > 2 }
            .map { it.lowercase() }
            .toSet()
        val lang = IdentifyLang(lex).lang()
        if (lang == IdentifyLang.Lang.MIXED) {
            return SpamReason(true, "Русские буквы заменены на английские.")
        }

        val filters = filterService.getAll()
        val fkeys = keyService.getAll().groupBy { it.filter.id }
        val kvalues = kvalueService.getAll().groupBy { it.key.id }
        val out = StringBuilder()
        for (filter in filters) {
            val keys = fkeys.get(filter.id) ?: continue
            var matched = 0
            for (key in keys) {
                val baseWords = kvalues.get(key.id)!!.map { it.value }.toList()
                val coincidences = lex.containsAny(baseWords)
                if (coincidences.size >= 3) {
                    return SpamReason(true,
                        "Стоп-фильтр \"${filter.name}\": ${coincidences.joinToString(", ")}\n")
                }
                if (coincidences.isNotEmpty()) {
                    matched++
                    out.append("Стоп-фильтр \"${filter.name}\": ${coincidences.joinToString(", ")}\n")
                }
            }
            if (matched == keys.size) {
                return SpamReason(true, out.toString())
            }
        }
        return SpamReason(false, "Не спам")
    }

    fun Set<String>.containsAny(baseWords: List<String>) : List<String> {
        val result = ArrayList<String>()
        for (messageWord in this) {
            for (baseWord in baseWords) {
                if (messageWord.contains(baseWord)) {
                    result.add(messageWord)
                }
            }
        }
        return result
    }
}