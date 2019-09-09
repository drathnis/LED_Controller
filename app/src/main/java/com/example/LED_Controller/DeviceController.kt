package com.example.LED_Controller


import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket


class DeviceController(){


    private val bulbPort = 55443
    private var thisSocket: Socket? =  null
    private var cmdRun = true
    private var mBos: BufferedOutputStream? = null
    private var mReader: BufferedReader? = null
    private var mName:String? = null
    private var mIP:String? = null

    fun setName(name: String){
       mName = name
    }
    fun setIP(ip: String){
        mIP = ip
    }

    fun start(name: String? = null, ip: String? = null){
        mName = name
        mIP = ip

        if (mName!=null && mIP!=null){
            println("connect")
            connect()
        }
        else{
            println("please provide name and ip")
        }
    }

    private fun write(cmd: String) {
        Thread(Runnable {
            if (mBos != null && thisSocket!!.isConnected) {
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
      //  Log.d("CONNECT", "Trying To Connect")
        Log.d("CONNECT", "Trying To Connect to $mIP")
        Thread(Runnable {
            try {
                cmdRun = true
                var mSocket = Socket(mIP, bulbPort)
                mSocket.keepAlive = true
                mBos = BufferedOutputStream(mSocket.getOutputStream())
                mReader = BufferedReader(InputStreamReader(mSocket.getInputStream()))
                thisSocket = mSocket
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
            thisSocket?.close()
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
    }


    fun turnOff() {
        println("Turning Off $mName")

        val test = getCMD(0,1,0)
        write(test)
    }

    fun turnOn() {
        println("Turning On $mName")

        val test = getCMD(1,1,0)
        write(test)
    }


    fun openOptionPage() {
        println("Open Option Page $mName")
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

