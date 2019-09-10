package com.example.led_controller

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*

class GetDevices {

    private var listOfBulbs = mutableListOf<String>()
    private val bulbInfo = HashMap<String, String>()

    private val mainSocket = DatagramSocket()

    private var search = false
    private var bulbCount = 0
    var newDataAvailable = false


    fun isSearching():Boolean{
        return search
    }

    fun search() {

        if (!search){
            search = true
            discover()
        }
    }

    fun getDeviceList(): MutableList<String> {
        newDataAvailable = false
        return listOfBulbs
    }

    private fun processData(info: Array<String>) {
        newDataAvailable = true
        for (str in info) {
            val index = str.indexOf(":")
            if (index == -1) {
                continue
            }
            val title = str.substring(0, index)
            val value = str.substring(index + 1)
            bulbInfo[title] = value
        }

        if (!listOfBulbs.contains(bulbInfo["Location"].toString())){
            listOfBulbs.add(bulbInfo["Location"].toString())
            bulbCount++
        }
        //println(listOfBulbs);
    }

    private fun clearList(){
        listOfBulbs.clear()

    }
    fun sendDiscoverMessage(){
        clearList()
        GlobalScope.launch {
            println("Searching")
            try {
                val dpSend = DatagramPacket(
                    get_message.toByteArray(),
                    get_message.toByteArray().size, InetAddress.getByName(UDP_HOST),
                    UDP_PORT
                )
                mainSocket.send(dpSend)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun discover(){
        GlobalScope.launch {
            try {
                if (!search){
                    println("Please Enable Search")
                }
                while (search) {
                    val buf = ByteArray(1024)
                    val dpRecv = DatagramPacket(buf, buf.size)
                    mainSocket.receive(dpRecv)
                    val bytes = dpRecv.data
                    val buffer = StringBuffer()

                    for (i in 0 until dpRecv.length) {
                        // parse /r
                        if (bytes[i].toInt() == 13) {
                            continue
                        }
                        buffer.append(bytes[i].toChar())
                    }
                    val info = buffer.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    println(buffer.toString())

                    processData(info)

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


    }

    companion object {

        private const val UDP_HOST = "239.255.255.250"
        private const val UDP_PORT = 1982
        private const val get_message = "M-SEARCH * HTTP/1.1\r\n" +
                "HOST:239.255.255.250:1982\r\n" +
                "MAN:\"ssdp:discover\"\r\n" +
                "ST:wifi_bulb\r\n"
    }
}
