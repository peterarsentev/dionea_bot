package pro.dionea.service

import net.sourceforge.tess4j.Tesseract
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage

@Service
class TextExtractionService(@Value("\${tessdata.dir}") private val tessdataDir: String) {

    private fun createTesseractInstance(): Tesseract {
        return Tesseract().apply {
            setDatapath(tessdataDir)
            setLanguage("rus")
        }
    }

    fun extract(img: BufferedImage): String {
        return createTesseractInstance().doOCR(img)
    }
}

