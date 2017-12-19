package com.intelthings.intelthings.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.intelthings.intelthings.R;

public class UserinfoActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "inside onCreateUserInfoActivity");
        setContentView(R.layout.activity_userinfo);
        editTextMqttLogin = (EditText) findViewById(R.id.mqttLogin);
        editTextMqttPassword = (EditText) findViewById(R.id.mqttPassword);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.okButton:
                Intent intent = new Intent();
                intent.putExtra("mqttLogin", editTextMqttLogin.getText().toString());
                intent.putExtra("mqttPassword", editTextMqttPassword.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private EditText editTextMqttLogin;
    private EditText editTextMqttPassword;
    private Button cancelButton;
    private Button okButton;
    private String LOG_TAG = "myApp";
}
