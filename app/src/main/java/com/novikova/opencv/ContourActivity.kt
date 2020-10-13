package com.novikova.opencv

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_border.*
import org.opencv.android.Utils
import org.opencv.core.*
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
//        Параметр threshold1 задает  минимальное  пороговое  значение,
//        а  параметр threshold2  —  максимальное
        getImageMatrix()?.let {
            Imgproc.Canny(it, mat, 100.0, 100.0)
        }
        findContours(mat)
    }

    private fun findContours(mat: Mat) {
//        В первом параметре указывается исходное черно-белое изображение
//        (8 битов, один канал). (У нас это edgesCopy)
        val edgesCopy: Mat = mat.clone() // Создаем копию

//        сюда будет записываться список контуров
        val contours = ArrayList<MatOfPoint>()
//        Параметр hierarchy задает ссылку на матрицу,
//        в которую будет  записана  информация  об  уровне  вложенности  контура.
        val hierarchy = Mat()

//        Параметр mode задает режим поиска контуров
//        RETR_EXTERNAL — найти только крайние внешние контуры.
//        RETR_LIST  — найти все контуры без установления иерархии.
//        RETR_CCOMP — найти все контуры и организовать их в двухуровневую структуру.
//        RETR_TREE —  найти  все  контуры  и  организовать  полную  иерархию  вложенных контуров.
        val mode = Imgproc.RETR_TREE

//        Параметр method задает способ описания найденных контуров.
//        CHAIN_APPROX_NONE  — сохраняются все точки контура.
//        CHAIN_APPROX_SIMPLE —  прямая линия будет закодирована двумя точками.
//        CHAIN_APPROX_TC89_KCOS и CHAIN_APPROX_TC89_L1 — используется  алгоритм  TehChin.
        val method = Imgproc.CHAIN_APPROX_SIMPLE

        Imgproc.findContours(edgesCopy, contours, hierarchy,
            mode,
            method)

        drawContours(mat, contours, hierarchy)
    }

    private fun drawContours(
        mat: Mat,
        contours: ArrayList<MatOfPoint>,
        hierarchy: Mat
    ) {
        val color = Scalar(0.0, 0.0, 0.0)
        val matWithContours = Mat(mat.height(), mat.width(), CvType.CV_8UC3, Scalar(255.0, 255.0, 255.0))

        Imgproc.drawContours(matWithContours, contours, -1, color, 1, Imgproc.LINE_AA)

        val bitmapFromMatrix = Bitmap
            .createBitmap(
                matWithContours.cols(),
                matWithContours.rows(),
                Bitmap.Config.ARGB_8888
            )

        Utils.matToBitmap(matWithContours, bitmapFromMatrix)
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