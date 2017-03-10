package com.example.anhdt.smartalarm;

import android.content.res.Resources;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;

public class SetAlarmActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Resources system;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        initUiComponents();
    }

    private void initUiComponents() {
        timePicker = (TimePicker) findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(true);

    }


}
