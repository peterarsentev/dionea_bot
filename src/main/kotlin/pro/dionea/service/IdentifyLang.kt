package pro.dionea.service

class IdentifyLang(val lex: Set<String>) {
    private val englishRange = 'a' .. 'z'
    private val russianRange = 'а'.. 'я'

    enum class Lang {
        RUS, ENG
    }

    private fun Set<String>.countBy(range: CharRange): Int =
        this.count { word -> word.any { it in range } }

    fun lang() : Lang = if (lex.countBy(englishRange) >= lex.countBy(russianRange)) {
        Lang.ENG
    } else {
        Lang.RUS
    }
}