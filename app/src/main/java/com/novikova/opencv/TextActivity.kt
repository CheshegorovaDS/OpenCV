package com.novikova.opencv

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_text.*
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc


class TextActivity : AppCompatActivity(){
    private val resLayout = R.layout.activity_text

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(resLayout)
        createText()
    }

    private fun createText() {
        val white = Scalar(255.0, 255.0, 255.0)
        val black = Scalar(0.0, 0.0, 0.0)
        val matrix = Mat(200, 600, CvType.CV_8UC3, white)

        Imgproc.putText(
            matrix,
            "FONT_HERSHEY_SCRIPT_COMPLEX",
            Point(0.0, 100.0),
            Imgproc.FONT_HERSHEY_SCRIPT_COMPLEX,
            1.0,
            black
        )

        val bitmap = Bitmap.createBitmap(
            matrix.cols(),
            matrix.rows(),
            Bitmap.Config.ARGB_8888
        )

        Utils.matToBitmap(matrix, bitmap)
        text.setImageBitmap(bitmap)
        imageWithText(matrix)
    }

    private fun imageWithText(mat: Mat) {
        val img = getImageMatrix() ?: return

        var submat: Mat = img.submat(Rect(0, 0, mat.width(), mat.height()))
        mat.copyTo(submat)

        val bitmap = Bitmap.createBitmap(
            img.cols(),
            img.rows(),
            Bitmap.Config.ARGB_8888
        )

        Utils.matToBitmap(img, bitmap)
        imageWithText.setImageBitmap(bitmap)
        mat.release()
        img.release()
    }

    private fun getImageMatrix(): Mat? {
        var matrix = Mat()
        val input = assets.open("247220_abstract-hdtv-wallpapers_1920x1080_h.jpg")

        val bytes = input.readBytes()
        val matWithBytes = MatOfByte(*bytes)
//      сохранило в BGR
        matrix = Imgcodecs.imdecode(matWithBytes, Imgcodecs.IMREAD_UNCHANGED)

        if (matrix.empty()) {
            Toast.makeText(
                this,
                "Не удалось загрузить изображение в матрицу!",
                Toast.LENGTH_LONG
            ).show()
            return null
        }

        val matRGB = Mat()
//      преобразование BGR в RGB
        Imgproc.cvtColor(matrix, matRGB, Imgproc.COLOR_BGR2RGB)
        return matRGB
    }

    private fun nextActivity(){
        //val intent = Intent(this, ShowImagesActivity::class.java)
        startActivity(intent)
    }

}