package com.example.anhdt.smartalarm.challenges.countstep.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anhdt.smartalarm.R;
import com.example.anhdt.smartalarm.activities.WakeUpActivity;
import com.example.anhdt.smartalarm.challenges.countstep.accelerometer.AccelerometerDetector;
import com.example.anhdt.smartalarm.challenges.countstep.accelerometer.AccelerometerProcessing;
import com.example.anhdt.smartalarm.challenges.countstep.accelerometer.OnStepCountChangeListener;
import com.example.anhdt.smartalarm.challenges.countstep.dialogs.MyDialogEndTime;
import com.example.anhdt.smartalarm.challenges.recognize.activities.EmotionActivity;
import com.example.anhdt.smartalarm.models.Alarm;
import com.example.anhdt.smartalarm.services.PlayRingToneService;
import com.example.anhdt.smartalarm.utils.StaticWakeLock;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.Locale;

public class CountStepActivity extends AppCompatActivity {

    private Alarm alarm;
    private MediaPlayer mediaPlayer;

    private Vibrator vibrator;

    private boolean alarmActive;

    private static final int TIME_PLAY_EACH_QUESTION = 60;
    private static final int NUMBER_MAX_STEP = 40;
    private static final int TIME_REMAIN_EACH_QUESTION = 101;

    private static final String TAG = CountStepActivity.class.getSimpleName();

    private static final String PREFERENCES_NAME = "Values";
    private static final String PREFERENCES_VALUES_THRESHOLD_KEY = "threshold";
    private SharedPreferences preferences;
    private int mStepCount = 0;
    private AccelerometerDetector mAccelDetector;
    //private TextView mStepCountTextView;
    private TextView mTimeValTextView;
    private boolean isRunning;
    private MyDialogEndTime myDialogEndTime;
    private ArcProgress arcProgress;

    private Intent playIntent;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_REMAIN_EACH_QUESTION:
                    mTimeValTextView.setText(msg.arg1 + "");
                    if (msg.arg1 == 0) {
                        if (mStepCount >= NUMBER_MAX_STEP) {
                            if (!CountStepActivity.this.isFinishing()) {
                                createSuccessAlertDialog("Thông báo", "Bạn đã vượt qua thử thách thành công!");
                            }

                            stopService(playIntent);
                            CountStepActivity.this.finish();
                        }
                        else {
                            if (!CountStepActivity.this.isFinishing()) {
                                createFailedAlertDialog("Thông báo", "Bạn chưa vượt qua thử thách! Hãy thử lại!");
                            }
                            mStepCount = 0;
                            time = TIME_PLAY_EACH_QUESTION;
                        }

                    }
                    break;
            }
        }
    };

    private AlertDialog createSuccessAlertDialog(String title, String content) {
        return new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CountStepActivity.this, WakeUpActivity.class);
                        startActivity(intent);
                        stopService(playIntent);
                        CountStepActivity.this.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private AlertDialog createFailedAlertDialog(String title, String content) {
        return new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private int time;
    // constant reference
    private final AccelerometerProcessing mAccelerometerProcessing = AccelerometerProcessing.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_count_step);

        Bundle bundle = this.getIntent().getExtras();
        alarm = (Alarm) bundle.getSerializable("alarm");

        isRunning = true;
        time = TIME_PLAY_EACH_QUESTION;
        // set default locale:
        Locale.setDefault(Locale.ENGLISH);

        mTimeValTextView = (TextView) findViewById(R.id.timeVal_textView);
        mTimeValTextView.setText(TIME_PLAY_EACH_QUESTION+"");

        myDialogEndTime = new MyDialogEndTime(this);
        myDialogEndTime.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;

        arcProgress = (ArcProgress)findViewById(R.id.arc_progress);
        arcProgress.setMax(NUMBER_MAX_STEP);
        arcProgress.setProgress(0);


        // initialize accelerometer
        SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelDetector = new AccelerometerDetector(sensorManager);
        mAccelDetector.setStepCountChangeListener(new OnStepCountChangeListener() {
            @Override
            public void onStepCountChange(long eventMsecTime) {
                ++mStepCount;
                //mStepCountTextView.setText(String.valueOf(mStepCount) + "/" + String.valueOf(NUMBER_MAX_STEP) );
                if(arcProgress.getProgress() == NUMBER_MAX_STEP){
                    if (!CountStepActivity.this.isFinishing()) {
                        createSuccessAlertDialog("Thông báo", "Bạn đã vượt qua thử thách thành công!");
                    }

                    stopService(playIntent);
                    CountStepActivity.this.finish();
                }
                else {
                    arcProgress.setProgress(mStepCount);
                }
            }
        });

        Thread thread = new Thread(runnable2);
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, PlayRingToneService.class);
            playIntent.putExtra("alarm", alarm);
            startService(playIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);

        // set string values for menu
        String[] titles = getResources().getStringArray(R.array.nav_drawer_items);
        for (int i = 0; i < titles.length; i++) {
            menu.getItem(i).setTitle(titles[i]);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_threshold:
                saveThreshold();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveThreshold() {
        preferences.edit().putFloat(
                PREFERENCES_VALUES_THRESHOLD_KEY,
                (float) AccelerometerProcessing.getInstance().getThresholdValue()).apply();
    }

    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            while (isRunning && time > -1) {
                time--;
                Message message = new Message();
                message.what = TIME_REMAIN_EACH_QUESTION;
                message.arg1 = time;
                message.setTarget(mHandler);
                message.sendToTarget();
                if (time == 0) {
                    isRunning = false;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        alarmActive = true;
        mAccelDetector.startDetector();
    }

    @Override
    public void onBackPressed() {
        if (!alarmActive)
            super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StaticWakeLock.lockOff(this);
        mAccelDetector.stopDetector();
    }

    @Override
    protected void onDestroy() {
        if (myDialogEndTime.isShowing()) {
            myDialogEndTime.dismiss();
        }
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop");
    }
}
