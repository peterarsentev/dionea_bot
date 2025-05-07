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

class SpamAnalysisCurrencyTextTest {

    @Test
    fun whenEnglishTextIsSpamThenTrue() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val text = "Нужны люди на уд@ленку, от 6 000 ₽ в день, пару часов в день, от 18 лет. Писать в лс +";
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun whenDollarSpamThenTrue() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val text = "Доброго времени суток. Ищу заинтересованных людей. Доход от 160 долларов в день. Интересно? Пишите плюс в личные сообщения";
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }
}