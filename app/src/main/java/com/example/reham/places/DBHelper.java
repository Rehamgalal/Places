package com.example.reham.places;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static String dbName = "locations.db";
    private static int Version = 2;

    public DBHelper(Context context) {
        super(context, dbName, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE =
                "CREATE TABLE " + PlacesContract.Entry.tableName +
                " ( " + PlacesContract.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PlacesContract.Entry.placeName + " TEXT , " +
                PlacesContract.Entry.placeDescription + " TEXT , " +
                PlacesContract.Entry.latitude + " REAL ," +
                PlacesContract.Entry.longtude + " REAL ); ";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PlacesContract.Entry.tableName);
        onCreate(db);
    }
}
