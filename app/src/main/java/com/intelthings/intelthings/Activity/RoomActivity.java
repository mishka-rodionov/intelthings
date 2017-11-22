package com.intelthings.intelthings.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelthings.intelthings.R;

/**
 * Created by Lab1 on 07.11.2017.
 */

public class RoomActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.room_activity);

        mainLinearLayout = new LinearLayout(this);
        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(mainLinearLayout, linearLayoutParams);

        ViewGroup.LayoutParams linParView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        floatingActionButton = new FloatingActionButton(this);
//        textView.setText("RoomName");
        floatingActionButton.setLayoutParams(linParView);
        floatingActionButton.setClickable(true);
        mainLinearLayout.addView(floatingActionButton);
/*
        ViewGroup.LayoutParams llVertical = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutVertical = new LinearLayout(mainLinearLayout.getContext());
        linearLayoutVertical.setOrientation(LinearLayout.VERTICAL);
        linearLayoutVertical.setGravity(Gravity.BOTTOM);
        linearLayoutVertical.setLayoutParams(llVertical);

        mainLinearLayout.addView(linearLayoutVertical);

        ViewGroup.LayoutParams llHor = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutHorizontal = new LinearLayout(linearLayoutVertical.getContext());
        linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutHorizontal.setLayoutParams(llHor);

        linearLayoutVertical.addView(linearLayoutHorizontal);

        ViewGroup.LayoutParams linParBut = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        createButtonLight = new Button(this);
        createButtonLight.setLayoutParams(linParBut);
        createButtonLight.setText("Create light");
        createButtonLight.setOnClickListener(onClickListenerLight);

        createButtonSocket = new Button(this);
        createButtonSocket.setLayoutParams(linParBut);
        createButtonSocket.setText("Create socket");
        createButtonSocket.setOnClickListener(onClickListenerSocket);

        createButtonSensor = new Button(this);
        createButtonSensor.setLayoutParams(linParBut);
        createButtonSensor.setText("Create sensor");
        createButtonSensor.setOnClickListener(onClickListenerSensor);

        createButtonActuator = new Button(this);
        createButtonActuator.setLayoutParams(linParBut);
        createButtonActuator.setText("Create actuator");
        createButtonActuator.setOnClickListener(onClickListenerActuator);

        linearLayoutHorizontal.addView(createButtonLight);
        linearLayoutHorizontal.addView(createButtonSocket);
        linearLayoutHorizontal.addView(createButtonSensor);
        linearLayoutHorizontal.addView(createButtonActuator);

        onClickListenerLight = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click on CreateLight button");
            }
        };

        onClickListenerSocket = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click on CreateSocket button");
            }
        };

        onClickListenerSensor = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click on CreateSensor button");
            }
        };

        onClickListenerActuator = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click on CreateActuator button");
            }
        };
*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.
        }
    }

    private LinearLayout mainLinearLayout;
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
}
