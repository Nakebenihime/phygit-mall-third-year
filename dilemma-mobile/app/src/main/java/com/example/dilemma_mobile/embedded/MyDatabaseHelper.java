package com.example.dilemma_mobile.embedded;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dilemma_mobile.model.Location;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "geolocations";
    public static int DATABASE_VERSION = 1;

    public static final String GEOLOCATION_TABLE = "GEOLOCATION";
    public static final String GEOLOCATION_X = "x";
    public static final String GEOLOCATION_Y = "y";
    public static final String GEOLOCATION_FLOOR = "floor";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + GEOLOCATION_TABLE + " (" +
                "x FLOAT," +
                "y FLOAT," +
                "floor INTEGER);";
        db.execSQL(script);
    }

    public void addGeolocation(Location location, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(GEOLOCATION_X, location.getX());
        values.put(GEOLOCATION_Y, location.getY());
        values.put(GEOLOCATION_FLOOR, location.getFloor());

        db.insert(GEOLOCATION_TABLE, null, values);
    }

    public List<Location> getAll(SQLiteDatabase db) {
        List<Location> listAll = new ArrayList<>();
        Cursor cursor = db.query(GEOLOCATION_TABLE,null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            while(! cursor.isAfterLast()) {
                double x = cursor.getDouble(cursor.getColumnIndex("x"));
                double y = cursor.getDouble(cursor.getColumnIndex("y"));
                int floor = cursor.getInt(cursor.getColumnIndex("floor"));
                Location location = new Location(x,y,floor);
                listAll.add(location);
                cursor.moveToNext();
            }
        }
        return listAll;
    }

    public void deleteAll(SQLiteDatabase db) {
        db.execSQL("delete from "+ GEOLOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GEOLOCATION_TABLE);
        onCreate(db);
        this.DATABASE_VERSION = newVersion;
    }
}