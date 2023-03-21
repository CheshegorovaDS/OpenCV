package com.novikova.opencv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.novikova.opencv.common.getImageMatrix
import com.novikova.opencv.common.openMatrix
import com.novikova.opencv.common.paintPoint
import kotlinx.android.synthetic.main.activity_affine_deformation.*
import org.opencv.calib3d.Calib3d
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class AffineDeformationActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_affine_deformation)

        val matrixWithPoints = paintControlPoints(getMatrixFromImage())

        openMatrix(matrixWithPoints, originalImage)
        openDeformationMatrix(matrixWithPoints)
    }

    private fun getMatrixFromImage(): Mat {
        val input = assets.open(NAME_IMAGE)
        val bytes = input.readBytes()
        val src = getImageMatrix(this, bytes)!!
        return src
    }

    private fun paintControlPoints(src: Mat): Mat {
        return paintPoint(
            src,
            listOf(
                Point(FIRST_POINT_X, FIRST_POINT_Y),
                Point(SECOND_POINT_X, SECOND_POINT_Y),
                Point(THIRD_POINT_X, THIRD_POINT_Y)
            )
        )
    }

    private fun openDeformationMatrix(src: Mat) {
        openMatrix(getTransformationMatrix(src), deformationImage)
    }

    private fun getTransformationMatrix(image: Mat): Mat {

        val deformationMat = Mat()

        Imgproc.warpAffine(
            image,
            deformationMat,
            getAffineTransformationMatrixBy3Points(),
            Size(image.width().toDouble(), image.height().toDouble())
        )
        return deformationMat
    }

    private fun getAffineTransformationMatrixBy3Points(): Mat? {
        val srcPoints = MatOfPoint2f(
            Point(FIRST_POINT_X, FIRST_POINT_Y),
            Point(SECOND_POINT_X, SECOND_POINT_Y),
            Point(THIRD_POINT_X, THIRD_POINT_Y)
        )
        val dstPoints = MatOfPoint2f(
            Point(300.0, 350.0),
            Point(750.0, 400.0),
            Point(450.0, 410.0)
        )
        val affineTransformMat = Imgproc.getAffineTransform(srcPoints, dstPoints)
        return affineTransformMat
    }

    private fun getTransformationMatrix2(image: Mat): Mat {

        val deformationMat = Mat()

        Imgproc.warpPerspective(
            image,
            deformationMat,
            getAffineTransformationMatrixByPoints(image.width(), image.height()),
            Size(image.width().toDouble(), image.height().toDouble())
        )
        return deformationMat
    }

    private fun getAffineTransformationMatrixByPoints(width: Int, height: Int): Mat? {
        val srcPoints = MatOfPoint2f(
//            Point(0.0, 0.0),
//            Point(0.0, height.toDouble()),
            Point(width.toDouble(), 0.0),
//            Point(width.toDouble(), height.toDouble()),
            Point(FIRST_POINT_X, FIRST_POINT_Y),
            Point(SECOND_POINT_X, SECOND_POINT_Y),
            Point(THIRD_POINT_X, THIRD_POINT_Y)
        )
        val dstPoints = MatOfPoint2f(
//            Point(0.0, 0.0),
//            Point(0.0, height.toDouble()),
            Point(width.toDouble(), 0.0),
//            Point(width.toDouble(), height.toDouble()),
            Point(300.0, 350.0),
            Point(750.0, 400.0),
            Point(450.0, 410.0)
        )
        val affineTransformMat = Calib3d.findHomography(srcPoints, dstPoints)

        return affineTransformMat
    }

    companion object {
        const val NAME_IMAGE = "cat_eye.jpg"
        const val FIRST_POINT_X = 300.0
        const val FIRST_POINT_Y = 95.0
        const val SECOND_POINT_X = 750.0
        const val SECOND_POINT_Y = 500.0
        const val THIRD_POINT_X = 450.0
        const val THIRD_POINT_Y = 510.0
    }

}