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

@Disabled
class SpamAnalysisSignEnglishLetterTest {

    @Test
    fun whenEnglishTextIsSpamThenTrue() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val text = "Coтpyднuчecтвo в cфepe Crуptо, выгoдныe ycлoвия, oпыт нe нyжeн oбyчuм, cвязaтьcя: @KSTRun"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }
}