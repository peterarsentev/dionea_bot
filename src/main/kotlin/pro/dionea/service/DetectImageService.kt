package pro.dionea.service

import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.tensorflow.SavedModelBundle
import org.tensorflow.Tensor
import org.tensorflow.Tensors
import java.awt.image.BufferedImage

@Service
class DetectImageService(@Value("\${model.dir}") modelDir: String) {
    private val model = SavedModelBundle.load(modelDir, "serve")

    fun detect(img: BufferedImage) : ImageCategory {
        return ImageCategory.DRAWINGS
        val imageArray = bufferedImageTo4DArray(img)
        val inputTensor = Tensors.create(imageArray)
        val result = model.session().runner()
            .feed("serving_default_input", inputTensor)
            .fetch("StatefulPartitionedCall:0")
            .run()[0] as Tensor<*>
        val probabilities = Array(1) { FloatArray(ImageCategory.entries.size) }
        result.copyTo(probabilities)
        result.close()
        val maxIndex = probabilities[0].indices.maxByOrNull { probabilities[0][it] } ?: -1
        return ImageCategory.entries[maxIndex]
    }

    @PreDestroy
    private fun destroy() {
        model.close()
    }

    private fun bufferedImageTo4DArray(img: BufferedImage): Array<Array<Array<FloatArray>>> {
        val width = 224
        val height = 224
        val resizedImg = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics = resizedImg.createGraphics()
        graphics.drawImage(img, 0, 0, width, height, null)
        graphics.dispose()

        val array = Array(1) { Array(height) { Array(width) { FloatArray(3) } } }
        for (y in 0 until height) {
            for (x in 0 until width) {
                val rgb = resizedImg.getRGB(x, y)
                array[0][y][x][0] = (rgb shr 16 and 0xFF) / 255.0f
                array[0][y][x][1] = (rgb shr 8 and 0xFF) / 255.0f
                array[0][y][x][2] = (rgb and 0xFF) / 255.0f
            }
        }
        return array
    }
}
