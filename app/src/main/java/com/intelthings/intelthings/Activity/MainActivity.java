package com.intelthings.intelthings.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.intelthings.intelthings.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imageButtonSettings = (ImageButton) findViewById(R.id.imageButtonSettings);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.imageButtonSettings:
                        Intent settingActivityIntent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(settingActivityIntent);
                }
            }
        };
    }
}
