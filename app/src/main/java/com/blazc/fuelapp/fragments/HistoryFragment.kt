package com.blazc.fuelapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blazc.fuelapp.R
import com.blazc.fuelapp.activities.MainActivity
import com.blazc.fuelapp.adapters.FuelUpAdapter
import com.blazc.fuelapp.databinding.FragmentHistoryBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var fuelUpAdapter: FuelUpAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        setBarChart()
    }

    //region Tasks Bar Chart
    private fun setBarChart() {
        val fuelUpsList = mainActivity.mydb.getCurrentYearFuelUps()
        val barList = ArrayList<BarEntry>()
        val barData: BarData

        for (i in 1..12) {
            var fuelAmount = 0f
            for (fuelUp in fuelUpsList) {
                if (fuelUp.localDate().monthValue == i) {
                    fuelAmount += fuelUp.fuelAmount
                }
            }
            barList.add(BarEntry(i.toFloat(), fuelAmount))
        }

        val lineDataSet = BarDataSet(barList, "Fuel amount")
        // set colors
        lineDataSet.color = mainActivity.getColor(R.color.blue)
        lineDataSet.valueTextColor = mainActivity.getColor(R.color.black)
        // set text size
        lineDataSet.valueTextSize = 15f
        // set bar data
        barData = BarData(lineDataSet)
        binding.barChart.data = barData
        // animate columns
        binding.barChart.animateY(1000)
        // hide description
        binding.barChart.description.isEnabled = false
        // customize xAxis
        val xAxis = binding.barChart.xAxis
        xAxis.setDrawGridLines(false) // remove grid lines
        xAxis.position = XAxis.XAxisPosition.BOTTOM // move labels to bottom
        xAxis.labelCount = 12 // set labels count to 12 for each month
        // customize left axis
        val leftAxis = binding.barChart.axisLeft
        leftAxis.setDrawGridLines(false)
        // customize right axis
        val rightAxis = binding.barChart.axisRight
        rightAxis.setDrawGridLines(false) // remove grid lines
        rightAxis.setDrawLabels(false) // remove right axis labels
    }
    //endregion
}