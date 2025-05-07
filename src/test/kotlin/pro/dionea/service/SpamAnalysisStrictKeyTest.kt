package pro.dionea.service

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

class SpamAnalysisStrictKeyTest {

    @Test
    fun whenEnglishTextIsSpamThenTrue() {
        val text = "Нужнa бригaдa , пoдрoбнee в лс";
        val tokens = text.tokens()
        val keys = listOf("!лс")
        assertThat(tokens.containsAny(keys))
            .isEqualTo(listOf("лс"))
    }

    @Test
    fun whenFullTrue() {
        val text = "Нужнa бригaдa , пoдрoбнee в полсол";
        val tokens = text.tokens()
        val keys = listOf("!лс")
        assertThat(tokens.containsAny(keys))
            .isEmpty()
    }
}