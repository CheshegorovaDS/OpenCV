package com.novikova.opencv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.novikova.opencv.common.*
import kotlinx.android.synthetic.main.activity_affine_deformation.*
import kotlinx.android.synthetic.main.activity_affine_deformation.originalImage
import kotlinx.android.synthetic.main.activity_resize_image.*
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class ResizeImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resize_image)

        val src = getMatrixFromImage()
        val matrixWithPoints = paintControlPoints(src)

//        openMatrix(matrixWithPoints, originalImage)
//        openOtherImage(matrixWithPoints)
        copyMat(matrixWithPoints, src)
    }

    private fun openOtherImage(src: Mat) {
        //get cut rectangle from origin image
        val topEyeRect = getTopEyeRectangle(src, FIRST_POINT_X, FIRST_POINT_Y, RADIUS_CIRCLE, RADIUS_FACE)
        val eyeRect = getEyeRectangle(src, FIRST_POINT_X, FIRST_POINT_Y, RADIUS_CIRCLE)
        val bottomEyeRect = getBottomEyeRectangle(src, FIRST_POINT_X, FIRST_POINT_Y, RADIUS_CIRCLE, RADIUS_FACE)

        //get resized rectangles
        val newHeight = RADIUS_CIRCLE
        val decreaseEyeRect = getResizeMat(eyeRect, newHeight.toDouble())

        val increaseHeight = topEyeRect.height() + (RADIUS_CIRCLE / 2)
        val increaseTopEyeRect = getResizeMat(topEyeRect, increaseHeight.toDouble())
        val increaseBottomEyeRect = getResizeMat(bottomEyeRect, increaseHeight.toDouble())

        openMatrix(decreaseEyeRect, otherImage)
    }

    private fun getMatrixFromImage(): Mat {
        val input = assets.open(NAME_IMAGE)
        val bytes = input.readBytes()
        val src = getImageMatrix(this, bytes)!!
        return src
    }

    private fun paintControlPoints(src: Mat): Mat {
        val dst = Mat(src.rows(), src.cols(), src.type())
        src.copyTo(dst)
        return paintCircles(
            dst,
            listOf(Point(FIRST_POINT_X, FIRST_POINT_Y)),
            RADIUS_CIRCLE
        )
    }

    private fun copyMat(src: Mat, img: Mat) {
        val eyeRect = getEyeRectangle(src, FIRST_POINT_X, FIRST_POINT_Y, RADIUS_CIRCLE)

        val top = (FIRST_POINT_X - RADIUS_CIRCLE).toInt()
        val bottom = (FIRST_POINT_X + RADIUS_CIRCLE).toInt()
        val right = (FIRST_POINT_Y + RADIUS_CIRCLE).toInt()
        val left = (FIRST_POINT_Y - RADIUS_CIRCLE).toInt()

        val mat = getSrcForCopy(
            eyeRect,
            img,
            top,
            bottom,
            left,
            right
        )

        val mask = getMask(
            src,
            top,
            bottom,
            left,
            right
        )

        mat.copyTo(img, mask)
        openMatrix(img, originalImage)
    }

    private fun getSrcForCopy(src: Mat, dst: Mat, top: Int, bottom: Int, left: Int, right: Int): Mat {
        if (dst.channels() != 3) {
            throw Exception("The count of channels is wrong.")
        }
        val mat = Mat(dst.rows(), dst.cols(), dst.type())

        for (i in left..right) {
            for (j in top..bottom) {
                val arr = src.get(i - left, j - top)
                if (arr != null) {
                    mat.put(i, j, arr[0], arr[1], arr[2])
                }
            }
        }

        return mat
    }

    private fun getMask(dst: Mat, top: Int, bottom: Int, left: Int, right: Int): Mat {
        val mask = Mat(dst.rows(), dst.cols(), dst.type())

        for (i in left until right) {
            for (j in top until bottom) {
                mask.put(i, j, 1.0, 1.0, 1.0)
            }
        }

        return mask
    }

    private fun getTopEyeRectangle(src: Mat, x: Double, y: Double, radiusEye: Int, radiusFace: Int): Mat {
        return src.submat(
            (y - radiusFace).toInt(),
            (y - radiusEye).toInt(),
            (x - radiusEye).toInt(),
            (x + radiusEye).toInt()
        )
    }

    private fun getEyeRectangle(src: Mat, x: Double, y: Double, radius: Int): Mat {
        return src.submat(
            (y - radius).toInt(),
            (y + radius).toInt(),
            (x - radius).toInt(),
            (x + radius).toInt()
        )
    }

    private fun getBottomEyeRectangle(src: Mat, x: Double, y: Double, radiusEye: Int, radiusFace: Int): Mat {
        return src.submat(
            (x + radiusEye).toInt(),
            (x + radiusFace).toInt(),
            (y - radiusEye).toInt(),
            (y + radiusEye).toInt()
        )
    }

    private fun getIncreaseRatio() {
        val difference = RADIUS_CIRCLE / 2
        val newHeight = (2 * RADIUS_CIRCLE) - difference

    }

    private fun getResizeMat(src: Mat, newHeight: Double): Mat {
        val dst = Mat()
        val newWidth = src.width().toDouble()
        Imgproc.resize(src, dst, Size(newWidth, newHeight))
        return dst
    }

    companion object {
        const val NAME_IMAGE = "dog_2.jpg"
        const val FIRST_POINT_X = 630.0
        const val FIRST_POINT_Y = 510.0
        const val RADIUS_CIRCLE = 22
        const val RADIUS_FACE = 66
    }


}