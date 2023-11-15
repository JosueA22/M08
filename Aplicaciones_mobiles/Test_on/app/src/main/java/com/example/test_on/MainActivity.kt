package com.example.test_on

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    fun callSecondactivity() {
        val intent = Intent(this,MainActivity2::class.java)
        startActivity(intent)
        intent.putExtra("key1","Andriod hola")
    }

    fun setWebButton() {
        val i = Intent(Intent.ACTION_VIEW,Uri.Parse("algo"))
        startActivity(i)
    }


    fun setCallButton() {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.Parse("telf"+ "12345678")
        startActivity(dialIntent)
    }

    fun setMessageButton() {
        val uri = Uri.Parse("xdd")
        val intent = Intent(Intent.ACTION_SENDTO,uri)
        dialIntent.data = Uri.Parse("telf"+ "12345678")
        startActivity(intent)
    }
}



