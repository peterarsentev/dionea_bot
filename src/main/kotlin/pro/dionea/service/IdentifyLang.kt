package pro.dionea.service

class IdentifyLang(private val lex: Set<String>) {
    private val englishRange = 'a' .. 'z'
    private val russianRange = 'а'.. 'я'

    enum class Lang {
        RUS, ENG, MIXED, UNDEFINED
    }

    private fun Set<String>.countBy(range: CharRange): Int =
        flatMap { it.asSequence() }
            .count { it in range }

    fun sizeByLang() : Map<Lang, Int>
            = mapOf(
        Lang.RUS to lex.countBy(russianRange),
        Lang.ENG to lex.countBy(englishRange)
    )

    private fun String.lang() : Lang {
        val rus = count { it in russianRange }
        val eng = count { it in englishRange }
        return if (rus != 0 && eng != 0) {
            Lang.MIXED
        } else if (rus != 0) {
            Lang.RUS
        } else if (eng != 0) {
            Lang.ENG
        } else {
            Lang.UNDEFINED
        }
}

    fun lang() : Lang {
        var lang = Lang.UNDEFINED
        for (word in lex) {
            val wordLang = word.lang()
            if (wordLang == Lang.UNDEFINED) {
                continue
            }
            if (lang == Lang.UNDEFINED) {
                lang = wordLang
            } else if (lang != wordLang) {
                return Lang.MIXED
            }
        }
        return lang
    }
}