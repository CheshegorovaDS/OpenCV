package com.novikova.opencv

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_draw_figure.*
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc


class DrawFigureActivity: AppCompatActivity() {
    private val white = Scalar(255.0,255.0,255.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_figure)
        openMatrix(line(), imageLine)
        openMatrix(rectangle(), imageRectangle)
        openMatrix(circle(), imageCircle)
        openMatrix(fillPoly(), imageFillPoly)
        next.setOnClickListener { nextActivity() }
    }

    private fun line(): Mat {
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

    // стрелки то же самое, но метод Imgproc.arrowedLine()
    //+ параметр: tipLength — длина стрелки относительно длины линии (значение по умолчанию: 0.1)

    private fun rectangle(): Mat {
        val mat = Mat(100, 100, CvType.CV_8UC3, white)

        val color = Scalar(0.0, 255.0, 0.0)

//        Если  в  параметре thickness
//        указать константу FILLED
//        из класса Core, то прямоугольник будет рисоваться
//        с заливкой без обводки.
        val thickness = 5   //толщина обводки
        val full = -1 //полная заливка
        val lineType = Imgproc.LINE_AA // сглаживание


        Imgproc.rectangle(
            mat,
            Point(10.0,10.0),
            Point(80.0, 80.0),
            color,
            full,
            lineType
        )

        return mat
    }

    private fun circle(): Mat {
        val mat = Mat(100, 100, CvType.CV_8UC3, white)

        val color = Scalar(0.0, 255.0, 0.0)
        val center = Point(50.0, 50.0)
        val radius = 50

        Imgproc.circle(
            mat,
            center,
            radius,
            color
        )

        return mat
    }

    // также можно нарисовать эллипс, дугу и сектор

    private fun fillPoly(): Mat {
        val mat = Mat(100, 100, CvType.CV_8UC3, white)

        val color = Scalar(0.0, 255.0, 0.0)

        val points = MatOfPoint(
            Point(0.0, 0.0),
            Point(100.0, 0.0),
            Point(50.0, 100.0)
        )
        val list = ArrayList<MatOfPoint>()
        list.add(points)

        val isClosed = true // замкнут?
        Imgproc.fillPoly(mat, list, color)

        return mat
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

    private fun nextActivity(){
        val intent = Intent(this, TextActivity::class.java)
        startActivity(intent)
    }
}