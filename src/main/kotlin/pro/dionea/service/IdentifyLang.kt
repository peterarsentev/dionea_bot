package pro.dionea.service

class IdentifyLang(val text: String) {
    private val englishRange = 'a' .. 'z'
    private val russianRange = 'а'.. 'я'

    enum class Lang {
        RUS, ENG, UNKNOWN
    }

    private fun String.countBy(range: CharRange) = this.count { it in range }

    fun lang() : Lang = if (text.countBy(englishRange) >= text.countBy(russianRange)) {
        Lang.ENG
    } else {
        Lang.RUS
    }
}