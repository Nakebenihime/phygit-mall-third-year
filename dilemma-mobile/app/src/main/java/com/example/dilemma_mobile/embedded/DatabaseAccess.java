package com.example.dilemma_mobile.embedded;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dilemma_mobile.model.Store;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    Cursor cursor =null;

    public DatabaseAccess(Context context) {
        this.openHelper = new DatabaseFromAsset(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if(instance==null){
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void openDatabse(){
        database= openHelper.getWritableDatabase();
    }

    public void closeDatabase() {
        if (database != null) {
            database.close();
        }
    }

    /**
     * Return the store if it match with the capteurUUID
     * @param capteurUUID
     * @return null or the store
     */
    public Store getStoreByUUID(String capteurUUID) {
        Store store = null;
        cursor = database.rawQuery("SELECT * FROM STORE WHERE capteurUUID = ?", new String[]{String.valueOf(capteurUUID)});

        if(cursor.moveToFirst()  && cursor.getCount()== 1){;

        store = new Store(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5));
        }
        //Only 1 result
        cursor.close();
        closeDatabase();
        return store;
    }

    public boolean insertStore(Store store){

        ContentValues values = new ContentValues();
        values.put("id", store.getId());
        values.put("name", store.getName());
        values.put("type", store.getType());
        values.put("latitude", store.getLatitude());
        values.put("longitude", store.getLongitude());
        values.put("capteurUUID", store.getCapteurUUID());

        boolean result = database.insert("STORE", null, values) > 0;
        if (result == true)
            Log.d("Create", "Data Has Been Saved");
        return result;
    }

    public Integer deleteStore(String id){
        return database.delete("STORE",  "id = ?", new String[]{id} );
    }
}
