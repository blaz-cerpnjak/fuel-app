package com.blazc.fuelapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.blazc.fuelapp.helper.FuelUp

private const val FUEL_UP = "FUEL_UP"
private const val VEHICLE = "VEHICLE"

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val CREATE_FUELUP =
        "CREATE TABLE " + FUEL_UP + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "CURRENT_ODOMETER INT," +
                "FUEL_AMOUNT FLOAT," +
                "PRICE_PER_UNIT FLOAT," +
                "DATE TEXT," +
                "IS_PARTIAL INT," +
                "COMMENT TEXT," +
                "VEHICLE_ID INT)"

    private val CREATE_VEHICLE =
        "CREATE TABLE " + VEHICLE + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT)"

    companion object {
        var DATABASE_VERSION = 1
        const val DATABASE_NAME = "FuelUp.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_FUELUP)
        db.execSQL(CREATE_VEHICLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    //region Vehicle
    fun insertVehicle(name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("NAME", name)
        }
        return db.insert(VEHICLE, null, values) // returns row id
    }
    //endregion

    //region FuelUp
    fun insertFuelUp(fuelUp: FuelUp): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("CURRENT_ODOMETER", fuelUp.odometer)
            put("FUEL_AMOUNT", fuelUp.fuelAmount)
            put("PRICE_PER_UNIT", fuelUp.pricePerUnit)
            put("DATE", fuelUp.inputDate)
            put("IS_PARTIAL", fuelUp.isPartial)
            put("COMMENT", fuelUp.comment)
            put("VEHICLE_ID", fuelUp.vehicleID)
        }
        return db.insert(FUEL_UP, null, values) // returns row id
    }
    //endregion
}