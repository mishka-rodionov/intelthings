package com.intelthings.intelthings.Logic;

import java.util.HashMap;

/**
 * Created by Lab1 on 07.11.2017.
 */

public class Room {

    public Room(){}

    public Room(String roomName){
        this.roomName = roomName;
        lightHashMap = new HashMap<String, Light>();
        socketHashMap = new HashMap<String, Socket>();
        sensorHashMap = new HashMap<String, Sensor>();
        actuatorHashMap = new HashMap<String, Actuator>();
    }

    //Формирование топика для публикации, при изменении состояния отдельного выключателя,
    //с сохранением данных об изменении в его объекте.
    public String publishLight(String lightName, Boolean lightState){
        Light light = getLightHashMap().get(lightName);
        light.setState(lightState);
        light.setTopic();
        setTopic(light.getTopic());
        return getTopic();
    }

    //Метод для добавления нового устройства в контейнер к выключателям
    public void addLight(String lightName){
        Light light = new Light(lightName, false);
        lightHashMap.put(lightName, light);
    }

    //Удаление устройства из контейнера выключателей
    public void destroyLight(String lightName){
        getLightHashMap().remove(lightName);
    }

    public HashMap<String, Light> getLightHashMap() {
        return lightHashMap;
    }

    public void setLightHashMap(HashMap<String, Light> lightsHashMap) {
        this.lightHashMap = lightsHashMap;
    }

    public HashMap<String, Socket> getSocketHashMap() {
        return socketHashMap;
    }

    public void setSocketHashMap(HashMap<String, Socket> socketsHashMap) {
        this.socketHashMap = socketsHashMap;
    }

    public HashMap<String, Sensor> getSensorHashMap() {
        return sensorHashMap;
    }

    public void setSensorHashMap(HashMap<String, Sensor> sensorHashMap) {
        this.sensorHashMap = sensorHashMap;
    }

    public HashMap<String, Actuator> getActuatorHashMap() {
        return actuatorHashMap;
    }

    public void setActuatorHashMap(HashMap<String, Actuator> actuatorHashMap) {
        this.actuatorHashMap = actuatorHashMap;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = roomName + "/" + topic;
    }

    private HashMap<String, Light> lightHashMap;
    private HashMap<String, Socket> socketHashMap;
    private HashMap<String, Sensor> sensorHashMap;
    private HashMap<String, Actuator> actuatorHashMap;
    private String roomName;
    private String topic;

}
