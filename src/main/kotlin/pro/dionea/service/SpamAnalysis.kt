package pro.dionea.service

import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Service

@Service
class SpamAnalysis(
    val filterService: FilterService,
    val keyService: KeyService,
    val kvalueService: KValueService) {

    fun isSpam(text: String): Boolean {
        if (EmojiParser.extractEmojis(text).size >= 3) {
            return true
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