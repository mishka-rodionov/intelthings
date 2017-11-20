package com.intelthings.intelthings.Service;

import android.app.Activity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
//import org.eclipse.paho.client.mqttv3.IM
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.SQLOutput;

/**
 * Created by Lab1 on 07.11.2017.
 */

public class MQTTService implements MqttCallback {
    //Конструктор по умолчанию
    public MQTTService(){

    }
    //Тестовый конструктор для связи с брокером cloudmqtt
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
        getClient().setCallback(this);      //Вызов данного метода необходим для оформления подписки.
    }

    //Основной конструктор, использует данные из SettingActivity. создает обект по указанным
    //username, password, номер порта брокера, адресу брокера и активити из оторого происходит вызов
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
        getClient().setCallback(this);
    }

    //Метод для установки соединения с MQTT-брокером
    public void connectMQTTServer(){
        try {
            IMqttToken iMqttToken = getClient().connect(getOptions());
            IMqttActionListener iMqttActionListener = new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Connection success");
                    System.out.println("ClientID = " + getMqttClientID());
                    System.out.println("Server URI = " + getClient().getServerURI());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Connection failed");
                }

            };
            iMqttToken.setActionCallback(iMqttActionListener);
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    //Метод для публикации сообщений
    public void publishMQTTMessage(String topic){
        MqttMessage message = new MqttMessage();
        try {
            getClient().publish(topic, message);
        } catch (MqttException e) {
            System.out.println("Publish failed.");
            e.printStackTrace();
        }
    }

    //Метод для подписки на топик
    public void subscribeToTopic(){
        try {
            getClient().subscribe("recieveTopic", 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Failed to subscribe");
                }
            });
        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Subscribe payload = " + message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

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
