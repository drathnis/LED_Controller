package com.example.led_controller.backup

import android.graphics.Color
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket


@Parcelize
class DeviceList(val name: String, val ip: String): Parcelable {

    private var deviceName: String = name
  //  private var deviceIP: String = ip

    private val bulbPort = 55443
    private var mSocket: Socket? = null
    private var cmdRun = true
    private var mBos: BufferedOutputStream? = null
    private var mReader: BufferedReader? = null

    var currentColor =  Color.argb(100,0,0,50)

    var testInt = 0

    init {
        connect()

    }

    private fun write(cmd: String) {
        Thread(Runnable {
            if (mBos != null && mSocket!!.isConnected) {
                try {
                    mBos?.write(cmd.toByteArray())
                    mBos?.flush()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                Log.d("WRITE", "mBos = null or mSocket is closed")
            }
        }).start()
    }


    private fun connect() {
        Log.d("CONNECT", "Trying To Connect")
        Thread(Runnable {
            try {
                cmdRun = true
                mSocket = Socket(ip, bulbPort)
                mSocket?.keepAlive = true
                mBos = BufferedOutputStream(mSocket?.getOutputStream())
                mReader = BufferedReader(InputStreamReader(mSocket?.getInputStream()))

                while (cmdRun) {
                    try {
                        val value = mReader!!.readLine()
                        Log.d("READER", "value = " + value!!)
                    } catch (e: Exception) {
                        Log.e("Error", e.toString())
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }

    fun close() {
        try {
            cmdRun = false
            mSocket?.close()
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
    }


    fun turnOff() {
        println("Turning Off $deviceName")

        val test = getCMD(0,1,0)
        write(test)
        currentColor =  Color.argb(100,255,0,0)
    }

    fun turnOn() {
        println("Turning On $deviceName")

        val test = getCMD(1,1,0)
        write(test)
        currentColor =  Color.argb(100,0,0,255)

    }


    fun openOptionPage() {
        println("Open Option Page $deviceName")
    }



    private fun getCMD(cmd: Int, id: Int, mode: Int): String {

        val jsonData = JSONObject()
        val jsonArray = JSONArray()
        var method: String? = null
        val hsv = FloatArray(3)

        try {

            when (cmd) {
                0 -> {
                    jsonArray.put("off")
                    jsonArray.put("smooth")
                    jsonArray.put(500)
                    method = "set_power"
                }
                1 -> {
                    jsonArray.put("on")
                    jsonArray.put("smooth")
                    jsonArray.put(500)
                    method = "set_power"
                }
                2 -> method = "toggle"

                3->{

                }

            }
            jsonData.put("id", id)
            jsonData.put("method", method)
            jsonData.put("params", jsonArray)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toString() + "\r\n"

    }
}