package com.example.sqlitekotlin.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqlitekotlin.Utils.Constants

class DBHelper(
    context: Context
): SQLiteOpenHelper(
    context,
    Constants.DB_NAME,
    null,
    Constants.DB_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Constants.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS" + Constants.TABLE_NAME)
        onCreate(db)
    }

    fun insertRecord(
        name: String,
        image: String,
        phone: String,
        email: String,
        dob: String,
        bio: String,
        addedTime: String,
        updatedTime: String
    ): Long{
        val db = writableDatabase!!
        val values = ContentValues()
        values.put(Constants.C_NAME,name)
        values.put(Constants.C_EMAIL,email)
        values.put(Constants.C_IMAGE,image)
        values.put(Constants.C_PHONE,phone)
        values.put(Constants.C_DOB,dob)
        values.put(Constants.C_BIO,bio)
        values.put(Constants.C_ADDED_TIME_STAMP,addedTime)
        values.put(Constants.C_UPDATED_TIME_STAMP,updatedTime)

        val id = db.insert(Constants.TABLE_NAME,null,values)

        db.close()

        return id
    }
}