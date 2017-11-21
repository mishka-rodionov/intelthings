package com.intelthings.intelthings.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
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
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(linearLayout, linearLayoutParams);

        ViewGroup.LayoutParams linParView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView = new TextView(this);
        textView.setText("RoomName");
        textView.setLayoutParams(linParView);
        linearLayout.addView(textView);

        ViewGroup.LayoutParams linParBut = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FloatingActionButton floatingActionButton = new FloatingActionButton(this);
        floatingActionButton.setLayoutParams(linParBut);
//        floatingActionButton.setId();
        linearLayout.addView(floatingActionButton);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.
        }
    }

    private LinearLayout linearLayout;
    private TextView textView;
    private ViewGroup.LayoutParams linearLayoutParams;
}
