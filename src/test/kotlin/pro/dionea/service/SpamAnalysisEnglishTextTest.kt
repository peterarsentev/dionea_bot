package pro.dionea.service

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import pro.dionea.domain.Filter
import pro.dionea.domain.KValue
import pro.dionea.domain.Key
import pro.dionea.repository.FilterFakeRepository
import pro.dionea.repository.KValueFakeRepository
import pro.dionea.repository.KeyFakeRepository

class SpamAnalysisEnglishTextTest {

    @Test
    fun whenEnglishTextIsSpamThenTrue() {
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
        val text = ", I saw a post about your platform and I took a step to try if it’s going to work you changed my life financially \n" +
                "Why not use your 5 to 6 hours to learn how  forxe trading market , work while waiting for your salary \n" +
                "5 people are needed in this group to come and  learn forxes trading market online , to earn \$5,520 in 2 business days if interested ask how ⬇\uFE0F⬇\uFE0F\n" +
                "CLICK ON LINK (http://t.me/+qXa2EeAOVUw0ZDY0)";
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }
}