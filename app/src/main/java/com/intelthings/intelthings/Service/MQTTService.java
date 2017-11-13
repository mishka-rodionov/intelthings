package com.intelthings.intelthings.Service;

import android.app.Activity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * Created by Lab1 on 07.11.2017.
 */

public class MQTTService {

    public MQTTService(){

    }

    public MQTTService(String mqttUsername, String mqttPassword, Activity activity){
        setMqttUsername(mqttUsername);
        setMqttPassword(mqttPassword);
        setMqttBrokerPortNumber("15305");
        setMqttBrokerURL("tcp://m20.cloudmqtt.com:");
        setMqttClientID(MqttClient.generateClientId());
        setClient(new MqttAndroidClient(activity.getApplicationContext(), getMqttBrokerURL() +
                getMqttBrokerPortNumber(), getMqttClientID()));
        setOptions(new MqttConnectOptions());
        getOptions().setUserName(getMqttUsername());
        getOptions().setPassword(getMqttPassword().toCharArray());
        setActivity(activity);
    }

    public MQTTService(String mqttUsername, String mqttPassword, String mqttBrokerPortNumber,
                       String mqttBrokerURL, Activity activity){
        setMqttUsername(mqttUsername);
        setMqttPassword(mqttPassword);
        setMqttBrokerPortNumber(mqttBrokerPortNumber);
        setMqttBrokerURL(mqttBrokerURL);
        setMqttClientID(MqttClient.generateClientId());
        setClient(new MqttAndroidClient(activity.getApplicationContext(), getMqttBrokerURL() +
                getMqttBrokerPortNumber(), getMqttClientID()));
        setOptions(new MqttConnectOptions());
        getOptions().setUserName(getMqttUsername());
        getOptions().setPassword(getMqttPassword().toCharArray());
        setActivity(activity);
    }

    //Getter & setter
    public String getMqttBrokerURL() {
        return mqttBrokerURL;
    }

    public void setMqttBrokerURL(String mqttBrokerURL) {
        this.mqttBrokerURL = mqttBrokerURL;
    }

    public String getMqttUsername() {
        return mqttUsername;
    }

    public void setMqttUsername(String mqttUsername) {
        this.mqttUsername = mqttUsername;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }

    public String getMqttBrokerPortNumber() {
        return mqttBrokerPortNumber;
    }

    public void setMqttBrokerPortNumber(String mqttBrokerPortNumber) {
        this.mqttBrokerPortNumber = mqttBrokerPortNumber;
    }

    public Integer getMqttQoS() {
        return mqttQoS;
    }

    public void setMqttQoS(Integer mqttQoS) {
        this.mqttQoS = mqttQoS;
    }

    public String getMqttClientID() {
        return mqttClientID;
    }

    public void setMqttClientID(String mqttClientID) {
        this.mqttClientID = mqttClientID;
    }

    public MqttAndroidClient getClient() {
        return client;
    }

    public void setClient(MqttAndroidClient client) {
        this.client = client;
    }

    public MqttConnectOptions getOptions() {
        return options;
    }

    public void setOptions(MqttConnectOptions options) {
        this.options = options;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    //Private fields
    private Activity activity;
    private String mqttBrokerURL;
    private String mqttUsername;
    private String mqttPassword;
    private String mqttBrokerPortNumber;
    private String mqttClientID;
    private Integer mqttQoS;
    private MqttAndroidClient client;
    private MqttConnectOptions options;

}
