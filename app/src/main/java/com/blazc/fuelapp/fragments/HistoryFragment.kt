package com.blazc.fuelapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.blazc.fuelapp.activities.MainActivity
import com.blazc.fuelapp.adapters.FuelUpAdapter
import com.blazc.fuelapp.databinding.FragmentHistoryBinding
import com.blazc.fuelapp.helper.FuelUp

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
        setRecyclerViewFuelUps()
    }

    private fun setRecyclerViewFuelUps() {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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
}