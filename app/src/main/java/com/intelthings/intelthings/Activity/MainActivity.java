package com.intelthings.intelthings.Activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.intelthings.intelthings.Logic.Home;
import com.intelthings.intelthings.R;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roomActivityHashMap = new HashMap<String, RoomActivity>();
        imageButtonSettings = (ImageButton) findViewById(R.id.imageButtonSettings);
        addRooms = (FloatingActionButton) findViewById(R.id.addRoomsBtn);
        imageButtonSettings.setOnClickListener(this);
        addRooms.setOnClickListener(this);
        roomCount = 0;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonSettings:
                Intent settingActivityIntent = new Intent(MainActivity.this, SettingActivity.class);    //Интент для перехода на активити настроек
                startActivity(settingActivityIntent);
                break;
            case R.id.addRoomsBtn:
                RoomActivity roomActivity = new RoomActivity();                                         //Создание класса активити для новой комнаты.
                roomCount++;
                if(roomActivityHashMap.isEmpty()){
                    roomActivityHashMap.put("newRoom1", roomActivity);                                  //Добавление новой комнаты в общий контейнер комнат.
                    Intent roomActivityIntent = new Intent(this, RoomActivity.class);                   //интент для перехода на страницу вновь созданной комнаты.
                    roomActivityIntent.putExtra("roomActivity", (Parcelable) roomActivityHashMap.get("newRoom1"));
                    startActivity(roomActivityIntent);
                }
                else{
                    roomActivityHashMap.put("newRoom" + roomCount.toString(), roomActivity);
                    Intent roomActivityIntent = new Intent(this, RoomActivity.class);
                    startActivity(roomActivityIntent);
                }
                break;
        }
    }

    public Home home;
    public HashMap<String, RoomActivity> roomActivityHashMap;
    public FloatingActionButton addRooms;
    public ImageButton imageButtonSettings;
    public Integer roomCount;
}
