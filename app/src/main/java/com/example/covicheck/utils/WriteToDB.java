
package com.example.covicheck.utils;

import android.content.ContentValues;
import android.os.Environment;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
//import java.io.File;


public class WriteToDB {
    SQLiteDatabase db;
    String name;
    //SQLiteDatabase db = this.getWritableDatabase();

    //constructor to pass patient name
    public WriteToDB (String name) {
        this.name=name;
    }

    public void createDB() {

        db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+"/databaseFolder/myDB", null);
        db.beginTransaction();
        //perform your database operations here ...
        try {
            db.execSQL("create table "+name+" ("
                    + " patientID integer PRIMARY KEY autoincrement, "
                    + " heartRate integer, "
                    + " respiratoryRate integer, "
                    + " nausea integer, "
                    + " headache integer, "
                    + " diarrhea integer, "
                    + " sore integer, "
                    + " fever integer, "
                    + " muscle integer, "
                    + " loss integer, "
                    + " cough integer, "
                    + " shortness integer, "
                    + " tired integer ); ");

            db.setTransactionSuccessful(); //commit your changes
        }
        catch (SQLiteException e) {
            //report problem
            e.printStackTrace();
        }
    }
    public void writeDataToDB (ContentValues values) {
        db.beginTransaction();
        // Inserting Row
        db.insert("tblPat", null, values);
        //perform your database operations here ...
        //db.execSQL(query);
        db.setTransactionSuccessful(); //commit your changes
    }
}