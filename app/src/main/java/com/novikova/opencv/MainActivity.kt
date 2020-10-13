package com.novikova.opencv

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.core.Core


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App()
        setContentView(R.layout.activity_main)
        writeVersion()
        next.setOnClickListener { nextActivity() }
    }

    private fun writeVersion() {
        version.text = Core.VERSION
    }

    private fun nextActivity(){
        val intent = Intent(this, ContourActivity::class.java)
        startActivity(intent)
    }
}
