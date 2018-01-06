package com.intelthings.intelthings.Service;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
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

public class MQTTService extends Application implements MqttCallback {
    //Конструктор по умолчанию
    public MQTTService(){

    }

    private MQTTService(int mqttQoS){
        this.mqttQoS = mqttQoS;
    }

    //Тестовый конструктор для связи с брокером cloudmqtt
    public void setMQTTServiceParameters(String mqttUsername, String mqttPassword, Activity activity){
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

    public synchronized static void initMQTTServiceInstance(){
        if(mqttServiceInstance == null){
            mqttServiceInstance = new MQTTService(0);
        }
    }

    public static MQTTService getMqttServiceInstance(){
        return mqttServiceInstance;
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
    public void publishMQTTMessage(String topic, String payload){
        MqttMessage message = new MqttMessage();
        message.setPayload(payload.getBytes());
        try {
            getClient().publish(topic, message);
            System.out.println("Publish tpoic = " + topic);
        } catch (MqttException e) {
            System.out.println("Publish failed.");
            e.printStackTrace();
        }
    }

    //Метод для подписки на топик
    public void subscribeToTopic(String topic){
        try {
            getClient().subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(LOG_TAG, "Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(LOG_TAG, "Failed to subscribe");
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
        Log.d(LOG_TAG, "Recieve topic = " + topic + ", " +  "Subscribe payload = " + message);
        Toast.makeText(MQTTService.this, "Subscribe payload = " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Override
    public void onCreate(){
        super.onCreate();
        MQTTService.initMQTTServiceInstance();
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
    private static MQTTService mqttServiceInstance;
    private String LOG_TAG = "myApp";

}
