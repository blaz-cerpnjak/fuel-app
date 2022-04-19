package com.blazc.fuelapp.helper

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.math.roundToInt

class FuelUp (var odometer: Int, var fuelAmount: Float, var pricePerUnit: Float,
              var inputDate: String, var comment: String, var isPartial: Int,
              var vehicleID: Int = -1, var ID: Int = -1) {

    fun calculateTotalAmount(): Float {
        var totalAmount = fuelAmount * pricePerUnit
        totalAmount = ((totalAmount * 100.0).roundToInt() / 100.0).toFloat()
        return totalAmount
    }

    fun formatInputDate(): String {
        val dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        val date = LocalDate.parse(inputDate, dtf)
        return dateFormat.format(date)
    }

    fun getCurrency(): String {
        return Currency.getInstance(Locale.getDefault()).getSymbol(Locale.getDefault())
    }
}