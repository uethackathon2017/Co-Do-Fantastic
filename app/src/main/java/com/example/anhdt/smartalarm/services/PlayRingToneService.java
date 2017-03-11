package com.example.anhdt.smartalarm.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.anhdt.smartalarm.R;
import com.example.anhdt.smartalarm.activities.CaculateActivity;
import com.example.anhdt.smartalarm.activities.MainActivity;
import com.example.anhdt.smartalarm.challenges.countstep.activities.CountStepActivity;
import com.example.anhdt.smartalarm.challenges.recognize.activities.EmotionActivity;
import com.example.anhdt.smartalarm.models.Alarm;

import java.io.IOException;

/**
 * Created by anhdt on 3/12/2017.
 */

public class PlayRingToneService extends Service {

    //media player
    private MediaPlayer mediaPlayer;

    //alarm
    private Alarm alarm;

    private boolean alarmActive;


    //notification id
    private static final int NOTIFY_ID = 1;

    private Vibrator vibrator;
    long[] pattern = { 1000, 200, 200, 200 };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        final Alarm alarm = (Alarm) bundle.getSerializable("alarm");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setVolume(1.0f, 1.0f);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        mediaPlayer.setLooping(true);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (alarm.getAlarmTonePath() != "") {
            if (alarm.getVibrate()) {
                vibrator.vibrate(pattern, 0);
            }
            try {
                mediaPlayer.setDataSource(this,
                        Uri.parse(alarm.getAlarmTonePath()));
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        caseCalling();

        Intent notIntent = null;

        switch (alarm.getChallenge()) {
            case CALCULATION:
                notIntent = new Intent(this, CaculateActivity.class);
                break;
            case RECOGNIZE:
                notIntent = new Intent(this, EmotionActivity.class);
                break;
            case WALKING:
                notIntent = new Intent(this, CountStepActivity.class);
                break;
        }
        notIntent.putExtra("alarm", alarm);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Báo thức")
                .setOngoing(true);
        Notification not = builder.build();
        startForeground(NOTIFY_ID, not);
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_NOT_STICKY;
    }

    public void caseCalling() {
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(getClass().getSimpleName(), "Incoming call: "
                                + incomingNumber);
                        try {
                            mediaPlayer.pause();
                        } catch (IllegalStateException e) {

                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d(getClass().getSimpleName(), "Call State Idle");
                        try {
                            mediaPlayer.start();
                        } catch (IllegalStateException e) {

                        }
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    public boolean isPng(){
        return mediaPlayer.isPlaying();
    }

    public void pausePlayer(){
        mediaPlayer.pause();
    }

    public void go(){
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        try {
            if (vibrator != null)
                vibrator.cancel();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.stop();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.release();
        } catch (Exception e) {

        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
