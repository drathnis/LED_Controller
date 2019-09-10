package com.example.led_controller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask


open class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    private val timer = Timer()
    private var getDevices = GetDevices()


    private var deviceList: ArrayList<DeviceInfo>? = null


    private var handler: Handler?  =null
    private var adapter: ViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayoutManager = LinearLayoutManager(this)

        handler = MyHandler(WeakReference(MainActivity()))

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        deviceList = ArrayList()
        adapter = ViewAdapter(deviceList!!, this)

        recyclerView.adapter = adapter

        val dividerDrawable = ContextCompat.getDrawable(this, android.R.drawable.divider_horizontal_bright)

        recyclerView.addItemDecoration(DividerItemDecoration(dividerDrawable!!))

        getDevices.search()
        searchCheck()
        getDevices.sendDiscoverMessage()

        refresh_btn.setOnClickListener {

            getDevices.sendDiscoverMessage()

        }
    }

    override fun onResume() {

        super.onResume()
        adapter?.update()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode!=1)
            return
        if (resultCode == Activity.RESULT_OK){
            val id = data?.extras?.getInt("id")
            val color = data?.getIntExtra("color",0)
            deviceList?.get(id!!)?.setColor(color!!)

        }

        if (resultCode == Activity.RESULT_CANCELED){
            println("RESULT_CANCELED")
        }

        adapter?.update()

    }

    private fun updateList(){

        adapter?.removeAll()

        val list = getDevices.getDeviceList()

        for (d in list)
            deviceList?.add(DeviceInfo(separateName(d),separateIP(d)))

        adapter?.update()

    }

    private fun separateName(input: String):String{
        return input.substring(0, input.indexOf(":"))
    }

    private fun separateIP(input: String):String{
        return input.substring(input.lastIndexOf("/") + 1, input.lastIndexOf(":"))
    }

    private fun searchCheck(){

        val task = timerTask {
            if (getDevices.isSearching()) {
                //println("running")
                if (getDevices.newDataAvailable){
                    println("updating list")
                    handler!!.obtainMessage(1).sendToTarget()

                }
            }
            else
                println("not running")
        }
        timer.scheduleAtFixedRate(task,0,2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        adapter?.kill()
        println("ALL DONE!")

    }


    inner class MyHandler(private val mainActivity: WeakReference<MainActivity>) : Handler(){

        override fun handleMessage(msg: Message?) {
            if (msg != null) {
                if (msg.what==1) {
                    println("TESTING HANDLE INNER CLASS")
                    updateList()
                }
            }
        }
    }

}


