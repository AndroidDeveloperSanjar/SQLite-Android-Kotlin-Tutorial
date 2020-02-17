package com.example.sqlitekotlin.Utils

object Constants {
    //Camera permissions
    const val CAMERA_REQUEST_CODE = 100
    const val STORAGE_REQUEST_CODE = 101

    //Image pick
    const val IMAGE_PICK_CAMERA_CODE = 102
    const val IMAGE_PICK_GALLERY_CODE = 103

    //DB name
    const val DB_NAME = "my_records_db"

    //DB version
    const val DB_VERSION = 1

    //DB table name
    const val TABLE_NAME = "my_records_table"

    //Fields of table
    const val C_ID = "id"
    const val C_NAME = "name"
    const val C_IMAGE = "image"
    const val C_BIO = "bio"
    const val C_PHONE = "phone"
    const val C_EMAIL = "email"
    const val C_DOB = "dob"
    const val C_ADDED_TIME_STAMP = "added_time_stamp"
    const val C_UPDATED_TIME_STAMP = "updated_time_stamp"

    //Create table query
    const val CREATE_TABLE = (
            "CREATE TABLE" + TABLE_NAME + "("
            + C_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_NAME + "TEXT,"
            + C_PHONE + "TEXT,"
            + C_IMAGE + "TEXT,"
            + C_BIO + "TEXT,"
            + C_EMAIL + "TEXT,"
            + C_DOB + "TEXT,"
            + C_ADDED_TIME_STAMP + "TEXT,"
            + C_UPDATED_TIME_STAMP + "TEXT"
            + ")"
            )

}