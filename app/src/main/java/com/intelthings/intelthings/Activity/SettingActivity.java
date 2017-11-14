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

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUsername(usernameEditText.getText().toString());
                setPassword(passwordEditText.getText().toString());
                setBrokerPort(brokerPortNumberEditText.getText().toString());
                MQTTService mqttService = new MQTTService(getUsername(), getPassword(), SettingActivity.this);
                mqttService.connectMQTTServer();
            }
        };

        connectionButton.setOnClickListener(onClickListener);
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

    private String password;
    private String username;
    private String brokerPort;

}
