package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CustomAdapter(private val devices: ArrayList<DeviceList>, private val context: Context
): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)

        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = devices[position].name
        holder.txtIp.text = devices[position].ip
        holder.onButton.setOnClickListener { devices[position].turnOn() }
        holder.offButton.setOnClickListener { devices[position].turnOff() }
        holder.itemView.setOnClickListener{
            optionsOptions(position)
            updateUI(position)
        }
        holder.itemView.setBackgroundColor(devices[position].currentColor)
        println("Ran again?")

        println(devices[position].testInt)

    }
    override fun getItemCount(): Int {
        return devices.size
    }

    private fun optionsOptions(position: Int){

        val intent = Intent(context, OptionsMenu::class.java)
        intent.putExtra("device", devices[position])
        context.startActivity(intent)
    }

    fun updateUI(position: Int){
        devices[position].currentColor=Color.argb(100,55,5,50)
        notifyDataSetChanged()

    }
    fun changeBackgroundColor(){
        println("notifyDataSetChanged")
        notifyDataSetChanged()

    }

    fun getIP(id: Int):String{
        return devices[id].ip
    }

    fun removeItem(position: Int) {
        devices.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(item:DeviceList) {
        devices.add(item)
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