package com.novikova.opencv

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_border.*
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

class ContourActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_border)

        searchBorder()
        openImageGrey()
    }
    //граница - резкий перепад яркости

    private fun searchBorder() {
        val mat = Mat()
        getImageMatrix()?.let {
            Imgproc.Canny(it, mat, 100.0, 100.0)
        }

        val bitmapFromMatrix = Bitmap
            .createBitmap(
                mat.cols() ?: 0,
                mat.rows() ?: 0,
                Bitmap.Config.ARGB_8888
            )

        Utils.matToBitmap(mat, bitmapFromMatrix)
        imageWithBorder.setImageBitmap(bitmapFromMatrix)
    }

    private fun getImageMatrix(): Mat? {
        var matrix = Mat()
        val input = assets.open("wallpaper2you_184772.jpg")

        val bytes = input.readBytes()
        val matWithBytes = MatOfByte(*bytes)
//      сохранило в BGR
        matrix = Imgcodecs.imdecode(matWithBytes, Imgcodecs.IMREAD_GRAYSCALE)

        if (matrix.empty()) {
            Toast.makeText(
                this,
                "Не удалось загрузить изображение в матрицу!",
                Toast.LENGTH_LONG
            ).show()
            return null
        }
        return matrix
    }

    private fun openImageGrey() {
        val imageMatrix = getImageMatrix()
        val bitmapFromMatrix = Bitmap
            .createBitmap(
                imageMatrix?.cols() ?: 0,
                imageMatrix?.rows() ?: 0,
                Bitmap.Config.ARGB_8888
            )

        Utils.matToBitmap(imageMatrix, bitmapFromMatrix)
        greyImage.setImageBitmap(bitmapFromMatrix)
    }
}