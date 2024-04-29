package pro.dionea.service

class ConvertedLetter {

    private fun englishGraphicLetterToRussian(letter: Char): Pair<Char, Boolean> {
        return when (letter) {
            'a' -> Pair('а', true)
            'c' -> Pair('с', true)
            'e' -> Pair('е', true)
            'k' -> Pair('к', true)
            'o' -> Pair('о', true)
            'p' -> Pair('р', true)
            'u' -> Pair('и', true)
            'x' -> Pair('х', true)
            'y' -> Pair('у', true)
            else -> Pair(letter, false)
        }
    }
    fun englishToRussian(words: Set<String>) : Pair<Set<String>, Int> {
        val replaced = words.map { englishToRussian(it) }.toList()
        return Pair(
            replaced.map { it.first }.toSet(),
            replaced.sumOf { it.second }
        )
    }
    fun englishToRussian(word: String) : Pair<String, Int> {
        val replaced = word.map { englishGraphicLetterToRussian(it) }
            .toList()
        return Pair(
            replaced.map { it.first }.joinToString(""),
            replaced.count { it.second }
        )
    }
}