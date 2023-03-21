package com.novikova.opencv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.novikova.opencv.common.*
import kotlinx.android.synthetic.main.activity_affine_deformation.originalImage
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class ResizeImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resize_image)

        val src = getMatrixFromImage()
        val matrixWithPoints = paintControlPoints(src)

//        openMatrix(matrixWithPoints, originalImage)
        openOtherImage(matrixWithPoints)
    }

    private fun openOtherImage(src: Mat) {
        //get cut rectangle from origin image
        val topEyeRect = getTopEyeRectangle(src, FIRST_POINT_X, FIRST_POINT_Y, RADIUS_CIRCLE, RADIUS_FACE)
        val eyeRect = getEyeRectangle(src, FIRST_POINT_X, FIRST_POINT_Y, RADIUS_CIRCLE)
        val bottomEyeRect = getBottomEyeRectangle(src, FIRST_POINT_X, FIRST_POINT_Y, RADIUS_CIRCLE, RADIUS_FACE)

        //get resized rectangles
        val newEyeHeight = RADIUS_CIRCLE
        val decreaseEyeRect = getResizeMat(eyeRect, newEyeHeight.toDouble())

        val increaseHeight = topEyeRect.height() + (newEyeHeight / 2)
        val increaseTopEyeRect = getResizeMat(topEyeRect, increaseHeight.toDouble())
        val increaseBottomEyeRect = getResizeMat(bottomEyeRect, increaseHeight.toDouble())

        //save resized rectangles to src matrix for copy
        val matrix = Mat(src.rows(), src.cols(), src.type())
        saveEyeRectangles(
            matrix,
            increaseTopEyeRect,
            decreaseEyeRect,
            increaseBottomEyeRect,
            (FIRST_POINT_X - RADIUS_CIRCLE).toInt(),
            (FIRST_POINT_Y - RADIUS_FACE).toInt()
        )

        val mask = getMask(
            src,
            (FIRST_POINT_X - RADIUS_CIRCLE).toInt(),
            (FIRST_POINT_X + RADIUS_CIRCLE).toInt(),
            (FIRST_POINT_Y - RADIUS_FACE).toInt(),
            (FIRST_POINT_Y + RADIUS_FACE).toInt()
        )

        copyMatFromTo(matrix, src, mask)

        openMatrix(src, originalImage)
    }

    private fun saveEyeRectangles(
        dst: Mat,
        topImage: Mat,
        centerImage: Mat,
        bottomImage: Mat,
        firstRow: Int,
        firstCol: Int
    ) {
        var left = firstRow
        var right = left + topImage.cols()
        var top = firstCol
        var bottom = top + topImage.rows()

        for (i in top..bottom) {
            for (j in left..right) {
                val arr = topImage.get(i - top, j - left)
                if (arr != null) {
                    dst.put(i, j, arr[0], arr[1], arr[2])
                }
            }
        }

        top = bottom
        bottom = top + centerImage.rows()

        for (i in top..bottom) {
            for (j in left..right + 1) {
                val arr = centerImage.get(i - top, j - left)
                if (arr != null) {
                    dst.put(i, j, arr[0], arr[1], arr[2])
                }
            }
        }

        top = bottom
        bottom = top + bottomImage.rows()

        for (i in top..bottom) {
            for (j in left..right) {
                val arr = bottomImage.get(i - top, j - left)
                if (arr != null) {
                    dst.put(i, j, arr[0], arr[1], arr[2])
                }
            }
        }

    }

    private fun copyMatFromTo(from: Mat, to: Mat, mask: Mat) {
        from.copyTo(to, mask)
//        openMatrix(to, originalImage)
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

    private fun getResizeMat(src: Mat, newHeight: Double): Mat {
        val dst = Mat()
        val newWidth = src.width().toDouble()
        Imgproc.resize(src, dst, Size(newWidth, newHeight))
        return dst
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


    companion object {
        const val NAME_IMAGE = "dog_2.jpg"
        const val FIRST_POINT_X = 630.0
        const val FIRST_POINT_Y = 510.0
        const val RADIUS_CIRCLE = 27
        const val RADIUS_FACE = 66
    }


}