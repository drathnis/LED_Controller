package com.example.led_controller

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mcxiaoke.koi.ext.toast
import kotlinx.android.synthetic.main.options_menu.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.ImageView
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.view.MotionEvent
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.mcxiaoke.koi.ext.onTouchEvent
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T






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

        colorWheel.onTouchEvent() {
                view: View, motionEvent: MotionEvent ->
            val x = motionEvent.x
            val y = motionEvent.y

            println("y=$y x=$x")
            getColor(x,y)

            true
        }


//        colorWheel.setOnClickListener() {
//            getColor()
//            toast("OUCH") }


    }



//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        val x = event.x.toInt()
//        val y = event.y.toInt()
//
//        when (event.action) {
//
//        }
//       // println("y=$y x=$x")
//        return false
//    }

    private fun getColor(x: Float,y: Float){

        val imgDrawable = (colorWheel as ImageView).drawable
        //imgDrawable will not be null if you had set src to ImageView, in case of background drawable it will be null
        val bitmap = (imgDrawable as BitmapDrawable).bitmap


        val inverse = Matrix()
        (colorWheel as ImageView).imageMatrix.invert(inverse)
        val touchPoint = floatArrayOf(x, y)
        inverse.mapPoints(touchPoint)
        val xCoord = touchPoint[0].toInt()
        val yCoord = touchPoint[1].toInt()

        val touchedRGB = bitmap.getPixel(xCoord, yCoord)

        val redValue = Color.red(touchedRGB)
        val greenValue = Color.green(touchedRGB)
        val blueValue = Color.blue(touchedRGB)
        val alphaValue = Color.alpha(touchedRGB)

        color = Color.argb(alphaValue,redValue,greenValue,blueValue)
        done()

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


private fun ImageView.onTouchEvent(event: MotionEvent) {
    val x = event.x.toInt()
    val y = event.y.toInt()

    when (event.action) {

    }
    println("y=$y x=$x")
}
