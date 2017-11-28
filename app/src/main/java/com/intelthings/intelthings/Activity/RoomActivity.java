package com.intelthings.intelthings.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelthings.intelthings.R;
import com.intelthings.intelthings.Service.MQTTService;
import com.intelthings.intelthings.View.UserDialogFragment;

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
        final Context context = RoomActivity.this;
        createLight = (Button) findViewById(R.id.createLightBtn);
        createLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.custom_view, null);
                final TextView textView = (TextView) view.findViewById(R.id.textView);

                //******************************************************************************
                //Создание пользовательского диалогового окна, с полем ввода имени устройства и
                //с кнопками подтверждения и отмены операции.
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter the name");
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(context).inflate(R.layout.frame_dialog_layout,
                        (ViewGroup) findViewById(android.R.id.content), false);
                // Set up the input
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        textView.setText(input.getText().toString());;
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                //******************************************************************************

                ImageButton createBtn = (ImageButton) view.findViewById(R.id.createBtn);
                ImageButton clearBtn = (ImageButton) view.findViewById(R.id.clearBtn);
                ImageButton buildBtn = (ImageButton) view.findViewById(R.id.buildBtn);
                ImageButton settingsBtn = (ImageButton) view.findViewById(R.id.settingsBtn);
                settingsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        MQTTService.initMQTTServiceInstance();
                        MQTTService mqttService = MQTTService.getMqttServiceInstance();
                        mqttService.publishMQTTMessage("test_topic", textView.getText().toString());
                    }
                });
                ImageButton doneBtn = (ImageButton) view.findViewById(R.id.doneBtn);

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
    private final int createLightDialog = 1;
    private String m_Text = "";

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
