package com.example.led_controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView


class ViewAdapter(private val devices: ArrayList<DeviceInfo>, private val context: Context
): RecyclerView.Adapter<ViewAdapter.ViewHolder>() {

    private var controller = mutableListOf<DeviceController>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = devices[position].mName
        holder.txtIp.text = devices[position].mIP
        holder.onButton.setOnClickListener {on(position) }
        holder.offButton.setOnClickListener { off(position)}
        holder.itemView.setOnClickListener{
            openOptionMenu(position)

        }
        if (ipCheck(devices[position].mIP)) {
            controller.add(DeviceController())
            controller[position].start(devices[position].mName, devices[position].mIP)
        }
        val color = devices[position].getColor()
        holder.itemView.setBackgroundColor(color)

    }


    fun kill(){
        for (c in controller)
            c.close()
        println("DONE")

    }

    private fun ipCheck(ip:String):Boolean{
        if (Patterns.IP_ADDRESS.matcher(ip).matches())
            return true
        println("Invalid IP")
        return false
    }

    fun changeColor(){
        TODO("CHANGE THE COLOR OF THE BULBS")

    }

    private fun on(position: Int) {
        controller[position].turnOn()
    }

    private fun off(position: Int) {
        controller[position].turnOff()

    }

    override fun getItemCount(): Int {
        return devices.size
    }



    private fun openOptionMenu(position: Int){

        val intent = Intent(context, OptionsMenu::class.java)
        intent.putExtra("id", position)
        startActivityForResult(context as Activity, intent,1, Bundle())

    }



    fun update(){
        notifyDataSetChanged()
    }

    fun removeAll() {
        devices.clear()
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName: TextView = itemView.findViewById<TextView>(R.id.name_txt)
        val txtIp: TextView = itemView.findViewById<TextView>(R.id.ip_txt)
        val onButton: Button = itemView.findViewById(R.id.on_btn)
        val offButton: Button = itemView.findViewById(R.id.off_btn)

    }

}