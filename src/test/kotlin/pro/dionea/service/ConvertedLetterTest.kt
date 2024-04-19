package pro.dionea.service

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*

class ConvertedLetterTest {

    @Test
    fun whenEngAToRus() {
        val text = "кoмaнду"
        val expected = "команду"
        assertThat(text).isNotEqualTo(expected)
        assertThat(ConvertedLetter().englishToRussian(text))
            .isEqualTo(expected)
    }
}