package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.options_menu.*


class OptionsMenu : AppCompatActivity(){


    private var color = Color.WHITE
    var id:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.options_menu)


        id = intent!!.extras!!.getInt("id")

        toggle_btn.setOnClickListener {
            println("Temp... 1")
            color = Color.RED
            done()
        }



    }

    private fun done(){
        val intent = Intent()
        intent.putExtra("id", id)
        intent.putExtra("color", color)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent()
        intent.putExtra("id", id)
        intent.putExtra("color", color)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}