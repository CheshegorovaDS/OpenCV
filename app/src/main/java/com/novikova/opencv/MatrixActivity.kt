package com.novikova.opencv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_matrix.*
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.core.Size

class MatrixActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matrix)
        createMatrix()
        next.setOnClickListener { nextActivity() }
    }

    private fun createMatrix() {
//        Simple Matrix
        //count rows, count columns, elem
        val mat =  Mat.ones(2, 3, CvType.CV_8S)
        simpleMatrix.text = mat.dump()

//        Matrix with Scalar

        //Size(column, rows)

        //CvType.CV_[8,16,32,64 - количество бит на 1 канал]
        //          [S - Int со знаком, U - Int без знака, F - вещ.число]
        //          C[1,2,3,4 - кол-во каналов]

        val mat2 = Mat(Size(2.0,3.0), CvType.CV_8SC2 , Scalar(2.0, 3.0))
        val scalar = mat2.get(1, 1)
        matrixWithScalar.text = mat2.dump()
    }

    private fun nextActivity(){
        val intent = Intent(this, LoadImageActivity::class.java)
        startActivity(intent)
    }
}
