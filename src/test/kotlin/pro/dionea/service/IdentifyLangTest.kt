package pro.dionea.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class IdentifyLangTest {
    @Test
    fun whenTextRussia() {
        assertThat(
            IdentifyLang(
                setOf("писать")
            ).lang()
        ).isEqualTo(IdentifyLang.Lang.RUS)
    }

    @Test
    fun whenMessageContainsNumber() {
        assertThat(
            IdentifyLang(
                setOf("писать", "1234567890")
            ).lang()
        ).isEqualTo(IdentifyLang.Lang.RUS)
    }

    @Test
    fun whenMessageContainsMix() {
        val lex = setOf("дохoд", "пuсать")
        val size = IdentifyLang(lex).sizeByLang()
        assertThat(size[IdentifyLang.Lang.RUS]).isEqualTo(9)
    }
}