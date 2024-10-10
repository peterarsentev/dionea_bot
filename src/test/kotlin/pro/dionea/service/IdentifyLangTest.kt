package pro.dionea.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class IdentifyLangTest {
    @Test
    fun whenTextRussia() {
        assertThat(
            IdentifyLang(
                setOf("Писать")
            ).lang()
        ).isEqualTo(IdentifyLang.Lang.RUS)
    }

    @Test
    fun whenMessageContainsNumber() {
        assertThat(
            IdentifyLang(
                setOf("Писать", "1234567890")
            ).lang()
        ).isEqualTo(IdentifyLang.Lang.RUS)
    }
}