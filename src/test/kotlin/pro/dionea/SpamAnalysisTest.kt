package pro.dionea

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class SpamAnalysisTest {

    @Test
    fun isSpam1() {
        val text = "Нужны люди на удалённый заработок ! \n" +
                "18+ \n" +
                "Заработок возможен с любых устройств\n" +
                "От 175\$ в день\n" +
                "Пишите + в ЛС";
        assertThat(SpamAnalysis().isSpam(text)).isTrue()
    }

    @Test
    fun isSpam2() {
        val text = "Зaрaбoтoк oт нескoльких тысяч рублей кaждый день. Рaбoтa удaлённo, легaльнo, " +
                "без oпытa, нет влoжений. Нужнo выпoлнять не слoжные зaдaния в бoте в TG\n" +
                "\n" +
                "Узнaть детaли: нaйдите в пoиске rabota_382";
        assertThat(SpamAnalysis().isSpam(text)).isTrue()
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
                "\uD83D\uDC8EХочешь к нам? Пиши мне \uD83D\uDC49 @glass_koln";
        assertThat(SpamAnalysis().isSpam(text)).isTrue()
    }

    @Test
    fun isSpam4() {
        val text = "Приглашаю желающих освоить возможности хорошего дополнительного заработка на удаленке. " +
                "Опыт не обязателен, важны лишь желание и возраст от 20 лет. Детали в личных сообщениях.";
        assertThat(SpamAnalysis().isSpam(text)).isTrue()
    }

    @Test
    fun isSpam5() {
        val text = "\uD83D\uDC4BПредлагаю возможность стабильного и удалённого зароботка!\n" +
                "\uD83D\uDCB5Поднимаем каждый день более 200\$\n" +
                "\n" +
                "\uD83D\uDD36  Не имеет значения, есть ли у вас опыт, мы научим БЕСПЛАТНО!\n" +
                "✋\uD83C\uDFFB18+\n" +
                "\n" +
                "\uD83D\uDCE8\uD83D\uDCACИнтересует? Пиши! \uD83D\uDC49 @helix_oka";
        assertThat(SpamAnalysis().isSpam(text)).isTrue()
    }

    @Test
    fun isSpam6() {
        val text = "Здравствуйте. \n" +
                "Мы набираем партнёров в команду для взаимовыгодного сотрудничества.\n" +
                "Возраст от 20-ти лет, неполная занятость, ежедневный доход.\n" +
                "Заинтересованных жду в л.с.";
        assertThat(SpamAnalysis().isSpam(text)).isTrue()
    }

    @Test
    fun isSpam7() {
        val text = "НУЖНЫ ЛЮДИ ДЛЯ УДАЛЕННОЙ РАБОТЫ\n" +
                "Возраст от 18+ \n" +
                "Доход от \$200 в день \n" +
                "1-2 часа времени в день\n" +
                "Пиши в ЛС";
        assertThat(SpamAnalysis().isSpam(text)).isTrue()
    }

    @Test
    fun isSpam8() {
        val text = "Добрый день. Извиняюсь что не по теме, может кто то трейдингом занимается? " +
                "Хочу поделиться полезным материалом по трейдингу) " +
                "Вдруг кому то будет интересно, пишите в личку)"
        assertThat(SpamAnalysis().isSpam(text)).isTrue()
    }

    @Test
    fun isSpam9() {
        val text = "Ищу людей, возьму 2-3 человека 18+ \n" +
                "Удаленная деятельность \n" +
                "От 250\$  в  день\n" +
                "Кому интересно: Пишите + в лс"
        assertThat(SpamAnalysis().isSpam(text)).isTrue()
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
        assertThat(SpamAnalysis().isSpam(text)).isTrue()
    }

    @Test
    fun isSpam11() {
        val text = "\uD83D\uDCF2 Рaбoтa - oт 5 тыс. р ежедневнo. Рaбoтa пo интернет, " +
                "без нaрушения зaкoнa, oпыт не требуется, без влoжений. " +
                "Нужны люди, чтoбы делaть лёгкие зaдaния в бoте в TG\n" +
                "\n" +
                "Пoлучить инфoрмaцию: нaйдите в пoиске rabota_382"
        assertThat(SpamAnalysis().isSpam(text)).isTrue()
    }
}