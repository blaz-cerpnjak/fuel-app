package com.blazc.fuelapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blazc.fuelapp.R
import com.blazc.fuelapp.helper.FuelUp

class FuelUpAdapter(private val context: Context,
                    listFuelUps: List<FuelUp>): RecyclerView.Adapter<FuelUpAdapter.ViewHolder>() {

    private var listFuelUps = listFuelUps

    private lateinit var myListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val txtOdometer = itemView.findViewById<TextView>(R.id.txtOdometer)
        val txtPrice = itemView.findViewById<TextView>(R.id.txtPrice)
        val txtConsumption = itemView.findViewById<TextView>(R.id.txtConsumption)
        val txtDate = itemView.findViewById<TextView>(R.id.txtDate)
        val imgFuelUp = itemView.findViewById<ImageView>(R.id.imgFuelUp)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fuelup_item, parent, false)
        return ViewHolder(view, myListener)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fuelUp: FuelUp = listFuelUps[position]

        holder.txtConsumption.text = fuelUp.avgConsumption.toString() + " L/100km"
        holder.txtOdometer.text = fuelUp.odometer.toString()
        holder.txtDate.text = fuelUp.formatInputDate()
        holder.txtPrice.text = "${fuelUp.calculateTotalAmount()} ${fuelUp.getCurrency()}"
        holder.imgFuelUp.setImageResource(R.drawable.fuel_up)
    }

    override fun getItemCount(): Int {
        return listFuelUps.size
    }
}