package com.intelthings.intelthings.Logic;

import com.intelthings.intelthings.Service.MQTTService;

import java.util.HashMap;

/**
 * Created by Lab1 on 20.11.2017.
 */

public class Home {

    public Home(){}

    public Home(String homeName){
        this.homeName = homeName;
        roomHashMap = new HashMap<String, Room>();
    }

    public void addRoom(String roomName){
        Room room = new Room(roomName);
        roomHashMap.put(roomName, room);
    }

    public void publishLight(String roomName, String lightName, Boolean state, MQTTService mqttService){
        setTopic(roomHashMap.get(roomName).publishLight(lightName, state));
        mqttService.publishMQTTMessage(getTopic());
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
}
