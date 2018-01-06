package com.intelthings.intelthings.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelthings.intelthings.Logic.Light;
import com.intelthings.intelthings.R;
import com.intelthings.intelthings.Service.DatabaseManager;
import com.intelthings.intelthings.Service.MQTTService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Lab1 on 07.11.2017.
 */

public class RoomActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        mqttService = MQTTService.getMqttServiceInstance();

        Intent mainActivityIntent = getIntent();
        DatabaseManager dbManager = new DatabaseManager(this);

        sqLiteDatabase = dbManager.getWritableDatabase();
        final Context context = RoomActivity.this;
        tableName = mainActivityIntent.getStringExtra("tableName");
        Log.d(LOG_TAG, "tableName = " + tableName);

        viewList = new ArrayList<View>();
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutRoomActivity);
        createLight = (Button) findViewById(R.id.createLightBtn);

//        representView(sqLiteDatabase, tableName);

        Cursor roomTableCursor = sqLiteDatabase.query(tableName, null,null,null,null,null,null);
        if(roomTableCursor.moveToFirst()){
            do{
                representView(sqLiteDatabase, roomTableCursor.getString(roomTableCursor.getColumnIndex("DeviceName")));
            }while (roomTableCursor.moveToNext());
        }else{
            Log.d(LOG_TAG, "0 rows");

        }

        //Обработчик нажатия на кнопку создания устройства. При нажатии на кнопку, создается view и
        //отображается в linearLayout. Также создаётся запись в таблице с именем комнаты.
        createLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomActivity.this, DeviceinfoActivity.class);
                intent.putExtra("tableName", tableName);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(data.getStringExtra("buttonClick").equals("OK")) {
                Log.d(LOG_TAG, "press OK");
                String deviceName;
                String roomTableName;
                if (data == null) {
                    return;
                } else {
                    deviceName = data.getStringExtra("deviceName");
                    roomTableName = data.getStringExtra("tableName");
                }

                sqLiteDatabase.execSQL("create table if not exists light" + deviceName
                        + " ("
                        + "id integer primary key autoincrement,"
                        + "name text,"
                        + "state text,"
                        + "stateOS text,"
                        + "temperature real,"
                        + "date_time text,"
                        + "FK integer" + ");");

//                        light.setName(input.getText().toString());
//                        textView.setText(input.getText().toString());

                contentValues.put("name", deviceName);
                Log.d(LOG_TAG, "roomname = " + roomTableName);
                cvRoomTable.put("Roomname", roomTableName);
                cvRoomTable.put("DeviceType", "light");
                cvRoomTable.put("DeviceName", deviceName);
                cvRoomTable.put("Datetime", getTime());

                sqLiteDatabase.insert(roomTableName, null, cvRoomTable);
                cvRoomTable.clear();
                representView(sqLiteDatabase, deviceName);
            }else if(data.getStringExtra("buttonClick").equals("Cancel")){
                Log.d(LOG_TAG, "press cancel");
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.
        }
    }

    //Метод получения текущей даты и времени
    public String getTime(){
        return "" + Calendar.getInstance().get(Calendar.YEAR) + ":"
                + Calendar.getInstance().get(Calendar.MONTH)+ ":"
                + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + ":"
                + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":"
                + Calendar.getInstance().get(Calendar.MINUTE) + ":"
                + Calendar.getInstance().get(Calendar.SECOND);
    }

    public void representView(final SQLiteDatabase sqLiteDatabase, String deviceName){
        View view = getLayoutInflater().inflate(R.layout.custom_view, null);
        final TextView textView = (TextView) view.findViewById(R.id.textView);
        final Light light = new Light();

        light.setName(deviceName);
        light.setState(false);
        light.setTemperature(23.0);             //Тестовое значение, необходимо обновлять из подписки
        light.setStateOutsideSwitch(false);     //Тестовое значение, необходимо обновлять из подписки

        textView.setText(deviceName);

        contentValues.put("temperature", light.getTemperature());
        contentValues.put("stateOS", light.getStateOutsideSwitch().toString());
        contentValues.put("FK", 1);

        ImageButton createBtn = (ImageButton) view.findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomNames = readHomeTable(sqLiteDatabase);
                Log.d(LOG_TAG, "roomNames.size() = " + roomNames.size());
                for (int i = 0; i < roomNames.size(); i++) {
                    Log.d(LOG_TAG, roomNames.get(i) + " ");
                }
            }
        });
        ImageButton clearBtn = (ImageButton) view.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mqttService.subscribeToTopic(light.getName());
                Log.d(LOG_TAG, "Subscribe to topic" + light.getName());
            }
        });
        ImageButton buildBtn = (ImageButton) view.findViewById(R.id.buildBtn);
        buildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < roomNames.size(); i++) {
                    try{
                        readRoomTable(roomNames.get(i), sqLiteDatabase);
                    }catch (SQLiteException e){
                        Log.d(LOG_TAG, "no such table");
                    }
                }
            }
        });
        ImageButton settingsBtn = (ImageButton) view.findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        MQTTService.initMQTTServiceInstance();
                MQTTService mqttService = MQTTService.getMqttServiceInstance();
                mqttService.publishMQTTMessage("test_topic", textView.getText().toString());
                light.setState(!light.getState());
                contentValues.put("state", light.getState().toString());
                sqLiteDatabase.insert("light" + light.getName(), null, contentValues);
            }
        });
        ImageButton doneBtn = (ImageButton) view.findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "--- Rows in mytable: ---");
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                Cursor c = sqLiteDatabase.query("light" + light.getName(), null, null, null, null, null, null);

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int stateColIndex = c.getColumnIndex("state");
                    int stateOSColIndex = c.getColumnIndex("stateOS");
                    int temperatureColIndex = c.getColumnIndex("temperature");
                    int date_timeColIndex = c.getColumnIndex("date_time");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", name = " + c.getString(nameColIndex) +
                                        ", state = " + c.getString(stateColIndex) +
                                        ", stateOS = " + c.getString(stateOSColIndex) +
                                        ", temperature = " + c.getString(temperatureColIndex) +
                                        ", date_time = " + c.getString(date_timeColIndex));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();
            }
        });

        viewList.add(view);
        linearLayout.addView(view);
    }

    public void readRoomTable(String tableName, SQLiteDatabase sqLiteDatabase){
        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = sqLiteDatabase.query(tableName, null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int roomNameColIndex = c.getColumnIndex("Roomname");
            int deviceTypeColIndex = c.getColumnIndex("DeviceType");
            int deviceNameColIndex = c.getColumnIndex("DeviceName");
            int date_timeColIndex = c.getColumnIndex("Datetime");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", RoomName = " + c.getString(roomNameColIndex) +
                                ", DeviceType = " + c.getString(deviceTypeColIndex) +
                                ", DeviceName = " + c.getString(deviceNameColIndex) +
                                ", date_time = " + c.getString(date_timeColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
    }

    public ArrayList<String> readHomeTable(SQLiteDatabase sqLiteDatabase){
        ArrayList<String> rooms = new ArrayList<String>();
        Log.d(LOG_TAG, "--- Rows in home table: ---");

        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = sqLiteDatabase.query("home", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int homeNameColIndex = c.getColumnIndex("Homename");
            int roomNameColIndex = c.getColumnIndex("Roomname");
            int date_timeColIndex = c.getColumnIndex("Datetime");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(homeNameColIndex) +
                                ", state = " + c.getString(roomNameColIndex) +
                                ", date_time = " + c.getString(date_timeColIndex));
                rooms.add(c.getString(roomNameColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        return rooms;
    }

    private List<View> viewList;                //Контейнер для хранения динамически добавляемых view объектов
    private int counter = 0;
    private LinearLayout linearLayout;          //Layout в который встраиваются динамически добавляемые view
    private Button createLight;
    private final int createLightDialog = 1;
    private String m_Text = "";
    private String LOG_TAG = "myApp";
    private String tableName;
    private ContentValues cvRoomTable = new ContentValues();
    private final ContentValues contentValues = new ContentValues();
    private ArrayList<String> roomNames;
    private SQLiteDatabase sqLiteDatabase;
    private MQTTService mqttService;


}
