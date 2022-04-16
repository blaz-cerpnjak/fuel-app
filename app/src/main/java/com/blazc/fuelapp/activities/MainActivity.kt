package com.blazc.fuelapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.blazc.fuelapp.DatabaseHelper
import com.blazc.fuelapp.R
import com.blazc.fuelapp.databinding.ActivityMainBinding
import com.blazc.fuelapp.fragments.FuelUpFragment
import com.blazc.fuelapp.fragments.HistoryFragment
import com.blazc.fuelapp.fragments.HomeFragment
import io.ak1.OnBubbleClickListener

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

}