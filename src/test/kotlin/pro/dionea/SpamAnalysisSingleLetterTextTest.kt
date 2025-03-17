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

class SpamAnalysisSingleLetterTextTest {

    @Test
    fun whenTextIsSingleLettersThenSpamTrue() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "trading"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "interested"))
        val text = """
            H У Ж H Ы   Д E H Ь Г И???  П И Ш И   В   Л С!!!
        """.trimIndent();
        assertThat(
            SpamAnalysis(
                filterService,
                keyService,
                kvalueService)
                .isSpam(text).spam)
            .isTrue()
    }

    @Test
    fun whenTextIsSingleInFontLettersThenSpamTrue() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "trading"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "interested"))
        val text = """
            C P O Ч H O❗️
            П α с с и в н ы й   з α ρ α б ο т ο κ.
            У д α л е н н α я   с ф е ρ α.
            Н е  з α κ л α д κ и   и   п ρ ο ч α я  е ρ у н д α.
            Ч α с   в ρ е м е н и   в   д е н ь.
            С  л ю б ο й   т ο ч κ и  миρ α.
            С   т е л е ф ο н α/п κ.
            Н е   б е ρ е м   ο т   в α с   н и κ α κ и х   ο п л α т.❗️
            С т р о г о   20+
            И н т е ρ е с н ο?   "+" в   л и ч н ы е.
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