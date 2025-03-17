package pro.dionea

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import pro.dionea.domain.Filter
import pro.dionea.domain.KValue
import pro.dionea.domain.Key
import pro.dionea.repository.FilterFakeRepository
import pro.dionea.repository.KValueFakeRepository
import pro.dionea.repository.KeyFakeRepository
import pro.dionea.service.FilterService
import pro.dionea.service.KValueService
import pro.dionea.service.KeyService
import pro.dionea.service.SpamAnalysis

class SpamAnalysisShortTextTest {

    @Disabled
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