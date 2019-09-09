package com.example.myapplication

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

    private val mainHandler = MainHandler(WeakReference<MainActivity>(this))

    private val timer = Timer()
    private var getDevices = GetDevices()


    var testDevices: ArrayList<DeviceInfo>? = null

    var devices: ArrayList<DeviceList>? = null

    var adapter: CustomAdapter? = null

    var newAdapter: ViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayoutManager = LinearLayoutManager(this)

        val deviceNames: ArrayList<String> = ArrayList()
        val deviceIPs: ArrayList<String> = ArrayList()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        devices = ArrayList<DeviceList>()

        testDevices = ArrayList<DeviceInfo>()



        val dCount = 5

        for (d in 0..dCount){
            testDevices?.add(DeviceInfo("test #$d ","ipTest"))
        }

       //test adapter = CustomAdapter(devices!!, this)
        newAdapter = ViewAdapter(testDevices!!, this)

        //recyclerView.adapter = adapter

        recyclerView.adapter = newAdapter

        val dividerDrawable = ContextCompat.getDrawable(this, android.R.drawable.divider_horizontal_bright)

        recyclerView.addItemDecoration(DividerItemDecoration(dividerDrawable!!))

        getDevices.search()
        searchCheck()

        refresh_btn.setOnClickListener {

            getDevices.sendDiscoverMessage()

        }
    }

    override fun onResume() {

        super.onResume()
        newAdapter?.update()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode!=1)
            return
        if (resultCode == Activity.RESULT_OK){
            val id = data?.extras?.getInt("id")
            val color = data?.getIntExtra("color",0)
            testDevices?.get(id!!)?.setColor(color!!)

        }

        if (resultCode == Activity.RESULT_CANCELED){
            println("RESULT_CANCELED")
        }

        newAdapter?.update()

    }

    private fun updateList(){

        newAdapter?.removeAll()

        val list = getDevices.getDeviceList()

        for (d in list)
            testDevices?.add(DeviceInfo(separateName(d),separateIP(d)))
            //devices?.add(DeviceList(separateName(d),separateIP(d)))

        newAdapter?.update()

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
                    mHandler.obtainMessage(1).sendToTarget()
                }
            }
            else
                println("not running")
        }
        timer.scheduleAtFixedRate(task,0,2000)
    }

    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {

            if (msg.what==1){
                updateList()
            }
           // StopWatch.time.setText(formatIntoHHMMSS(elapsedTime)) //this is the textview
        }
    }

    class MainHandler(private val outerClass: WeakReference<MainActivity>) : Handler(){

        override fun handleMessage(msg: Message?) {
            if (msg != null) {
                if (msg.what==1) {
                    println("TESTING HANDLE")

                }
            }
        }
    }

}


