package com.novikova.opencv

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_draw_figure.*
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

class DrawFigureActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_figure)
        openMatrix(line(), imageLine)
    }

    private fun line(): Mat {
        val white = Scalar(255.0,255.0,255.0)
        val colorLine = Scalar(0.0, 255.0, 0.0)
        val mat = Mat(100, 100, CvType.CV_8UC3, white)

        val thickness = 5   //толщина
        val type = Imgproc.LINE_AA //сглаживание
        val shift = 1   //сдвиг

        Imgproc.line(
            mat,
            Point(0.0,0.0),
            Point(150.0, 150.0),
            colorLine,
            thickness,
            type,
            shift
        )
        
        return  mat
    }

    private fun openMatrix(imageMatrix: Mat, imageView: ImageView) {
        val bitmapFromMatrix = Bitmap
            .createBitmap(
                imageMatrix.cols() ?: 0,
                imageMatrix.rows() ?: 0,
                Bitmap.Config.ARGB_8888
            )

        Utils.matToBitmap(imageMatrix, bitmapFromMatrix)
        imageView.setImageBitmap(bitmapFromMatrix)
    }
}