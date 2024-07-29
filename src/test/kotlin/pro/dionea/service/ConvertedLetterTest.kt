package pro.dionea.service

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*

class ConvertedLetterTest {

    @Test
    fun whenRussTextHasEgnlishLetter() {
        val text = "Зaрaбoтoк oт нескoльких тысяч рублей кaждый день. Рaбoтa удaлённo, легaльнo, \" +\n" +
                "                \"без oпытa, нет влoжений. Нужнo выпoлнять не слoжные зaдaния в бoте в TG\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Узнaть детaли: нaйдите в пoиске rabota_382"
        val (_, size) = ConvertedLetter().englishToRussian(text)
        assertThat(size).isEqualTo(30)
    }

    @Test
    fun whenEngAToRus() {
        val text = "кoмaнду"
        val expected = "команду"
        assertThat(text).isNotEqualTo(expected)
        val (word, size) = ConvertedLetter().englishToRussian(text)
        assertThat(word).isEqualTo(expected)
        assertThat(size).isEqualTo(2)
    }

    @Test
    fun whenEngOToRus() {
        val text = "зapaбoтoк"
        val expected = "заработок"
        assertThat(text).isNotEqualTo(expected)
        assertThat(ConvertedLetter().englishToRussian(text).first)
            .isEqualTo(expected)
    }
}