package com.novikova.opencv

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_show_image.*
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

class ShowImagesActivity: AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)
        unchanged.setOnClickListener(this)
        colored.setOnClickListener(this)
        grayscale.setOnClickListener(this)
        next.setOnClickListener{ nextActivity() }
    }

    override fun onClick(v: View?) {
        val code = when (v) {
            unchanged -> {
                codeImage.text = "UNCHANGED"
                Imgcodecs.IMREAD_UNCHANGED
            }

            colored -> {
                codeImage.text = "COLOR"
                Imgcodecs.IMREAD_COLOR
            }

            grayscale -> {
                codeImage.text = "GRAYSCALE"
                Imgcodecs.IMREAD_GRAYSCALE
            }

            else -> null
        }
        code?.let { loadImageToMatrix(it) }
    }

    private fun loadImageToMatrix(code: Int) {
        var matrix = Mat()
        val input = assets.open("247220_abstract-hdtv-wallpapers_1920x1080_h.jpg")

        val bytes = input.readBytes()
        val matWithBytes = MatOfByte(*bytes)
//      сохранило в BGR
        matrix = Imgcodecs.imdecode(matWithBytes, code)

        if (matrix.empty()) {
            Toast.makeText(
                this,
                "Не удалось загрузить изображение в матрицу!",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val matRGB = Mat()
//      преобразование BGR в RGB
        Imgproc.cvtColor(matrix, matRGB, Imgproc.COLOR_BGR2RGB)

        openMatrix(matRGB)
    }

    private fun openMatrix(imageMatrix: Mat) {
        val bitmapFromMatrix = Bitmap
            .createBitmap(
                imageMatrix.cols() ?: 0,
                imageMatrix.rows() ?: 0,
                Bitmap.Config.ARGB_8888
            )

        Utils.matToBitmap(imageMatrix, bitmapFromMatrix)
        imageView.setImageBitmap(bitmapFromMatrix)
        imageView.visibility = View.VISIBLE
        codeImage.visibility = View.VISIBLE
    }

    private fun nextActivity(){
        val intent = Intent(this, DrawFigureActivity::class.java)
        startActivity(intent)
    }

}