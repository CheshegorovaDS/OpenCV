package com.novikova.opencv

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.novikova.opencv.common.getImageMatrix
import com.novikova.opencv.common.openMatrix
import com.novikova.opencv.common.paintCircles
import com.novikova.opencv.common.paintPoint
import kotlinx.android.synthetic.main.activity_affine_deformation.*
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.imgproc.Imgproc

class FindingCircleActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_affine_deformation)

        val src = getMatrixFromImage()
        val matrixWithPoints = paintControlPoints(src)

        openMatrix(matrixWithPoints, originalImage)
        openMatrixWithCircles(src)
    }

    private fun openMatrixWithCircles(src: Mat) {
        val circles = getCircles(src)

        val result = paintCircles(src, circles)
        openMatrix(result, deformationImage)
    }

    private fun getCircles(src: Mat): Mat {
        val greyMat = getGreyMat(src)
        val circles = Mat()

        Imgproc.HoughCircles(
            greyMat,
            circles,
            Imgproc.HOUGH_GRADIENT,
            1.0,
            (src.height()/2).toDouble()
        )
        Log.d("GetCircles: ", "${circles.width()}")
        return circles
    }

    private fun getGreyMat(src: Mat): Mat {
        val greyMat = Mat()
        Imgproc.cvtColor(src, greyMat, Imgproc.COLOR_BGR2GRAY)
        return greyMat
    }

    private fun getMatrixFromImage(): Mat {
        val input = assets.open(NAME_IMAGE)
        val bytes = input.readBytes()
        return getImageMatrix(this, bytes)!!
    }

    private fun paintControlPoints(src: Mat): Mat {
        return paintPoint(
            src,
            listOf(
                Point(FIRST_POINT_X, FIRST_POINT_Y)
//                Point(SECOND_POINT_X, SECOND_POINT_Y),
//                Point(THIRD_POINT_X, THIRD_POINT_Y)
            )
        )
    }

    companion object {
        const val NAME_IMAGE = "dog_2.jpg"
        const val FIRST_POINT_X = 630.0
        const val FIRST_POINT_Y = 510.0
        const val MIN_RADIUS = 10.0
    }

}
