package com.example.anhdt.smartalarm.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.anhdt.smartalarm.R;
import com.example.anhdt.smartalarm.models.Alarm;
import com.example.anhdt.smartalarm.models.MathProblem;
import com.example.anhdt.smartalarm.services.PlayRingToneService;
import com.example.anhdt.smartalarm.utils.StaticWakeLock;

import java.io.IOException;

public class CaculateActivity extends BaseActivity implements View.OnClickListener {

    private Alarm alarm;
    private MediaPlayer mediaPlayer;

    private StringBuilder answerBuilder = new StringBuilder();

    private MathProblem mathProblem;
    private Vibrator vibrator;

    private boolean alarmActive;

    private TextView problemView;
    private TextView answerView;
    private String answerString;

    //test
    private PlayRingToneService playRingToneService;
    private Intent playIntent;

    private boolean playBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_caculate);

        Bundle bundle = this.getIntent().getExtras();
        alarm = (Alarm) bundle.getSerializable("alarm");

        this.setTitle(alarm.getAlarmName());

        switch (alarm.getDifficulty()) {
            case EASY:
                mathProblem = new MathProblem(3);
                break;
            case MEDIUM:
                mathProblem = new MathProblem(4);
                break;
            case HARD:
                mathProblem = new MathProblem(5);
                break;
        }

        answerString = String.valueOf(mathProblem.getAnswer());
        if (answerString.endsWith(".0")) {
            answerString = answerString.substring(0, answerString.length() - 2);
        }

        problemView = (TextView) findViewById(R.id.textView1);
        problemView.setText(mathProblem.toString());

        answerView = (TextView) findViewById(R.id.textView2);
        answerView.setText("= ?");

        ((Button) findViewById(R.id.Button0)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button1)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button2)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button3)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button4)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button5)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button6)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button7)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button8)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button9)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button_clear)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button_decimal)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button_minus)).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
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
    protected void onResume() {
        super.onResume();
        alarmActive = true;
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
    }



    @Override
    public void onClick(View v) {
        if (!alarmActive)
            return;
        String button = (String) v.getTag();
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        if (button.equalsIgnoreCase("clear")) {
            if (answerBuilder.length() > 0) {
                answerBuilder.setLength(answerBuilder.length() - 1);
                answerView.setText(answerBuilder.toString());
            }
        } else if (button.equalsIgnoreCase(".")) {
            if (!answerBuilder.toString().contains(button)) {
                if (answerBuilder.length() == 0)
                    answerBuilder.append(0);
                answerBuilder.append(button);
                answerView.setText(answerBuilder.toString());
            }
        } else if (button.equalsIgnoreCase("-")) {
            if (answerBuilder.length() == 0) {
                answerBuilder.append(button);
                answerView.setText(answerBuilder.toString());
            }
        } else {
            answerBuilder.append(button);
            answerView.setText(answerBuilder.toString());
            if (isAnswerCorrect()) {
                alarmActive = false;
                stopService(playIntent);
                this.finish();
            }
        }
        if (answerView.getText().length() >= answerString.length()
                && !isAnswerCorrect()) {
            answerView.setTextColor(Color.RED);
        } else {
            answerView.setTextColor(Color.BLACK);
        }
    }

    public boolean isAnswerCorrect() {
        boolean correct = false;
        try {
            correct = mathProblem.getAnswer() == Float.parseFloat(answerBuilder
                    .toString());
        } catch (NumberFormatException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return correct;
    }
}
