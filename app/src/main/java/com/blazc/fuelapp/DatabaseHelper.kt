package com.blazc.fuelapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.blazc.fuelapp.helper.FuelUp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

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

    fun insertVehicle(name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("NAME", name)
        }
        return db.insert(VEHICLE, null, values) // returns row id
    }

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

    @SuppressLint("Range")
    fun getFuelUp(id: Int): FuelUp? {
        var fuelUp: FuelUp? = null
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $FUEL_UP WHERE ID = ?", arrayOf(id.toString()))
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val id = cursor.getInt(cursor.getColumnIndex("ID"))
            val odometer = cursor.getInt(cursor.getColumnIndex("CURRENT_ODOMETER"))
            val fuelAmount = cursor.getFloat(cursor.getColumnIndex("FUEL_AMOUNT"))
            val pricePerUnit = cursor.getFloat(cursor.getColumnIndex("PRICE_PER_UNIT"))
            val date = cursor.getString(cursor.getColumnIndex("DATE"))
            val isPartial = cursor.getInt(cursor.getColumnIndex("IS_PARTIAL"))
            val comment = cursor.getString(cursor.getColumnIndex("COMMENT"))
            val vehicleID = cursor.getInt(cursor.getColumnIndex("VEHICLE_ID"))
            fuelUp = FuelUp(odometer, fuelAmount, pricePerUnit, date, comment, isPartial, vehicleID, id)
            cursor.moveToNext()
        }
        cursor.close()
        return fuelUp
    }

    @SuppressLint("Range")
    fun getFuelUps(): List<FuelUp> {
        val fuelUpList = mutableListOf<FuelUp>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $FUEL_UP", null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val id = cursor.getInt(cursor.getColumnIndex("ID"))
            val odometer = cursor.getInt(cursor.getColumnIndex("CURRENT_ODOMETER"))
            val fuelAmount = cursor.getFloat(cursor.getColumnIndex("FUEL_AMOUNT"))
            val pricePerUnit = cursor.getFloat(cursor.getColumnIndex("PRICE_PER_UNIT"))
            val date = cursor.getString(cursor.getColumnIndex("DATE"))
            val isPartial = cursor.getInt(cursor.getColumnIndex("IS_PARTIAL"))
            val comment = cursor.getString(cursor.getColumnIndex("COMMENT"))
            val vehicleID = cursor.getInt(cursor.getColumnIndex("VEHICLE_ID"))
            fuelUpList.add(FuelUp(odometer, fuelAmount, pricePerUnit, date, comment, isPartial, vehicleID, id))
            cursor.moveToNext()
        }
        cursor.close()
        return fuelUpList
    }

    fun deleteFuelUp(id: Int): Int {
        val db = writableDatabase
        val selection = "ID LIKE ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(FUEL_UP, selection, selectionArgs) // returns deleted rows
    }

    fun updateFuelUp(fuelUp: FuelUp): Int {
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
        val selection = "ID LIKE ?"
        val selectionArgs = arrayOf(fuelUp.ID.toString())
        val count = db.update(
            FUEL_UP,
            values,
            selection,
            selectionArgs
        )
        return count // returns number of rows affected
    }

    @SuppressLint("Range")
    fun getCurrentMonthsFuelUps(): List<FuelUp> {
        val fuelUpList = mutableListOf<FuelUp>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $FUEL_UP", null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val date = cursor.getString(cursor.getColumnIndex("DATE"))
            val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
            val parsedDate = LocalDate.parse(date, dtf)

            if (parsedDate.monthValue == LocalDate.now().monthValue && parsedDate.year == LocalDate.now().year) {
                val id = cursor.getInt(cursor.getColumnIndex("ID"))
                val odometer = cursor.getInt(cursor.getColumnIndex("CURRENT_ODOMETER"))
                val fuelAmount = cursor.getFloat(cursor.getColumnIndex("FUEL_AMOUNT"))
                val pricePerUnit = cursor.getFloat(cursor.getColumnIndex("PRICE_PER_UNIT"))
                val isPartial = cursor.getInt(cursor.getColumnIndex("IS_PARTIAL"))
                val comment = cursor.getString(cursor.getColumnIndex("COMMENT"))
                val vehicleID = cursor.getInt(cursor.getColumnIndex("VEHICLE_ID"))

                fuelUpList.add(FuelUp(odometer, fuelAmount, pricePerUnit, date, comment, isPartial, vehicleID, id))
            }

            cursor.moveToNext()
        }
        cursor.close()
        return fuelUpList
    }

}