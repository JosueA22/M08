package com.example.test_on

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        setContentView(R.layout.activity_main)
    }

    override fun onStop() {
        super.onStop()
        setContentView(R.layout.activity_main)
    }
}



