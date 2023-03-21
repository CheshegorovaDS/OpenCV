package com.novikova.opencv.common

import com.novikova.opencv.ResizeImageActivity
import kotlinx.android.synthetic.main.activity_affine_deformation.*
import org.opencv.core.Mat

//private fun copyMat(src: Mat, img: Mat) {
//    val eyeRect = getEyeRectangle(src,
//        ResizeImageActivity.FIRST_POINT_X,
//        ResizeImageActivity.FIRST_POINT_Y,
//        ResizeImageActivity.RADIUS_CIRCLE
//    )
//
//    val top = (ResizeImageActivity.FIRST_POINT_X - ResizeImageActivity.RADIUS_CIRCLE).toInt()
//    val bottom = (ResizeImageActivity.FIRST_POINT_X + ResizeImageActivity.RADIUS_CIRCLE).toInt()
//    val right = (ResizeImageActivity.FIRST_POINT_Y + ResizeImageActivity.RADIUS_CIRCLE).toInt()
//    val left = (ResizeImageActivity.FIRST_POINT_Y - ResizeImageActivity.RADIUS_CIRCLE).toInt()
//
//    val mat = getSrcForCopy(
//        eyeRect,
//        img,
//        top,
//        bottom,
//        left,
//        right
//    )
//
//    val mask = getMask(
//        src,
//        top,
//        bottom,
//        left,
//        right
//    )
//
//    mat.copyTo(img, mask)
//    openMatrix(img, originalImage)
//}


//private fun getSrcForCopy(src: Mat, dst: Mat, top: Int, bottom: Int, left: Int, right: Int): Mat {
//    if (dst.channels() != 3) {
//        throw Exception("The count of channels is wrong.")
//    }
//    val mat = Mat(dst.rows(), dst.cols(), dst.type())
//
//    for (i in left..right) {
//        for (j in top..bottom) {
//            val arr = src.get(i - left, j - top)
//            if (arr != null) {
//                mat.put(i, j, arr[0], arr[1], arr[2])
//            }
//        }
//    }
//
//    return mat
//}