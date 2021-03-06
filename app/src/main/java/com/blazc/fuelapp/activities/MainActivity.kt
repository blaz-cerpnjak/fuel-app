package com.blazc.fuelapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.blazc.fuelapp.DatabaseHelper
import com.blazc.fuelapp.R
import com.blazc.fuelapp.databinding.ActivityMainBinding
import com.blazc.fuelapp.fragments.FuelUpFragment
import com.blazc.fuelapp.fragments.HistoryFragment
import com.blazc.fuelapp.fragments.HomeFragment
import com.blazc.fuelapp.helper.FuelUp
import io.ak1.OnBubbleClickListener
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var mydb: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mydb = DatabaseHelper(this)
        setBottomNavigation()
        setSelectedMenu(0)
    }

    //region Bottom Navigation and Fragments
    private fun setBottomNavigation() {
        binding.bottomNavigation.addBubbleListener(OnBubbleClickListener { id ->
            var fragment: Fragment? = null
            when (id) {
                R.id.home -> fragment = HomeFragment()
                R.id.fuelUp -> fragment = FuelUpFragment()
                R.id.history -> fragment = HistoryFragment()
            }
            if (fragment != null) {
                loadFragment(fragment)
            }
        })
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    fun setSelectedMenu(id: Int) {
        binding.bottomNavigation.setSelected(id)
    }
    //endregion

    fun getCurrency(): String {
        return Currency.getInstance(Locale.getDefault()).getSymbol(Locale.getDefault())
    }

    //region Calculations
    fun totalAverageConsumption(): Float {
        val fuelUps = mydb.getFuelUps().sortedByDescending { it.ID }

        if (fuelUps.size < 2)
            return 0f

        var counter = 1
        var fuelAmount = 0f
        var lastOdometer = fuelUps[0].odometer
        var drivenDistance = 0
        var partialConsumption = 0f

        for (i in 1 until fuelUps.size) {
            if (fuelUps[i].isPartial != 1) {
                counter++
                fuelAmount += fuelUps[i].fuelAmount
                drivenDistance = lastOdometer - fuelUps[i].odometer
                lastOdometer = fuelUps[i].odometer
                partialConsumption = (fuelAmount / drivenDistance) * 100
            }
        }

        if (fuelAmount <= 0)
            return 0f

        val avgConsumption = partialConsumption / counter

        return ((avgConsumption * 100.0).roundToInt() / 100.0).toFloat()
    }

    fun avgFuelCostPerUnit(): Float {
        val fuelUps = mydb.getFuelUps().sortedByDescending { it.ID }

        if (fuelUps.size < 2)
            return 0f

        var lastOdometer = fuelUps[0].odometer
        var drivenDistance = 0
        var priceAmount = fuelUps[0].pricePerUnit * fuelUps[0].fuelAmount

        for (i in 1 until fuelUps.size) {
            drivenDistance += lastOdometer - fuelUps[i].odometer
            lastOdometer = fuelUps[i].odometer
            priceAmount += fuelUps[i].pricePerUnit * fuelUps[i].fuelAmount
        }

        if (drivenDistance <= 0 || priceAmount <= 0)
            return 0f

        val avgFuelCostPerUnit = priceAmount / drivenDistance

        return ((avgFuelCostPerUnit * 100.0).roundToInt() / 100.0).toFloat()
    }


    //endregion
}