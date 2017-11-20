package com.intelthings.intelthings.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.intelthings.intelthings.R;
import com.intelthings.intelthings.Service.MQTTService;

/**
 * Created by Lab1 on 07.11.2017.
 */

public class SettingActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        usernameEditText = (EditText) findViewById(R.id.usernameEdtTxt);                    //Инициализация текстового поля юзернэйма
        passwordEditText = (EditText) findViewById(R.id.passwordEdtTxt);                    //Инициализация текстового поля пароля
        brokerPortNumberEditText = (EditText) findViewById(R.id.brokerPortNumberEdtTxt);    //Инициализация текстового поля рабочего порта брокера
        connectionButton = (Button) findViewById(R.id.connectBtn);                          //Инициализация кнопки соединения с брокером
        publishButton = (Button) findViewById(R.id.publishBtn);
        subscribeButton = (Button) findViewById(R.id.subscribeBtn);

        publishButton.setVisibility(View.INVISIBLE);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.connectBtn:
                        setUsername(usernameEditText.getText().toString());
                        setPassword(passwordEditText.getText().toString());
                        setBrokerPort(brokerPortNumberEditText.getText().toString());
                        mqttService = new MQTTService(getUsername(), getPassword(), SettingActivity.this);
                        mqttService.connectMQTTServer();
                        publishButton.setVisibility(View.VISIBLE);
                        break;
                    case R.id.publishBtn:
                        try{
                            mqttService.publishMQTTMessage("test_topic");
                        }catch (NullPointerException e){
                            e.printStackTrace();
                            System.out.println("MQTTservice object not created.");
                        }
                        break;
                    case R.id.subscribeBtn:
                        try{
                            mqttService.subscribeToTopic();
                        }catch (Exception e){
                            e.printStackTrace();
                            System.out.println("trouble to subscribe");
                        }
                        break;
                }

            }
        };

        connectionButton.setOnClickListener(onClickListener);
        publishButton.setOnClickListener(onClickListener);
        subscribeButton.setOnClickListener(onClickListener);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(String brokerPort) {
        this.brokerPort = brokerPort;
    }

    public EditText usernameEditText;
    public EditText passwordEditText;
    public EditText brokerPortNumberEditText;
    public Button connectionButton;
    public Button publishButton;
    public Button subscribeButton;
    public MQTTService mqttService;

    private String password;
    private String username;
    private String brokerPort;

}
