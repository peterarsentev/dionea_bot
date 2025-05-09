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
        val URL_PATTERN = Pattern.compile("https?://[\\w-]+(\\.[\\w-]+)+(/[\\w-]*)*")
        const val MIN_SIZE_OF_MESSAGE = 30
    }

    fun isSpam(text: String): SpamReason {
        if (text.length < MIN_SIZE_OF_MESSAGE) {
            return SpamReason(false, "Сообщение короткое.")
        }
        if (text.containsCurrency()) {
            return SpamReason(true, "Содержит символы валюты.")
        }
        val emojis = EmojiParser.extractEmojis(text)
        if (emojis.size >= 3) {
            return SpamReason(true, "Содержит более 3 эмоджи.")
        }
        val count = CONTACT_PATTERN.matcher(text).results().count()
        if (count > 1) {
            return SpamReason(true, "Содержит множественные контактные данные.")
        }
        val multipleUrls = URL_PATTERN.matcher(text).results().count()
        if (multipleUrls > 1) {
            return SpamReason(true, "Содержит множественные ссылки.")
        }
        if (emojis.isNotEmpty() && count > 0) {
            return SpamReason(true, "Содержит эмодзи и контактный логин.")
        }
        if (text.length <= text.count { it == ' ' } * 2.2) {
            return SpamReason(true, "Символы разделены пробелами.")
        }
        val lex = text.tokens()
        if (lex.isEmpty() && text.trim().isNotEmpty()) {
            return SpamReason(true, "Символы разделены пробелами.")
        }
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
}

fun Set<String>.containsAny(keys: List<String>) : List<String> {
    val result = ArrayList<String>()
    for (messageWord in this) {
        for (key in keys) {
            if (key[0] == '!' && messageWord.equals(key.substring(1))
                || messageWord.contains(key)) {
                result.add(messageWord)
            }
        }
    }
    return result
}

fun String.tokens(): Set<String>
        = EmojiParser.removeAllEmojis(this)
    .replace("[.,+~?!:;(){}\n/]".toRegex(), " ")
    .split("\\s+".toRegex())
    .asSequence()
    .filter { it.length >= 2 }
    .map { it.lowercase() }
    .toSet()

fun String.containsCurrency(): Boolean {
    val symbols = setOf('$', '€', '₽')
    if (this.any { it in symbols }) return true
    val currencyWords = setOf(
        "руб", "руб.", "рублей", "рубля", "рубли",
        "доллар", "доллара", "долларов", "доллары", "баксы", "баксов",
        "евро", "eur", "usd"
    )
    return this.tokens().any { it in currencyWords }
}
