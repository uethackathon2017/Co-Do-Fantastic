package com.example.anhdt.smartalarm.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.anhdt.smartalarm.activities.CaculateActivity;
import com.example.anhdt.smartalarm.challenges.countstep.activities.CountStepActivity;
import com.example.anhdt.smartalarm.challenges.recognize.activities.EmotionActivity;
import com.example.anhdt.smartalarm.models.Alarm;
import com.example.anhdt.smartalarm.utils.StaticWakeLock;

/**
 * Created by anhdt on 3/11/2017.
 */

public class AlarmAlertBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mathAlarmServiceIntent = new Intent(
                context,
                AlarmServiceBroadcastReciever.class);
        context.sendBroadcast(mathAlarmServiceIntent, null);

        StaticWakeLock.lockOn(context);
        Bundle bundle = intent.getExtras();
        final Alarm alarm = (Alarm) bundle.getSerializable("alarm");

        if (alarm.getChallenge() != null) {
            if (alarm.getChallenge() == Alarm.Challenge.CALCULATION) {
                Intent mathAlarmAlertActivityIntent;
                mathAlarmAlertActivityIntent = new Intent(context, CaculateActivity.class);
                mathAlarmAlertActivityIntent.putExtra("alarm", alarm);
                mathAlarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mathAlarmAlertActivityIntent);
            }
            else if (alarm.getChallenge() == Alarm.Challenge.RECOGNIZE){
                Intent recognizeAlarmAlertActivityIntent;
                recognizeAlarmAlertActivityIntent = new Intent(context, EmotionActivity.class);
                recognizeAlarmAlertActivityIntent.putExtra("alarm", alarm);
                recognizeAlarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(recognizeAlarmAlertActivityIntent);
            }
            else {
                Intent countStepAlarmAlertActivityIntent;
                countStepAlarmAlertActivityIntent = new Intent(context, CountStepActivity.class);
                countStepAlarmAlertActivityIntent.putExtra("alarm", alarm);
                countStepAlarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(countStepAlarmAlertActivityIntent);
            }
        }
    }

}
