package com.novikova.opencv

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_load_image.*
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs

class LoadImageActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_image)
        loadImageToMatrix()
    }

    private fun loadImageToMatrix() {
        var matrix = Mat()
        val input = assets.open("247220_abstract-hdtv-wallpapers_1920x1080_h.jpg")

        val bytes = input.readBytes()
        val matWithBytes = MatOfByte(*bytes)

        matrix = Imgcodecs.imdecode(matWithBytes, Imgcodecs.IMREAD_COLOR)

        if (matrix.empty()) {
            Toast.makeText(
                this,
                "Не удалось загрузить изображение в матрицу!",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        size.text = "${matrix.rows()} * ${matrix.cols()}"
        matrixValues.text = "${matrix.channels()}"
        sizeText2.text = "${matrix.width()} * ${matrix.height()}"
    }
}