package com.intelthings.intelthings.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelthings.intelthings.Logic.Light;
import com.intelthings.intelthings.R;
import com.intelthings.intelthings.Service.DatabaseManager;
import com.intelthings.intelthings.Service.MQTTService;
import com.intelthings.intelthings.View.UserDialogFragment;

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
        setContentView(R.layout.room_activity);

        Intent mainActivityIntent = getIntent();
        DatabaseManager dbManager = new DatabaseManager(this);

        final ContentValues contentValues = new ContentValues();
        final SQLiteDatabase sqLiteDatabase = dbManager.getWritableDatabase();
        final Context context = RoomActivity.this;
        final String tableName = mainActivityIntent.getStringExtra("tableName");
        Log.d(LOG_TAG, "tableName = " + tableName);

        viewList = new ArrayList<View>();
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutRoomActivity);
        createLight = (Button) findViewById(R.id.createLightBtn);

        //Обработчик нажатия на кнопку создания устройства. При нажатии на кнопку, создается view и
        //отображается в linearLayout. Также создаётся запись в таблице с именем комнаты.
        createLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.custom_view, null);

                final TextView textView = (TextView) view.findViewById(R.id.textView);
                final Light light = new Light();

                light.setState(false);
                light.setTemperature(23.0);             //Тестовое значение, необходимо обновлять из подписки
                light.setStateOutsideSwitch(false);     //Тестовое значение, необходимо обновлять из подписки

                contentValues.put("temperature", light.getTemperature());
                contentValues.put("stateOS", light.getStateOutsideSwitch().toString());
                contentValues.put("FK", 1);
                cvRoomTable = new ContentValues();

                //******************************************************************************
                //Создание пользовательского диалогового окна, с полем ввода имени устройства и
                //с кнопками подтверждения и отмены операции.
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter the name");
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(context).inflate(R.layout.frame_dialog_layout,
                        (ViewGroup) findViewById(android.R.id.content), false);
                // Set up the input
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sqLiteDatabase.execSQL("create table if not exists light" + input.getText().toString()
                                + " ("
                                + "id integer primary key autoincrement,"
                                + "name text,"
                                + "state text,"
                                + "stateOS text,"
                                + "temperature real,"
                                + "date_time text,"
                                + "FK integer"+ ");");

                        light.setName(input.getText().toString());
                        textView.setText(input.getText().toString());

                        contentValues.put("name", input.getText().toString());
                        cvRoomTable.put("RoomName", tableName);
                        cvRoomTable.put("DeviceType", "light");
                        cvRoomTable.put("DeviceName", input.getText().toString());
                        cvRoomTable.put("date_time", getTime());

                        sqLiteDatabase.insert(tableName, null, cvRoomTable);
                        cvRoomTable.clear();

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

                ImageButton createBtn = (ImageButton) view.findViewById(R.id.createBtn);
                createBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(LOG_TAG, "--- Rows in mytable: ---");

                        // делаем запрос всех данных из таблицы mytable, получаем Cursor
                        Cursor c = sqLiteDatabase.query("home", null, null, null, null, null, null);

                        // ставим позицию курсора на первую строку выборки
                        // если в выборке нет строк, вернется false
                        if (c.moveToFirst()) {

                            // определяем номера столбцов по имени в выборке
                            int idColIndex = c.getColumnIndex("id");
                            int homeNameColIndex = c.getColumnIndex("HomeName");
                            int roomNameColIndex = c.getColumnIndex("RoomName");
                            int stateOSColIndex = c.getColumnIndex("stateOS");
                            int temperatureColIndex = c.getColumnIndex("temperature");
                            int date_timeColIndex = c.getColumnIndex("date_time");

                            do {
                                // получаем значения по номерам столбцов и пишем все в лог
                                Log.d(LOG_TAG,
                                        "ID = " + c.getInt(idColIndex) +
                                                ", name = " + c.getString(homeNameColIndex) +
                                                ", state = " + c.getString(roomNameColIndex) +
                                                ", date_time = " + c.getString(date_timeColIndex));
                                // переход на следующую строку
                                // а если следующей нет (текущая - последняя), то false - выходим из цикла
                            } while (c.moveToNext());
                        } else
                            Log.d(LOG_TAG, "0 rows");
                        c.close();
                    }
                });
                ImageButton clearBtn = (ImageButton) view.findViewById(R.id.clearBtn);
                ImageButton buildBtn = (ImageButton) view.findViewById(R.id.buildBtn);
                buildBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(LOG_TAG, "--- Rows in mytable: ---");
                        // делаем запрос всех данных из таблицы mytable, получаем Cursor
                        Cursor c = sqLiteDatabase.query(tableName, null, null, null, null, null, null);

                        // ставим позицию курсора на первую строку выборки
                        // если в выборке нет строк, вернется false
                        if (c.moveToFirst()) {

                            // определяем номера столбцов по имени в выборке
                            int idColIndex = c.getColumnIndex("id");
                            int roomNameColIndex = c.getColumnIndex("RoomName");
                            int deviceTypeColIndex = c.getColumnIndex("DeviceType");
                            int deviceNameColIndex = c.getColumnIndex("DeviceName");
                            int date_timeColIndex = c.getColumnIndex("date_time");

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
        });

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

    private List<View> viewList;                //Контейнер для хранения динамически добавляемых view объектов
    private int counter = 0;
    private LinearLayout linearLayout;          //Layout в который встраиваются динамически добавляемые view
    private Button createLight;
    private final int createLightDialog = 1;
    private String m_Text = "";
    private String LOG_TAG = "myApp";
    private ContentValues cvRoomTable;

}
