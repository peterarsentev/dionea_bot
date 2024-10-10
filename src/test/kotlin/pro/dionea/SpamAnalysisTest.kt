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
class SpamAnalysisTest {

    @Test
    fun isSpam1() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val key = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, key, "заработок"))
        kvalueRepository.save(KValue(2, key, "лс"))
        val text = "Нужны люди на удалённый заработок ! \n" +
                "18+ \n" +
                "Заработок возможен с любых устройств\n" +
                "От 175\$ в день\n" +
                "Пишите + в ЛС"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam2() {
        val text = "Зaрaбoтoк oт нескoльких тысяч рублей кaждый день. Рaбoтa удaлённo, легaльнo, " +
                "без oпытa, нет влoжений. Нужнo выпoлнять не слoжные зaдaния в бoте в TG\n" +
                "\n" +
                "Узнaть детaли: нaйдите в пoиске rabota_382"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam3() {
        val text = "\uD83D\uDE80Готов поднимать 100-200 \$ каждый день?\n" +
                "\n" +
                "\uD83D\uDED1БЕСПЛАТНОЕ обучение.\n" +
                "\n" +
                "\uD83D\uDD36Занятость до 2-х часов в сутки\n" +
                "\n" +
                "✋\uD83C\uDFFBОТ 18 ЛЕТ\n" +
                "\n" +
                "\uD83D\uDC8EХочешь к нам? Пиши мне \uD83D\uDC49 @glass_koln"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam4() {
        val text = "Приглашаю желающих освоить возможности хорошего дополнительного заработка на удаленке. " +
                "Опыт не обязателен, важны лишь желание и возраст от 20 лет. Детали в личных сообщениях."
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam5() {
        val text = "\uD83D\uDC4BПредлагаю возможность стабильного и удалённого зароботка!\n" +
                "\uD83D\uDCB5Поднимаем каждый день более 200\$\n" +
                "\n" +
                "\uD83D\uDD36  Не имеет значения, есть ли у вас опыт, мы научим БЕСПЛАТНО!\n" +
                "✋\uD83C\uDFFB18+\n" +
                "\n" +
                "\uD83D\uDCE8\uD83D\uDCACИнтересует? Пиши! \uD83D\uDC49 @helix_oka"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam6() {
        val text = "Здравствуйте. \n" +
                "Мы набираем партнёров в команду для взаимовыгодного сотрудничества.\n" +
                "Возраст от 20-ти лет, неполная занятость, ежедневный доход.\n" +
                "Заинтересованных жду в л.с."
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam7() {
        val text = "НУЖНЫ ЛЮДИ ДЛЯ УДАЛЕННОЙ РАБОТЫ\n" +
                "Возраст от 18+ \n" +
                "Доход от \$200 в день \n" +
                "1-2 часа времени в день\n" +
                "Пиши в ЛС"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam8() {
        val text = "Добрый день. Извиняюсь что не по теме, может кто то трейдингом занимается? " +
                "Хочу поделиться полезным материалом по трейдингу) " +
                "Вдруг кому то будет интересно, пишите в личку)"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam9() {
        val text = "Ищу людей, возьму 2-3 человека 18+ \n" +
                "Удаленная деятельность \n" +
                "От 250\$  в  день\n" +
                "Кому интересно: Пишите + в лс"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam10() {
        val text = "Готов обучить aрбитражу \n" +
                "Меж-биржeвой и внyтри-биpжевой apбитраж \n" +
                "Опeрации прoизводите на своём личном aккаунте \n" +
                "Не беру дaнные и срeдства под свoё упрaвление \n" +
                "Пpибыль получаете также на свой кoшелёк\n" +
                "Беру только прoцент от вaшей чистой пpибыли, oплата прoцентов любым удобным способом\n" +
                "По всем вопросам жду в личных сообщениях"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam11() {
        val text = "\uD83D\uDCF2 Рaбoтa - oт 5 тыс. р ежедневнo. Рaбoтa пo интернет, " +
                "без нaрушения зaкoнa, oпыт не требуется, без влoжений. " +
                "Нужны люди, чтoбы делaть лёгкие зaдaния в бoте в TG\n" +
                "\n" +
                "Пoлучить инфoрмaцию: нaйдите в пoиске rabota_382"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam12() {
        val text = "\uD83D\uDCA5 Пoдрaбoткa oт нескoльких тысяч рублей ежедневнo. " +
                "Удaлённo, зaкoннo, oпыт не требуется, без влoжений. Нaбирaем испoлнителей, " +
                "чтoбы делaть лёгкие зaдaния в в телегрaм-бoте\n" +
                "\n" +
                "Узнaть детaли: ищите в пoиске work_828"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam13() {
        val text = "В поиске людей, заинтересованных в получении " +
                "дополнительной прибыли на удаленке. Частичная занятость, от 20 лет. Подробности в лс"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam14() {
        val text = "⁉\uFE0F Ищeте заработок? Работайте прямо в Telegram!\n" +
                "\n" +
                "\uD83D\uDD38 Лёгкий заработок пять тысяч рублeй каждый дeнь\n" +
                "\uD83D\uDD38 Всё лeгально\n" +
                "\uD83D\uDD38 Работа в любоe врeмя\n" +
                "\uD83D\uDD38 Доп. знания нe нужны\n" +
                "\uD83D\uDD38 Вложeний ноль\n" +
                "\uD83D\uDD38 Оплата каждый дeнь\n" +
                "\n" +
                "Вся информация \uD83D\uDC41\u200D\uD83D\uDDE8 напишитe в поиск \"rabota9492\""
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam15() {
        val text = "‼\uFE0F\uD83C\uDD71\uFE0F\uD83C\uDD71\uFE0F\uD83C\uDD71\uFE0F\uD83C\uDD71\uFE0F\uD83C\uDD71\uFE0F\uD83C\uDD71\uFE0F‼\uFE0F\n" +
                "Возьму в команду 2-х ответственных ребят. \n" +
                "Ра6отаем с крипт0валют0й\n" +
                "Покажем бесплатно \n" +
                "Все, делаете самостоятельно на своих биржах (все на официальный биржах крипт0валют)"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam16() {
        val text = "В поиске людей, заинтересованных в получении дополнительной прибыли на удаленке. " +
                "Частичная занятость, от 20 лет. Подробности в лс"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam17() {
        val text = "ЕСТЬ ЖЕЛАНИЕ ОБУЧИТЬСЯ ПОДНИМАТЬ ЛАВЕ ОНЛАЙН?\uD83D\uDED1\n" +
                "\uD83D\uDD01Делаю набор в мою личную команду\n" +
                "\n" +
                "\uD83D\uDCB5Поднимаем каждый день более 100\$\n" +
                "\n" +
                "\uD83D\uDC8EНАУЧУ БЕСПЛАТНО. Беру свою часть только после того как заработаете!\n" +
                "\n" +
                "\uD83D\uDCACЕсть вопросы? обращайся сюда. \uD83D\uDC49@hok_stand"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam18() {
        val text = "Добрый день, ищем ответственного человека для сотрудничества, в ЛС"
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam19() {
        val text = "Ищем партнеров для пассивного заработка❗\uFE0F\n" +
                "Удаленная сфера.\n" +
                "Новое нαпрαвление.\n" +
                "Ниκακих оплαт.\n" +
                "С телефонα/пκ.\n" +
                "1 чαс времени вдень.\n" +
                "Подробнее в лс."
        println(text + "\n\r")
//        assertThat(SpamAnalysis().isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam20() {
//        val filterRepository = FilterFakeRepository()
//        val filterService = FilterService(filterRepository)
//        val keyRepository = KeyFakeRepository()
//        val keyService = KeyService(keyRepository)
//        val kvalueRepository = KValueFakeRepository()
//        val kvalueService = KValueService(kvalueRepository)
//        val filter = filterRepository.save(Filter(1))
//        val key = keyRepository.save(Key(1, filter))
//        kvalueRepository.save(KValue(key, "прибыль"))
//        kvalueRepository.save(KValue(key, "напиши"))
//        val text = "\uD83D\uDD01Провожу сбор в мою личную команду\n" +
//                "\uD83D\uDED1Прибыль от 600\$ в неделю.\uD83D\uDED1\n" +
//                "\uD83D\uDED1Прибыль с первого дня!\n" +
//                "\uD83E\uDD1DДля работы с нами тебе нужен смартфон и несколько часов  в день!\n" +
//                "\n" +
//                "\uD83D\uDCACНапиши по контактам, что бы получить больше информации  \uD83D\uDC49 @kake_ios";
//        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam21() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val key = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, key, "оплата"))
        kvalueRepository.save(KValue(2, key, "поиске"))
        val text = "\uD83E\uDD16 Платим за лёгкие задания в интернет\n" +
                "\n" +
                "Oт 4000-6000 рублей ежедневнo. Без нелегальных предлoжений и влoжения cредcтв. Oплата в любoе время. Чтoбы начать, требуетcя тoлькo запуcтить бoта\n" +
                "\n" +
                "✔\uFE0F Найди в пoиcке job_work089 и зарабатывай"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam22() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "платим"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "поиске"))
        val text = "\uD83D\uDC8E Платим за лёгкие задания в cети\n" +
                "\n" +
                "Oт 4-6 тыcяч р в день. Нет нарушения закoна и влoжений. Вывoди зарабoтаннoе в любoй мoмент. Чтoбы начать, неoбхoдимo тoлькo запуcтить бoта\n" +
                "\n" +
                "✳\uFE0F Пиши в пoиcке online_rabota8338 и начинай зарабатывать"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam23() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "подработка"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "поиске"))
        val text = "\uD83D\uDCF2 Пoдрaбoткa oт 5 тыс. р кaждый день. Удaлённo, зaкoннo, oпыт не нужен, без влoжений. Нужнo будет делaть прoстые зaдaния в бoте в Telegram\n" +
                "\n" +
                "Узнaть детaли: нaпишите в пoиске work_828"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam24() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "заработок"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "личку"))
        val text = "Прuвeт, нyжeн 1 чeлoвeк, прuятный yдaлённый зapaбoтoк, пoдрoбнocтu в лuчкy\uD83D\uDD1D\uD83C\uDD97"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam25() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "проект"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "лс"))
        val text = "Здравствуйте, нужны люди, стремительно развивающий проект, на удалённой основе + в ЛС\uD83D\uDD25\uD83C\uDFE0"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam26() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "кайф"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "лс"))
        val text = "У меня есть то, что принесет тебе кайф.Заинтересовал?Пиши в лс."
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam27() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "кайф"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "бесплатно"))
        val text = "Я бесплатно за нищие вероятные 500к такую хуйню делать не буду"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isFalse()
    }

    @Test
    fun isSpam28() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "кайф"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "бесплатно"))
        val text = "\uD83D\uDC8B  П р о б е й     з н а к о м у ю     н а      и н т и м к и \n" +
                "\n" +
                "@nmw999"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam29() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "кайф"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "бесплатно"))
        val text = "\uD83D\uDE18 пᴘоҕᴇй дᴇвʏωкʏ нᴀ нᴀличиᴇ интимок\n" +
                "\n" +
                "@Nmw999"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam30() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "кайф"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "бесплатно"))
        val text = "@Nmw999 пᴘоҕᴇй дᴇвʏωкʏ нᴀ нᴀличиᴇ интимок @Nmw999"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam31() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "кайф"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "бесплатно"))
        val text = "\uD83C\uDDF7\uD83C\uDDFAТРЕБУЮТСЯ РЕБЯТА|МУЖЧИНЫ-ЖЕНЩИНЫ ДЛЯ РАБОТЫ ОНЛАЙН\n" +
                "возраст 18+❗\uFE0F (не продажи, не валюта)\n" +
                "\n" +
                "➤Оплата достойная\n" +
                "➤Можно как подработка (совмещение)\n" +
                "➤Без опыта\n" +
                "\n" +
                "По вопросам  + Переходите сюда (https://telegra.ph/Kontakt-dlya-svyazi-08-08) !"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun isSpam32() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "сфера"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "поиск"))
        val text = "Добрый день! В поиске людей. Удаленная сфера занятости,подойдет каждому. За дополнительной информацией пишите в личку"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun whenContainEmojiAndContact() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val text = "ХОРОШЕЕ НАСТРОЕНИЕ тут \uD83C\uDF40\uD83C\uDF31 @omgomgsh"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }

    @Test
    fun whenContainSlash() {
        val filterRepository = FilterFakeRepository()
        val filterService = FilterService(filterRepository)
        val keyRepository = KeyFakeRepository()
        val keyService = KeyService(keyRepository)
        val kvalueRepository = KValueFakeRepository()
        val kvalueService = KValueService(kvalueRepository)
        val filter = filterRepository.save(Filter(1))
        val keyJob = keyRepository.save(Key(1, filter))
        kvalueRepository.save(KValue(1, keyJob, "работ"))
        val keyMessage = keyRepository.save(Key(2, filter))
        kvalueRepository.save(KValue(2, keyMessage, "писать"))
        val text = "!!СРOЧНO ВAЖНO СРOЧНO!!!\n" +
                "!!!НYЖНЫ ЛЮДИ!!!\n" +
                "НА OНЛAЙH ЗAРAБOТOК/СOTРYДНИЧЕСТВO \n" +
                "25+\n" +
                "2-3 ЧАСА В ДЕНЬ\n" +
                "950-1500\$ В НEДEЛЮ\n" +
                "ПИСAТЬ @romankovoy"
        assertThat(SpamAnalysis(filterService, keyService, kvalueService).isSpam(text).spam).isTrue()
    }
}