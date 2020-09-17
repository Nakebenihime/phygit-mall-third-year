package com.example.dilemma_mobile.embedded;


import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseFromAsset extends SQLiteAssetHelper {
    public static final String DBNAME ="dilemmadatabase.db";
    private  static  final int DATABASE_VERSION=1;

    public DatabaseFromAsset(Context context){
        super(context,DBNAME,null,DATABASE_VERSION);
        // this.context= context;
    }
}
