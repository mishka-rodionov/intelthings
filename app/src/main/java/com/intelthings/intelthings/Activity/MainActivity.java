package com.intelthings.intelthings.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.LinearLayout;

import com.intelthings.intelthings.Logic.Home;
import com.intelthings.intelthings.R;
import com.intelthings.intelthings.Service.DatabaseManager;
import com.intelthings.intelthings.Service.MQTTService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqLiteDatabase = dbManager.getWritableDatabase();
        roomName = new ArrayList<String>();

        checkFirstRun();

        roomActivityHashMap = new HashMap<String, RoomActivity>();
        imageButtonSettings = (ImageButton) findViewById(R.id.imageButtonSettings);
        addRooms = (FloatingActionButton) findViewById(R.id.addRoomsBtn);
        imageButtonSettings.setOnClickListener(this);
        addRooms.setOnClickListener(this);
        roomCount = 0;

//        home = new Home("MyHome");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonSettings:
                Intent settingActivityIntent = new Intent(MainActivity.this, SettingActivity.class);    //Интент для перехода на активити настроек
                startActivity(settingActivityIntent);
                break;
            case R.id.addRoomsBtn:
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
                        contentValues.put("RoomName", roomnameEdtTxt.getText().toString());
                        contentValues.put("date_time", getTime());
                        sqLiteDatabase.insert("home", null, contentValues);
                        contentValues.clear();
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

    //Метод определения первого запуска приложения и текущей версии. При первом запуске метод создает SharedPreferences
    //в котором сохраняет определенное значение. При последующих запусках происходит проверка на наличие этого значения
    //и если оно существует, то происходит обычный запуск, без начальных инициализаций.
    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile28";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -2;
        final int currentVersionCode = BuildConfig.VERSION_CODE;
        final SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Получение текущей версии кода
        Log.d(LOG_TAG, "currentVersionCode = " + currentVersionCode);
        Log.d(LOG_TAG, "current time = " + getTime());


        // Получение сохраненной версии кода
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);
        Log.d(LOG_TAG, "savedVersionCode = " + savedVersionCode);

        // Проверка на первый вход или апгрейд
        if (currentVersionCode == savedVersionCode) {
            // При выполнении данного условия происходит обычная загрузка приложения
            Log.d(LOG_TAG, "currentVersionCode == savedVersionCode");
            try{
                connectMqtt(sqLiteDatabase);
            }catch (Exception e){
                e.printStackTrace();
            }
            Cursor cursor = sqLiteDatabase.query("home", null, null, null, null, null, null);
            if (cursor.moveToFirst()){
                int roomNameColIndex = cursor.getColumnIndex("RoomName");
                Log.d(LOG_TAG, "roomNameColIndex = " + roomNameColIndex);
                Log.d(LOG_TAG, "value = " + cursor.getString(roomNameColIndex));
                do {
                    roomName.add(cursor.getString(roomNameColIndex));
                    Log.d(LOG_TAG, cursor.getString(roomNameColIndex));
                }while (cursor.moveToNext());
            }else{
                Log.d(LOG_TAG, "0 rows in table home");
            }

            return;
        } else if (savedVersionCode == DOESNT_EXIST) {
            Log.d(LOG_TAG, "savedVersionCode == DOESNT_EXIST");
            // TODO This is a new install (or the user cleared the shared preferences)
            //*При первом включении приложения выводится пользовательский диалог*//
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

                    //******************** Удаление таблиц для тестирования первого включения
//                    sqLiteDatabase.execSQL("drop table userInfo");
//                    sqLiteDatabase.execSQL("drop table home");
                    //********************

                    //Создание таблицы для пользовательской информации (логин и пароль).
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

                    //Создание таблицы для хранения списка комнат.
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
                    //При первом запуске приложения и нажатии кнопки cancel в диалоговом окне ввода
                    //имени пользователя и пароля, приложение будет закрыто.
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

    public void connectMqtt(SQLiteDatabase sqLiteDatabase){
        Cursor cursor = sqLiteDatabase.query("userInfo", null, null, null, null, null, null);
        int loginColIndex = cursor.getColumnIndex("Username");
        int passwordColIndex = cursor.getColumnIndex("Password");
        Log.d(LOG_TAG, "login = " + cursor.getString(loginColIndex));
        Log.d(LOG_TAG, "password = " + cursor.getString(passwordColIndex));
        mqttService = MQTTService.getMqttServiceInstance();
        mqttService.setMQTTServiceParameters(cursor.getString(loginColIndex), cursor.getString(passwordColIndex),
                this);
        cursor.close();
    }

    private Home home;
    private HashMap<String, RoomActivity> roomActivityHashMap;
    private FloatingActionButton addRooms;
    private ImageButton imageButtonSettings;
    private Integer roomCount;
    private String LOG_TAG = "myApp";
    private DatabaseManager dbManager = new DatabaseManager(MainActivity.this);
    private final ContentValues contentValues = new ContentValues();
    private SQLiteDatabase sqLiteDatabase;
    private ArrayList<String> roomName;
    private MQTTService mqttService;
    private ArrayList<View> viewArrayList;
    private LinearLayout dynamicLinearlayout;

}
