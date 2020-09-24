package com.novikova.opencv

import android.app.Application

class App: Application() {
    init {
        System.loadLibrary("opencv_java4")
    }
}