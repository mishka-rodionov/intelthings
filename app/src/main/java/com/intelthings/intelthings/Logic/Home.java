package com.intelthings.intelthings.Logic;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

import com.intelthings.intelthings.Service.MQTTService;

import java.util.HashMap;

/**
 * Created by Lab1 on 20.11.2017.
 * Базовый класс приложения. Имеет единственный экземпляр. Содержит в себе контейнер объектов класса
 * комнат. Также используется для работы с MQTT-сервисом.
 */

public class Home extends Application implements Parcelable {



    public static void initHomeInstance(){
        if(homeInstance == null){
            homeInstance = new Home();
        }
    }

    public static Home getHomeInstance(){
        return homeInstance;
    }

    public Home(){}

    private Home(String homeName){
        this.homeName = homeName;
        roomHashMap = new HashMap<String, Room>();
    }

    public void addRoom(String roomName){
        Room room = new Room(roomName);
        roomHashMap.put(roomName, room);
    }

    public void publishLight(String roomName, String lightName, Boolean state, MQTTService mqttService, String payloadLight){
        setTopic(roomHashMap.get(roomName).publishLight(lightName, state));
        mqttService.publishMQTTMessage(getTopic(), payloadLight);
    }


    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public HashMap<String, Room> getRoomHashMap() {
        return roomHashMap;
    }

    public void setRoomHashMap(HashMap<String, Room> roomHashMap) {
        this.roomHashMap = roomHashMap;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = homeName + "/" + topic;
    }


    private String homeName;
    private String topic;
    private HashMap<String, Room> roomHashMap;
    private static Home homeInstance;

    @Override
    public void onCreate(){
        super.onCreate();
        Home.initHomeInstance();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.homeName);
        dest.writeString(this.topic);
        dest.writeSerializable(this.roomHashMap);
    }

    protected Home(Parcel in) {
        this.homeName = in.readString();
        this.topic = in.readString();
        this.roomHashMap = (HashMap<String, Room>) in.readSerializable();
    }

    public static final Parcelable.Creator<Home> CREATOR = new Parcelable.Creator<Home>() {
        @Override
        public Home createFromParcel(Parcel source) {
            return new Home(source);
        }

        @Override
        public Home[] newArray(int size) {
            return new Home[size];
        }
    };
}
