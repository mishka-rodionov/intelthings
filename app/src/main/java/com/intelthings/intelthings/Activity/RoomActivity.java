package com.intelthings.intelthings.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelthings.intelthings.R;
import com.intelthings.intelthings.Service.MQTTService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lab1 on 07.11.2017.
 */

public class RoomActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_activity);

        viewList = new ArrayList<View>();
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        createLight = (Button) findViewById(R.id.createLightBtn);
        createLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.custom_view, null);
                ImageButton createBtn = (ImageButton) view.findViewById(R.id.createBtn);
                ImageButton clearBtn = (ImageButton) view.findViewById(R.id.clearBtn);
                ImageButton buildBtn = (ImageButton) view.findViewById(R.id.buildBtn);
                ImageButton settingsBtn = (ImageButton) view.findViewById(R.id.settingsBtn);
                settingsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        MQTTService.initMQTTServiceInstance();
                        MQTTService mqttService = MQTTService.getMqttServiceInstance();
                        mqttService.publishMQTTMessage("test_topic");
                    }
                });
                ImageButton doneBtn = (ImageButton) view.findViewById(R.id.doneBtn);
                TextView textView = (TextView) view.findViewById(R.id.textView);
                viewList.add(view);
                linearLayout.addView(view);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.
        }
    }

    private List<View> viewList;
    private int counter = 0;
    private LinearLayout linearLayout;
    private Button createLight;

/*    private LinearLayout mainLinearLayout;
    private LinearLayout linearLayoutVertical;
    private LinearLayout linearLayoutHorizontal;
    private TextView textView;
    private ViewGroup.LayoutParams linearLayoutParams;
    private FloatingActionButton floatingActionButton;
    private Button createButtonLight;
    private Button createButtonSocket;
    private Button createButtonSensor;
    private Button createButtonActuator;
    private View.OnClickListener onClickListenerLight;
    private View.OnClickListener onClickListenerSocket;
    private View.OnClickListener onClickListenerSensor;
    private View.OnClickListener onClickListenerActuator;
    */
}
