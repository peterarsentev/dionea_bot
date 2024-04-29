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

    companion object {
        const val CONVERTED_LETTERS = 20
    }

    fun isSpam(text: String): SpamReason {
        if (text.length <= 50) {
            return SpamReason(false, "Too short.")
        }
        if (EmojiParser.extractEmojis(text).size >= 3) {
            return SpamReason(true, "More than 3 emojis.")
        }
        val lang = IdentifyLang(text).lang()
        val converted = ConvertedLetter()
        val lex = EmojiParser.removeAllEmojis(text)
            .replace("[.,+~?!:;(){}\n]".toRegex(), " ")
            .split("\\s+".toRegex())
            .asSequence()
            .filter { it.length > 2 }
            .map { it.lowercase() }
            .toSet()
        val words =
            if (lang == IdentifyLang.Lang.RUS) {
               val convertedLetter = converted.englishToRussian(lex)
                if (convertedLetter.second >= CONVERTED_LETTERS) {
                    return SpamReason(true, "More than 20 letters are converted from English to Russian.")
                } else {
                    convertedLetter.first
                }
            } else {
                lex
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
                val coincidences = words.containsAny(baseWords)
                if (coincidences.isNotEmpty()) {
                    matched++
                    out.append("Marked by \"${filter.name}\": ${coincidences.joinToString(", ")}\n")
                }
            }
            if (matched == keys.size) {
                return SpamReason(true, out.toString())
            }
        }
        return SpamReason(false, "No spam.")
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