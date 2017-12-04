package com.intelthings.intelthings.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.design.BuildConfig;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.intelthings.intelthings.Logic.Home;
import com.intelthings.intelthings.R;
import com.intelthings.intelthings.Service.DatabaseManager;

import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkFirstRun();

        roomActivityHashMap = new HashMap<String, RoomActivity>();
        imageButtonSettings = (ImageButton) findViewById(R.id.imageButtonSettings);
        addRooms = (FloatingActionButton) findViewById(R.id.addRoomsBtn);
        imageButtonSettings.setOnClickListener(this);
        addRooms.setOnClickListener(this);
        roomCount = 0;
        sqLiteDatabase = dbManager.getWritableDatabase();
//        home = new Home("MyHome");
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonSettings:
                Intent settingActivityIntent = new Intent(MainActivity.this, SettingActivity.class);    //Интент для перехода на активити настроек
                startActivity(settingActivityIntent);
                break;
            case R.id.addRoomsBtn:
//                RoomActivity roomActivity = new RoomActivity();                                         //Создание класса активити для новой комнаты.
//                roomCount++;
//                if(roomActivityHashMap.isEmpty()){
//                    roomActivityHashMap.put("newRoom1", roomActivity);                                  //Добавление новой комнаты в общий контейнер комнат.
//                    Intent roomActivityIntent = new Intent(this, RoomActivity.class);                   //интент для перехода на страницу вновь созданной комнаты.
//                    //roomActivityIntent.putExtra("roomActivity", (Parcelable) roomActivityHashMap.get("newRoom1"));
//                    startActivity(roomActivityIntent);
//                }
//                else{
//                    roomActivityHashMap.put("newRoom" + roomCount.toString(), roomActivity);
//                    Intent roomActivityIntent = new Intent(this, RoomActivity.class);
//                    startActivity(roomActivityIntent);
//                }
                //******************************************************************************
                //Создание пользовательского диалогового окна, с полем ввода имени устройства и
                //с кнопками подтверждения и отмены операции.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter the name");
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(this).inflate(R.layout.room_dialog_layout,
                        (ViewGroup) findViewById(android.R.id.content), false);
                // Set up the input
                final EditText roomnameEdtTxt = (EditText) viewInflated.findViewById(R.id.roomnameEdtTxt);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sqLiteDatabase.execSQL("create table " + roomnameEdtTxt.getText().toString()
                                + " ("
                                + "id integer primary key autoincrement,"
                                + "RoomName text,"
                                + "DeviceType text,"
                                + "DeviceName text,"
                                + "date_time text,"
                                + "FK integer"+ ");");
                        Intent roomActivityIntent = new Intent(MainActivity.this, RoomActivity.class);
                        roomActivityIntent.putExtra("tableName", roomnameEdtTxt.getText().toString());
                        startActivity(roomActivityIntent);

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                //******************************************************************************
                break;
        }
    }

    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile21";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -2;

        // Get current version code
        final int currentVersionCode = BuildConfig.VERSION_CODE;
        Log.d(LOG_TAG, "currentVersionCode = " + currentVersionCode);
        Log.d(LOG_TAG, "current time = " + getTime());


        // Get saved version code
        final SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);
        Log.d(LOG_TAG, "savedVersionCode = " + savedVersionCode);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            Log.d(LOG_TAG, "currentVersionCode == savedVersionCode");
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            Log.d(LOG_TAG, "savedVersionCode == DOESNT_EXIST");
            // TODO This is a new install (or the user cleared the shared preferences)
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter the name");
            // I'm using fragment here so I'm using getView() to provide ViewGroup
            // but you can provide here any other instance of ViewGroup from your Fragment / Activity
            View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.username_dialog_layout,
                    (ViewGroup) findViewById(android.R.id.content), false);
            // Set up the input
            final EditText usernameEdtTxt = (EditText) viewInflated.findViewById(R.id.usernameEdtTxt);
            final EditText passwordEdtTxt = (EditText) viewInflated.findViewById(R.id.passwordEdtTxt);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    // Update the shared preferences with the current version code
                    prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();

                    //********************
                    sqLiteDatabase.execSQL("drop table userInfo");
                    sqLiteDatabase.execSQL("drop table home");
                    //********************

                    sqLiteDatabase.execSQL("create table userInfo"
                            + " ("
                            + "id integer primary key autoincrement,"
                            + "Username text,"
                            + "Password text,"
                            + "date_time text"
                            + ");");
                    contentValues.put("Username", usernameEdtTxt.getText().toString());
                    contentValues.put("Password", passwordEdtTxt.getText().toString());
                    contentValues.put("date_time", getTime());
                    sqLiteDatabase.insert("UserInfo", null, contentValues);
                    contentValues.clear();
                    sqLiteDatabase.execSQL("create table home"
                            + " ("
                            + "id integer primary key autoincrement,"
                            + "HomeName text,"
                            + "RoomName text,"
                            + "date_time text"
                            + ");");
                    contentValues.put("HomeName", usernameEdtTxt.getText().toString());
                    contentValues.put("RoomName", "firstRoom");
                    contentValues.put("date_time", getTime());
                    sqLiteDatabase.insert("home", null, contentValues);
                    contentValues.clear();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });


            builder.show();

        } else if (currentVersionCode > savedVersionCode) {

            Log.d(LOG_TAG, "currentVersionCode > savedVersionCode");
            // TODO This is an upgrade
        }

//        // Update the shared preferences with the current version code
//        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }

    public String getTime(){
        return "" + Calendar.getInstance().get(Calendar.YEAR) + ":"
                + Calendar.getInstance().get(Calendar.MONTH)+ ":"
                + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + ":"
                + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":"
                + Calendar.getInstance().get(Calendar.MINUTE) + ":"
                + Calendar.getInstance().get(Calendar.SECOND);
    }

    public Home home;
    public HashMap<String, RoomActivity> roomActivityHashMap;
    public FloatingActionButton addRooms;
    public ImageButton imageButtonSettings;
    public Integer roomCount;
    private String LOG_TAG = "myApp";
    DatabaseManager dbManager = new DatabaseManager(MainActivity.this);
    final ContentValues contentValues = new ContentValues();
    private SQLiteDatabase sqLiteDatabase;

}
