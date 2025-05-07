package pro.dionea.service

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import pro.dionea.repository.FilterFakeRepository
import pro.dionea.repository.KValueFakeRepository
import pro.dionea.repository.KeyFakeRepository

class SpamAnalysisShortTextTest {

    @Test
    fun whenShortTextWithNumberThenSpamTrue() {
        val filterService = FilterService(FilterFakeRepository())
        val keyService = KeyService(KeyFakeRepository())
        val kvalueService = KValueService(KValueFakeRepository())
        val text = """
            Зарабатываю 10 баксов в час, пару часов в день с телефоном и доволен.
        """.trimIndent();
        assertThat(
            SpamAnalysis(
                filterService,
                keyService,
                kvalueService)
                .isSpam(text).spam)
            .isTrue()
    }
}