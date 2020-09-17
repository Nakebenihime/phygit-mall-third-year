package com.example.dilemma_mobile.embedded;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "embedded.db";

    //JOURNEY - TO BE VALIDATED
    public static final String TABLE_JOURNEY = "journey";
    public static final String JOURNEY_ID = "ID";
    public static final String JOURNEY_NAME = "NAME";

    //STORE - TO BE VALIDATED
    public static final String TABLE_STORE = "store";
    public static final String STORE_ID = "ID";
    public static final String STORE_NAME = "NAME";
    public static final String STORE_TYPE = "TYPE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String LATITUDE = "LATITUDE";

    // STORE JOURNEY - TO BE VALIDATED
    public static final String TABLE_JOURNEY_STORE = "journey_store";
    public static final String STORE_ASS_ID = "ID_STORE";
    public static final String JOURNEY_ASS_ID = "ID_JOURNEY";

    // TABLE CREATION SCRIPTS
    public static final String CREATE_TABLE_JOURNEY = "CREATE TABLE IF NOT EXISTS " + TABLE_JOURNEY + "("
            + JOURNEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + JOURNEY_NAME + " TEXT )";
    public static final String CREATE_TABLE_STORE = "CREATE TABLE IF NOT EXISTS " + TABLE_STORE + "("
            + STORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + STORE_NAME + " TEXT,"
            + STORE_TYPE + " TEXT,"
            + LONGITUDE + " float, " + LATITUDE + " float)";
    public static final String CREATE_TABLE_JOURNEY_STORE = "CREATE TABLE IF NOT EXISTS " + TABLE_JOURNEY_STORE + "("
            + STORE_ASS_ID + " INTEGER NOT NULL ,"
            + JOURNEY_ASS_ID + " INTEGER NOT NULL,"
            + "FOREIGN KEY(" + STORE_ASS_ID + ") REFERENCES " + TABLE_STORE + "(" + STORE_ID + "),"
            + "FOREIGN KEY(" + JOURNEY_ASS_ID + ") REFERENCES " + TABLE_JOURNEY + "(" + JOURNEY_ID + "),"
            + "PRIMARY KEY(" + STORE_ASS_ID + "," + JOURNEY_ASS_ID + "))";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE_JOURNEY);
        sqLiteDatabase.execSQL(CREATE_TABLE_STORE);
        sqLiteDatabase.execSQL(CREATE_TABLE_JOURNEY_STORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNEY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNEY_STORE);
        onCreate(sqLiteDatabase);
    }

    /**
     * Insert a store to SQLite database.
     *
     * @param id
     * @param name
     * @param longitude
     * @param latitude
     * @return
     */
    public boolean insertStore(int id, String name, String type, float longitude, float latitude) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STORE_TYPE, type);
        contentValues.put(STORE_NAME, name);
        contentValues.put(LONGITUDE, longitude);
        contentValues.put(LATITUDE, latitude);
        long result = database.insert(TABLE_STORE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Insert a journey to SQLite database
     *
     * @param id
     * @param name
     * @return
     */
    public boolean insertJourney(int id, String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(JOURNEY_ID, id);// a retirer  car auto increment
        contentValues.put(JOURNEY_NAME, name);
        long result = database.insert(TABLE_JOURNEY, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Insert journey_store associative table to SQLite database.
     *
     * @param idJourney
     * @param idStore
     * @return
     */
    public boolean insertJourneyStore(int idJourney, int idStore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(JOURNEY_ASS_ID, idJourney);
        contentValues.put(STORE_ASS_ID, idStore);
        long result = db.insert(TABLE_JOURNEY_STORE, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
}