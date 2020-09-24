package com.novikova.opencv

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.core.Core


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        writeVersion()
        next.setOnClickListener { nextActivity() }
    }

    private fun writeVersion() {
        version.text = Core.VERSION
    }

    private fun nextActivity(){
        val intent = Intent(this, MatrixActivity::class.java)
        startActivity(intent)
    }

//    override fun onResume() {
//        super.onResume()
//        //Вызываем асинхронный загрузчик библиотеки
//        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, loaderCallback)
//    }
//
//    private val loaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
//        override fun onManagerConnected(status: Int) {
//            when (status) {
//                LoaderCallbackInterface.SUCCESS -> nextActivity()
//                else -> super.onManagerConnected(status)
//            }
//        }
//    }
}
