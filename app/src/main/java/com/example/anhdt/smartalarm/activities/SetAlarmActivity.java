package com.example.anhdt.smartalarm.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.GenericTranscodeRequest;
import com.example.anhdt.smartalarm.R;
import com.example.anhdt.smartalarm.database.Database;
import com.example.anhdt.smartalarm.models.Alarm;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.Calendar;

public class SetAlarmActivity extends BaseActivity implements OnClickListener{

    private Alarm alarm;
    private MediaPlayer mediaPlayer;

    private TimePicker timePicker;

    private TextView tvCancel;
    private TextView tvDelete;
    private RelativeLayout relativeChooseChallange;
    private TextView tvChallengeName;

    private TextView btn_T2;
    private TextView btn_T3;
    private TextView btn_T4;
    private TextView btn_T5;
    private TextView btn_T6;
    private TextView btn_T7;
    private TextView btn_CN;

    private EditText edTypeLabel;
    private TextView text_chose_ringstone_music;
    private RelativeLayout relativeChooseRingtone;
    private TextView tvChosenRingtone;

    private boolean[] selectedDays = {false, false, false, false, false, false, false};
    private String[] alarmTones;
    private String[] alarmTonePaths;
    private String[] listChallange = {"Thử thách vận động", "Thử thách tính toán", "Thử thách chụp ảnh"};
    private RelativeLayout saveSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        retriveRingtones();

        checkEditOrCreate();

        initUiComponents();

        createAlarm();

        setOnClick();
    }

    private void checkEditOrCreate() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("alarm")) {
            setMathAlarm((Alarm) bundle.getSerializable("alarm"));
        } else {
            setMathAlarm(new Alarm());
        }
//        if (bundle != null && bundle.containsKey("adapter")) {
//            setListAdapter((AlarmPreferenceListAdapter) bundle.getSerializable("adapter"));
//        } else {
//            setListAdapter(new AlarmPreferenceListAdapter(this, getMathAlarm()));
//        }

    }

    private void initUiComponents() {
        tvCancel = (TextView) findViewById(R.id.Function_Setting_Cancel);
        tvDelete = (TextView) findViewById(R.id.Function_Setting_Delete);
        timePicker = (TimePicker) findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(true);
        relativeChooseChallange = (RelativeLayout) findViewById(R.id.Head_Setting_Time);
        tvChallengeName = (TextView) findViewById(R.id.GameName);

        btn_T2 = (TextView) findViewById(R.id.btn_T2);
        btn_T3 = (TextView) findViewById(R.id.btn_T3);
        btn_T4 = (TextView) findViewById(R.id.btn_T4);
        btn_T5 = (TextView) findViewById(R.id.btn_T5);
        btn_T6 = (TextView) findViewById(R.id.btn_T6);
        btn_T7 = (TextView) findViewById(R.id.btn_T7);
        btn_CN = (TextView) findViewById(R.id.btn_CN);

        edTypeLabel = (EditText) findViewById(R.id.edTypeLabel);
        relativeChooseRingtone = (RelativeLayout) findViewById(R.id.relativeChooseRingTone);
        tvChosenRingtone = (TextView) findViewById(R.id.text_chose_ringstone_music);

        saveSetting = (RelativeLayout) findViewById(R.id.Save_Setting_Alarm);

        if (getMathAlarm().getId() > 0) {
            setSelectedDaysIfExistAlarm();
            edTypeLabel.setText(getMathAlarm().getAlarmName());
        }
        else {
            getMathAlarm().setChallenge(Alarm.Challenge.CALCULATION);
            if (alarmTones.length > 1) {
                getMathAlarm().setAlarmTonePath(alarmTonePaths[1]);
            }
            else {
                getMathAlarm().setAlarmTonePath(alarmTonePaths[0]);
            }
        }
        tvChallengeName.setText(getMathAlarm().getChallenge().toString());
        Uri uri = Uri.parse(getMathAlarm().getAlarmTonePath());
        tvChosenRingtone.setText(getFileName(uri));
        setSelectDays(selectedDays);



    }

    private void setOnClick() {
        tvCancel.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        btn_T2.setOnClickListener(this);
        btn_T3.setOnClickListener(this);
        btn_T4.setOnClickListener(this);
        btn_T5.setOnClickListener(this);
        btn_T6.setOnClickListener(this);
        btn_T7.setOnClickListener(this);
        btn_CN.setOnClickListener(this);
        relativeChooseRingtone.setOnClickListener(this);
        relativeChooseChallange.setOnClickListener(this);
        handleTimePicker();
        saveSetting.setOnClickListener(this);
    }

    private void createAlarm() {

    }

    private void setSelectedDaysIfExistAlarm() {
        Alarm.Day[] days = alarm.getDays();
        for (Alarm.Day day: days) {
            switch (day) {
                case MONDAY:
                    selectedDays[1] = true;
                    break;
                case TUESDAY:
                    selectedDays[2] = true;
                    break;
                case WEDNESDAY:
                    selectedDays[3] = true;
                    break;
                case THURSDAY:
                    selectedDays[4] = true;
                    break;
                case FRIDAY:
                    selectedDays[5] = true;
                    break;
                case SATURDAY:
                    selectedDays[6] = true;
                    break;
                case SUNDAY:
                    selectedDays[0] = true;
                    break;

            }
        }
    }

    private void setSelectDays(boolean selectedDays[]) {
        btn_T2.setSelected(selectedDays[1]);
        btn_T3.setSelected(selectedDays[2]);
        btn_T4.setSelected(selectedDays[3]);
        btn_T5.setSelected(selectedDays[4]);
        btn_T6.setSelected(selectedDays[5]);
        btn_T7.setSelected(selectedDays[6]);
        btn_CN.setSelected(selectedDays[0]);
    }

    private void handleTimePicker() {

        if (getMathAlarm().getId() > 0 && android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            timePicker.setCurrentHour(getMathAlarm().getAlarmTime().get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(getMathAlarm().getAlarmTime().get(Calendar.MINUTE));
        }
        else if (getMathAlarm().getId() > 0 && android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
            timePicker.setHour(getMathAlarm().getAlarmTime().get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(getMathAlarm().getAlarmTime().get(Calendar.MINUTE));
        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Calendar now = Calendar.getInstance();
                now.set(Calendar.HOUR_OF_DAY, hourOfDay);
                now.set(Calendar.MINUTE, minute);
                now.set(Calendar.SECOND, 0);
                alarm.setAlarmTime(now);


            }
        });

    }

    private CountDownTimer alarmToneTimer;

    private void chooseChallenge() {

        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);
        Alarm.Challenge[] items = Alarm.Challenge.values();
        alert.setTitle("Chọn thử thách");
        alert.setItems(listChallange, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getMathAlarm().setChallenge(Alarm.Challenge.values()[which]);
                tvChallengeName.setText(listChallange[which]);
            }
        });
        alert.show();
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void chooseRingtone() {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);

        alert.setTitle("Chọn nhạc chuông");
        alert.setItems(alarmTones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alarm.setAlarmTonePath(alarmTonePaths[which]);
                tvChosenRingtone.setText(alarmTones[which]);
                if (alarm.getAlarmTonePath() != null) {
                    if (mediaPlayer == null) {
                        mediaPlayer = new MediaPlayer();
                    } else {
                        if (mediaPlayer.isPlaying())
                            mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                    try {
                        mediaPlayer.setVolume(2.0f, 2.0f);
                        //mediaPlayer.setVolume(0.2f, 0.2f);
                        mediaPlayer.setDataSource(SetAlarmActivity.this, Uri.parse(alarm.getAlarmTonePath()));
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        // Force the mediaPlayer to stop after 5
                        // seconds...
                        if (alarmToneTimer != null)
                            alarmToneTimer.cancel();
                        alarmToneTimer = new CountDownTimer(5000, 5000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                try {
                                    if (mediaPlayer.isPlaying())
                                        mediaPlayer.stop();
                                } catch (Exception e) {

                                }
                            }
                        };
                        alarmToneTimer.start();
                    } catch (Exception e) {
                        try {
                            if (mediaPlayer.isPlaying())
                                mediaPlayer.stop();
                        } catch (Exception e2) {

                        }
                    }
                }
            }
        });

        alert.show();
    }

    private void retriveRingtones() {
        RingtoneManager ringtoneMgr = new RingtoneManager(this);

        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);

        Cursor alarmsCursor = ringtoneMgr.getCursor();

        alarmTones = new String[alarmsCursor.getCount()+1];
        alarmTones[0] = "Silent";
        alarmTonePaths = new String[alarmsCursor.getCount()+1];
        alarmTonePaths[0] = "";

        if (alarmsCursor.moveToFirst()) {
            do {
                alarmTones[alarmsCursor.getPosition()+1] = ringtoneMgr.getRingtone(alarmsCursor.getPosition()).getTitle(this);
                alarmTonePaths[alarmsCursor.getPosition()+1] = ringtoneMgr.getRingtoneUri(alarmsCursor.getPosition()).toString();
            }while(alarmsCursor.moveToNext());
        }
        Log.d("SetAlarmActivity", "Finished Loading " + alarmTones.length + " Ringtones.");
        alarmsCursor.close();
    }

    public Alarm getMathAlarm() {
        return alarm;
    }

    public void setMathAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("alarm", getMathAlarm());
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mediaPlayer != null)
                mediaPlayer.release();
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Head_Setting_Time:
                //change game
                chooseChallenge();
                break;
            case R.id.relativeChooseRingTone:
                chooseRingtone();
                break;

            case R.id.Function_Setting_Cancel:
                this.finish();
                break;

            case R.id.Function_Setting_Delete:
                break;

            case R.id.btn_T2:
                selectedDays[1] = !selectedDays[1];
                btn_T2.setSelected(selectedDays[1]);
                break;
            case R.id.btn_T3:
                selectedDays[2] = !selectedDays[2];
                btn_T3.setSelected(selectedDays[2]);

                break;
            case R.id.btn_T4:
                selectedDays[3] = !selectedDays[3];
                btn_T4.setSelected(selectedDays[3]);

                break;
            case R.id.btn_T5:
                selectedDays[4] = !selectedDays[4];
                btn_T5.setSelected(selectedDays[4]);

                break;
            case R.id.btn_T6:
                selectedDays[5] = !selectedDays[5];
                btn_T6.setSelected(selectedDays[5]);

                break;
            case R.id.btn_T7:
                selectedDays[6] = !selectedDays[6];
                btn_T7.setSelected(selectedDays[6]);

                break;
            case R.id.btn_CN:
                selectedDays[0] = !selectedDays[0];
                btn_CN.setSelected(selectedDays[0]);
                break;
            case R.id.Save_Setting_Alarm:
                alarm.setAlarmName(edTypeLabel.getText().toString());
                Alarm.Day[] days = Alarm.Day.values();

                Calendar time = Calendar.getInstance();
                time.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                time.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                Calendar now = Calendar.getInstance();
                if (time.after(now)) {
                    switch (now.get(Calendar.DAY_OF_WEEK)) {
                        case Calendar.MONDAY:
                            selectedDays[1] = true;
                            break;
                        case Calendar.TUESDAY:
                            selectedDays[2] = true;
                            break;
                        case Calendar.WEDNESDAY:
                            selectedDays[3] = true;
                            break;
                        case Calendar.THURSDAY:
                            selectedDays[4] = true;
                            break;
                        case Calendar.FRIDAY:
                            selectedDays[5] = true;
                            break;
                        case Calendar.SATURDAY:
                            selectedDays[6] = true;
                            break;
                        case Calendar.SUNDAY:
                            selectedDays[0] = true;
                            break;

                    }
                }
                else {
                    switch (now.get(Calendar.DAY_OF_WEEK)) {
                        case Calendar.MONDAY:
                            selectedDays[2] = true;
                            break;
                        case Calendar.TUESDAY:
                            selectedDays[3] = true;
                            break;
                        case Calendar.WEDNESDAY:
                            selectedDays[4] = true;
                            break;
                        case Calendar.THURSDAY:
                            selectedDays[5] = true;
                            break;
                        case Calendar.FRIDAY:
                            selectedDays[6] = true;
                            break;
                        case Calendar.SATURDAY:
                            selectedDays[0] = true;
                            break;
                        case Calendar.SUNDAY:
                            selectedDays[1] = true;
                            break;

                    }
                }

                for (int i = 0; i < selectedDays.length; i++) {
                    alarm.removeDay(days[i]);
                    if (selectedDays[i]) {
                        alarm.addDay(days[i]);
                    }
                }

                alarm.setChallenge(getMathAlarm().getChallenge());
                alarm.setDifficulty(Alarm.Difficulty.EASY);
                Database.init(getApplicationContext());
                if (getMathAlarm().getId() < 1) {
                    Database.create(getMathAlarm());
                } else {
                    Database.update(getMathAlarm());
                }
                callMathAlarmScheduleService();
                Toast.makeText(this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
                finish();

                break;
        }
    }
}
