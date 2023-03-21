package com.novikova.opencv.common

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

fun loadImageToMatrix(context: Context, bytes: ByteArray, imageView: ImageView) {
    var matrix = Mat()

    val matWithBytes = MatOfByte(*bytes)
//      сохранило в BGR
    matrix = Imgcodecs.imdecode(matWithBytes, Imgcodecs.IMREAD_UNCHANGED)

    val w = matrix.width()
    val h = matrix.height()

    if (matrix.empty()) {
        Toast.makeText(
            context,
            "Не удалось загрузить изображение в матрицу!",
            Toast.LENGTH_LONG
        ).show()
        return
    }

    val matRGB = Mat()
//      преобразование BGR в RGB
    Imgproc.cvtColor(matrix, matRGB, Imgproc.COLOR_BGR2RGB)

    openMatrix(matRGB, imageView)
}

fun getImageMatrix(context: Context, bytes: ByteArray): Mat? {
    var matrix = Mat()

    val matWithBytes = MatOfByte(*bytes)
//      сохранило в BGR
    matrix = Imgcodecs.imdecode(matWithBytes, Imgcodecs.IMREAD_UNCHANGED)

    if (matrix.empty()) {
        Toast.makeText(
            context,
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

fun openMatrix(imageMatrix: Mat, imageView: ImageView) {
    val bitmapFromMatrix = Bitmap
        .createBitmap(
            imageMatrix.cols() ?: 0,
            imageMatrix.rows() ?: 0,
            Bitmap.Config.ARGB_8888
        )

    Utils.matToBitmap(imageMatrix, bitmapFromMatrix)
    imageView.setImageBitmap(bitmapFromMatrix)
    imageView.visibility = View.VISIBLE
}
