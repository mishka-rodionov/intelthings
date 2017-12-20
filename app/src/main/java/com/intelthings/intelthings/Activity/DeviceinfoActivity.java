package com.intelthings.intelthings.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.intelthings.intelthings.R;

public class DeviceinfoActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviceinfo);
        deviceNameEdttxt = (EditText) findViewById(R.id.devicenameEdttxt);
        deviceIdEdttxt = (EditText) findViewById(R.id.deviceidEdttxt);
        okDeviceButton = (Button) findViewById(R.id.okDeviceButton);
        cancelDeviceButton = (Button) findViewById(R.id.cancelDeviceButton);
        okDeviceButton.setOnClickListener(this);
        cancelDeviceButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.okDeviceButton:
                Intent intent = getIntent();
                String tableName = intent.getStringExtra("tableName");
                Log.d(LOG_TAG, "tableName = " + tableName);
                intent.putExtra("buttonClick", "OK");
                intent.putExtra("deviceName", deviceNameEdttxt.getText().toString());
                intent.putExtra("deviceID", deviceIdEdttxt.getText().toString());
                intent.putExtra("tableName", tableName);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.cancelDeviceButton:
                Intent intentCancel = getIntent();
                intentCancel.putExtra("buttonClick", "Cancel");
                setResult(RESULT_OK,intentCancel);
                finish();
                break;
        }
    }

    private EditText deviceNameEdttxt;
    private EditText deviceIdEdttxt;
    private Button okDeviceButton;
    private  Button cancelDeviceButton;
    private String LOG_TAG = "myApp";
}
