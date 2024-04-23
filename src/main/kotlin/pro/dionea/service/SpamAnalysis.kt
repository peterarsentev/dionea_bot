package pro.dionea.service

import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Service
import pro.dionea.dto.SpamReason
import java.lang.StringBuilder

@Service
class SpamAnalysis(
    val filterService: FilterService,
    val keyService: KeyService,
    val kvalueService: KValueService) {

    fun isSpam(text: String): SpamReason {
        if (text.length <= 50) {
            return SpamReason(false, "Too short.")
        }
        if (EmojiParser.extractEmojis(text).size >= 3) {
            return SpamReason(true, "More than 3 emojis.")
        }
        val lang = IdentifyLang(text).lang()
        val converted = ConvertedLetter()
        val lex =
            EmojiParser.removeAllEmojis(text)
                .replace("[.,+~?!:;(){}\n]".toRegex(), " ")
                .split("\\s+".toRegex())
                .map { it.lowercase() }
                .map { if (lang == IdentifyLang.Lang.RUS) converted.englishToRussian(it) else it }
                .toSet();
        val filters = filterService.getAll()
        val fkeys = keyService.getAll().groupBy { it.filter.id }
        val kvalues = kvalueService.getAll().groupBy { it.key.id }
        val out = StringBuilder()
        for (filter in filters) {
            val keys = fkeys.get(filter.id) ?: continue
            for (key in keys) {
                val baseWords = kvalues.get(key.id)!!.map { it.value }.toList()
                val coincidences = lex.containsAny(baseWords)
                if (coincidences.isNotEmpty()) {
                    out.append("Marked by \"${filter.name}\": ${coincidences.joinToString(", ")}\n")
                }
            }
        }
        return if (out.isNotEmpty()) {
            SpamReason(true, out.toString())
        } else  {
            SpamReason(false, "No spam.")
        }
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