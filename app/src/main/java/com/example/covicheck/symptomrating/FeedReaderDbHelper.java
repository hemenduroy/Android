package com.example.covicheck.symptomrating;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CoviCheck.db";
    String name;
    //SQLiteDatabase db = this.getWritableDatabase();

    //constructor to pass patient name

    public FeedReaderDbHelper(Context context,String name) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.name=name;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS "+name+" ("
                + " recordID integer PRIMARY KEY autoincrement, "
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
        System.out.println(db.getPath());
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //db.execSQL(SQL_DELETE_ENTRIES);
        //onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //onUpgrade(db, oldVersion, newVersion);
    }
}
