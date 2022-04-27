package com.blazc.fuelapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blazc.fuelapp.DatabaseHelper
import com.blazc.fuelapp.R
import com.blazc.fuelapp.activities.MainActivity
import com.blazc.fuelapp.databinding.FragmentFuelUpBinding
import com.blazc.fuelapp.helper.FuelUp
import com.blazc.fuelapp.helper.MyToast
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt

class FuelUpFragment(val fuelUpID: Int = -1, val isUpdate: Boolean = false) : Fragment() {
    private var _binding: FragmentFuelUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var mydb: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFuelUpBinding.inflate(inflater, container, false)
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
        if (isUpdate && fuelUpID > 0) {
            binding.btnRemove.visibility = View.VISIBLE
            binding.btnSave.text = getString(R.string.update)
            populateDate(fuelUpID)
            btnRemoveOnClick()
        }
        setCurrentDate()
        etPriceListener()
        btnSaveOnClick()
    }

    private fun isNumber(s: String): Boolean {
        try {
            val n = s.toDouble()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    //region User Input Checks
    private fun checkOdometer(): Boolean {
        val odometer = binding.etOdometer.text.toString();
        if (odometer.isEmpty()) {
            MyToast.showError(mainActivity, "Error", "Odometer cannot be empty.")
            return false
        } else if (!isNumber(odometer)) {
            MyToast.showError(mainActivity, "Error", "Odometer must be a valid number.")
            return false
        }
        return true
    }

    private fun checkFuelAmount(): Boolean {
        val fuelAmount = binding.etFuelAmount.text.toString();
        if (fuelAmount.isEmpty()) {
            MyToast.showError(mainActivity, "Error", "Fuel amount cannot be empty.")
            return false
        } else if (!isNumber(fuelAmount)) {
            MyToast.showError(mainActivity, "Error", "Fuel amount must be a valid number.")
            return false
        }
        return true
    }

    private fun checkPrice(): Boolean {
        val price = binding.etPricePerLiter.text.toString();
        if (price.isEmpty()) {
            MyToast.showError(mainActivity, "Error", "Price cannot be empty.")
            return false
        } else if (!isNumber(price)) {
            MyToast.showError(mainActivity, "Error", "Price must be a valid number.")
            return false
        }
        return true
    }

    private fun checkTotalAmount(): Boolean {
        val totalAmount = binding.etTotalAmount.text.toString();
        if (totalAmount.isEmpty()) {
            MyToast.showError(mainActivity, "Error", "Total amount cannot be empty.")
            return false
        } else if (!isNumber(totalAmount)) {
            MyToast.showError(mainActivity, "Error", "Total amount must be a valid number.")
            return false
        }
        return true
    }
    //endregion

    //region Buttons
    private fun btnRemoveOnClick() {
        binding.btnRemove.setOnClickListener {
            removeFuelUp(fuelUpID)
            mainActivity.loadFragment(HistoryFragment())
        }
    }

    private fun btnSaveOnClick() {
        binding.btnSave.setOnClickListener {
            if (checkOdometer() && checkFuelAmount() && checkPrice() && checkTotalAmount())
                insertFuelUp()
        }
    }
    //endregion

    //region Input Listeners
    private fun etPriceListener() {
        binding.etPricePerLiter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.etFuelAmount.text.toString().isNotEmpty() && binding.etPricePerLiter.text.toString().isNotEmpty())
                    calculateTotalPriceAmount(
                        binding.etFuelAmount.text.toString().toDouble(),
                        binding.etPricePerLiter.text.toString().toDouble()
                    )
            }
        })
    }

    private fun setCurrentDate() {
        val dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        val currentDate = dateFormat.format(LocalDate.now())
        binding.txtDate.text = currentDate
    }

    private fun calculateTotalPriceAmount(fuelAmount: Double, price: Double) {
        var totalAmountPrice = fuelAmount * price
        totalAmountPrice = (totalAmountPrice * 100.0).roundToInt() / 100.0
        binding.etTotalAmount.setText(totalAmountPrice.toString())
    }
    //endregion

    //region Database
    private fun removeFuelUp(id: Int) {
        if (mydb.deleteFuelUp(id) > 0) {
            MyToast.showSuccess(mainActivity, "Success", "Fuel up was deleted.")
        } else {
            MyToast.showError(mainActivity, "Error", "Fuel up was not deleted.")
        }
    }

    private fun isPartial(): Int {
        if (binding.cboxIsPartial.isChecked)
            return 1
        return 0
    }

    private fun insertFuelUp() {
        val fuelUp = FuelUp(
            binding.etOdometer.text.toString().toInt(),
            binding.etFuelAmount.text.toString().toFloat(),
            binding.etPricePerLiter.text.toString().toFloat(),
            LocalDateTime.now().toString(),
            binding.etComment.text.toString(),
            isPartial(),
            0f,
            -1, // carID (todo)
            fuelUpID
        )
        if (isUpdate) {
            if (mydb.updateFuelUp(fuelUp) > 0)
                MyToast.showSuccess(mainActivity, "Success", "Fuel up was updated.")
            else
                MyToast.showError(mainActivity, "Error", "Fuel up was not updated.")
        } else {
            if (mydb.insertFuelUp(fuelUp) > 0)
                MyToast.showSuccess(mainActivity, "Success", "Fuel up was saved.")
            else
                MyToast.showError(mainActivity, "Error", "Fuel up was not saved.")
        }
    }
    //endregion

    //region Update
    private fun populateDate(fuelUpID: Int) {
        val fuelUp = mydb.getFuelUp(fuelUpID)
        if (fuelUp != null) {
            binding.etOdometer.setText(fuelUp.odometer.toString())
            binding.etFuelAmount.setText(fuelUp.fuelAmount.toString())
            binding.etPricePerLiter.setText(fuelUp.pricePerUnit.toString())
            binding.etTotalAmount.setText(fuelUp.calculateTotalAmount().toString())
            binding.txtDate.setText(fuelUp.formatInputDate())
            binding.etComment.setText(fuelUp.comment)
            binding.cboxIsPartial.isChecked = fuelUp.isPartial == 1
        } else {
            MyToast.showWarning(mainActivity, "Oops", "Fuel up not found.")
        }
    }
    //endregion
}