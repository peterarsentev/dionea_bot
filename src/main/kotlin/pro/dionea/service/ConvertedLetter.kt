package pro.dionea.service

class ConvertedLetter {

    fun englishGraphicLetterToRussian(letter: Char): Char {
        return when (letter) {
            'a' -> 'а'
            'c' -> 'с'
            'e' -> 'е'
            'k' -> 'к'
            'o' -> 'о'
            'p' -> 'р'
            'u' -> 'и'
            'x' -> 'x'
            'y' -> 'у'
            else -> letter
        }
    }
    fun englishToRussian(word: String) : String
            = word.map { englishGraphicLetterToRussian(it) }
        .joinToString(separator = "")
}