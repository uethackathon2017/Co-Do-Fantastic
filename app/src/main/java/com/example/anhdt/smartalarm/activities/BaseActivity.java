package com.example.anhdt.smartalarm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.anhdt.smartalarm.receivers.AlarmServiceBroadcastReciever;

/**
 * Created by anhdt on 3/11/2017.
 */

public class BaseActivity extends AppCompatActivity{

    public void callMathAlarmScheduleService() {
        Intent mathAlarmServiceIntent = new Intent(this, AlarmServiceBroadcastReciever.class);
        sendBroadcast(mathAlarmServiceIntent, null);
    }

}
