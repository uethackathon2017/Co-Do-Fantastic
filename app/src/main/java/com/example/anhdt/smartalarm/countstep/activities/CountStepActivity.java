package com.example.anhdt.smartalarm.countstep.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anhdt.smartalarm.R;
import com.example.anhdt.smartalarm.countstep.accelerometer.AccelerometerDetector;
import com.example.anhdt.smartalarm.countstep.accelerometer.AccelerometerProcessing;
import com.example.anhdt.smartalarm.countstep.accelerometer.OnStepCountChangeListener;
import com.example.anhdt.smartalarm.countstep.dialogs.MyDialogEndTime;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.Locale;

public class CountStepActivity extends AppCompatActivity {

    private static final int TIME_PLAY_EACH_QUESTION = 10;
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

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_REMAIN_EACH_QUESTION:
                    mTimeValTextView.setText(msg.arg1 + "");
                    if (msg.arg1 == 0) {
                        myDialogEndTime.show();
                        Handler handlerShow = new Handler();
                        handlerShow.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                myDialogEndTime.dismiss();
                            }
                        }, 4000);
                        Toast.makeText(CountStepActivity.this,"Ahihi",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private int time;
    // constant reference
    private final AccelerometerProcessing mAccelerometerProcessing = AccelerometerProcessing.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_step);

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
        // accelerometer graph setup:

        // get and configure text views
//        mStepCountTextView = (TextView)findViewById(R.id.stepcount_textView);
//        mStepCountTextView.setText(String.valueOf(0)+ "/" + String.valueOf(NUMBER_MAX_STEP));

        // initialize accelerometer
        SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelDetector = new AccelerometerDetector(sensorManager);
        mAccelDetector.setStepCountChangeListener(new OnStepCountChangeListener() {
            @Override
            public void onStepCountChange(long eventMsecTime) {
                ++mStepCount;
                //mStepCountTextView.setText(String.valueOf(mStepCount) + "/" + String.valueOf(NUMBER_MAX_STEP) );
                if(arcProgress.getProgress() == NUMBER_MAX_STEP){
                    Toast.makeText(CountStepActivity.this,"CCMM DDAO CHOS",Toast.LENGTH_SHORT).show();
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
        Log.d(TAG, "onResume");
        mAccelDetector.startDetector();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause");
        mAccelDetector.stopDetector();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop");
    }
}
