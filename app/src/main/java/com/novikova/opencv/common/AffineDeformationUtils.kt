package com.novikova.opencv.common

import org.opencv.core.*
import org.opencv.imgproc.Imgproc

const val RADIUS_POINT = 10

fun paintPoint(originalMat: Mat, points: List<Point>): Mat {
    val color = Scalar(0.0, 255.0, 0.0)

    for (point in points) {
        Imgproc.circle(
            originalMat,
            point,
            RADIUS_POINT,
            color,
            Core.FILLED
        )
    }

    return originalMat
}

fun paintCircles(originalMat: Mat, points: List<Point>, radius: Int): Mat {
    val color = Scalar(0.0, 255.0, 0.0)

    for (point in points) {
        Imgproc.circle(
            originalMat,
            point,
            radius,
            color,
            Core.FILLED
        )
    }

    return originalMat
}

fun paintCircles(originalMat: Mat, circles: Mat): Mat {
    val color = Scalar(0.0, 255.0, 0.0)

    if (circles.width() <= 0) return originalMat
    for (i in 0..circles.rows()) {
        for (j in 0..circles.cols()) {
            val circle = circles[i,j]
            Imgproc.circle(
                originalMat,
                Point(circle[0], circle[1]),
                circle[2].toInt(),
                color
            )
        }
    }
    return originalMat
}
