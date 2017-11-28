package com.intelthings.intelthings.Service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Lab1 on 07.11.2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {

    public DatabaseManager(Context context, String dbName){
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        sqLiteDatabase.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "email text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private String LOG_TAG = "databaseLogs";
}
