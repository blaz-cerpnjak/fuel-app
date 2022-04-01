package com.blazc.fuelapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val CREATE_FUELUP =
        "CREATE TABLE FUELUP (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "CURRENT_ODOMETER INT," +
                "FUEL_AMOUNT FLOAT," +
                "PRICE_PER_UNIT FLOAT," +
                "DATE TEXT," +
                "IS_PARTIAL INT," +
                "COMMENT TEXT)"

    private val CREATE_VEHICLE =
        "CREATE TABLE VEHICLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT)"

    companion object {
        var DATABASE_VERSION = 1
        const val DATABASE_NAME = "BeProductive.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_FUELUP)
        db.execSQL(CREATE_VEHICLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}