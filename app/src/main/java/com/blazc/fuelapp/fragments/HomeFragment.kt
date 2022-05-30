package com.blazc.fuelapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blazc.fuelapp.DatabaseHelper
import com.blazc.fuelapp.activities.MainActivity
import com.blazc.fuelapp.adapters.FuelUpAdapter
import com.blazc.fuelapp.databinding.FragmentHomeBinding
import com.blazc.fuelapp.helper.FuelUp
import kotlin.math.roundToInt

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var mydb: DatabaseHelper
    private lateinit var fuelUpAdapter: FuelUpAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        mydb = DatabaseHelper(mainActivity)
        setRecyclerViewFuelUps()
        binding.txtMonthlyExpenses.text = "${getCurrentMonthsExpenses()} ${mainActivity.getCurrency()}"
        binding.txtDistance.text = mydb.getAverageMonthlyDistance().toString()
        binding.txtAverageConsumption.text = mainActivity.totalAverageConsumption().toString()
        binding.txtAvgCostPerUnit.text = mainActivity.avgFuelCostPerUnit().toString()
    }

    fun getCurrentMonthsExpenses(): Float {
        val fuelUps = mydb.getCurrentMonthsFuelUps()
        if (fuelUps.isEmpty())
            return 0f
        var sum = 0f
        for (fuelUp in fuelUps) {
            sum += fuelUp.calculateTotalAmount()
        }
        return ((sum * 100.0).roundToInt() / 100.0).toFloat()
    }

    //region RecyclerView
    private fun setRecyclerViewFuelUps() {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        binding.recyclerViewFuelUps.layoutManager = linearLayoutManager
        val fuelUpList = getFuelUpList()
        fuelUpAdapter = FuelUpAdapter(mainActivity, fuelUpList)
        binding.recyclerViewFuelUps.adapter = fuelUpAdapter
        fuelUpAdapter.setOnItemClickListener(object : FuelUpAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                mainActivity.loadFragment(FuelUpFragment(fuelUpList[position].ID, true))
            }
        })
    }

    private fun getFuelUpList(): List<FuelUp> {
        return mainActivity.mydb.getFuelUps()
    }
    //endregion
}