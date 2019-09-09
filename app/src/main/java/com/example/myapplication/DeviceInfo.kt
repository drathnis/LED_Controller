package com.example.myapplication

import android.graphics.Color
import java.io.Serializable

class DeviceInfo(name: String, ip: String) : Serializable{

    var mName = name
    var mIP = ip

    private var color = Color.WHITE

    fun getColor(): Int {
        return color
    }

    fun setColor(color: Int){
        this.color = color
    }

    fun getName(): String {
        return mName
    }

    fun setName(name: String){
        this.mName = name
    }

    fun geIP(): String {
        return this.mIP
    }

    fun setIP(ip: String){
        this.mIP = ip
    }

}