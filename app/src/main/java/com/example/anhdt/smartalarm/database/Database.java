package com.example.anhdt.smartalarm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.anhdt.smartalarm.models.Alarm;
import com.example.anhdt.smartalarm.models.Weather;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhdt on 3/11/2017.
 */

public class Database extends SQLiteOpenHelper{

    static Database instance = null;
    static SQLiteDatabase database = null;

    static final String DATABASE_NAME = "DBAA";

    static final int DATABASE_VERSION = 1;


    public static final String ALARM_TABLE = "alarm";
    public static final String COLUMN_ALARM_ID = "_id";
    public static final String COLUMN_ALARM_ACTIVE = "alarm_active";
    public static final String COLUMN_ALARM_TIME = "alarm_time";
    public static final String COLUMN_ALARM_DAYS = "alarm_days";
    public static final String COLUMN_ALARM_DIFFICULTY = "alarm_difficulty";
    public static final String COLUMN_ALARM_CHALLENGE = "alarm_challenge";
    public static final String COLUMN_ALARM_TONE = "alarm_tone";
    public static final String COLUMN_ALARM_VIBRATE = "alarm_vibrate";
    public static final String COLUMN_ALARM_NAME = "alarm_name";

    public static final String WEATHER_TABLE = "weather";
    public static final String COLUMN_WEATHER_ID = "_idWeather";
    public static final String COLUMN_CITY = "_city";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_TEAMPRATURE = "temprature";
    public static final String COLUMN_CONDITION = "condition";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_SPEEDWIND = "speedwind";
    public static final String COLUMN_TIMEUPDATE = "timeupdate";

    public static void init(Context context) {
        if (null == instance) {
            instance = new Database(context);
        }
    }

    public static SQLiteDatabase getDatabase() {
        if (null == database) {
            database = instance.getWritableDatabase();
        }
        return database;
    }

    public static void deactivate() {
        if (null != database && database.isOpen()) {
            database.close();
        }
        database = null;
        instance = null;
    }

    public static long create(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
        cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(alarm.getDays());
            byte[] buff = bos.toByteArray();

            cv.put(COLUMN_ALARM_DAYS, buff);

        } catch (Exception e){
        }

        cv.put(COLUMN_ALARM_CHALLENGE, alarm.getChallenge().ordinal());
        cv.put(COLUMN_ALARM_DIFFICULTY, alarm.getDifficulty().ordinal());
        cv.put(COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
        cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
        cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());

        return getDatabase().insert(ALARM_TABLE, null, cv);
    }

    public static long createWeather(Weather weather){
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CITY , weather.location.getCity());
        cv.put(COLUMN_COUNTRY , weather.location.getCountry());
        cv.put(COLUMN_ICON ,weather.currentCondition.getIcon());
        cv.put(COLUMN_TEAMPRATURE, weather.temperature.getTemp());
        cv.put(COLUMN_CONDITION, weather.currentCondition.getCondition());
        cv.put(COLUMN_DESCRIPTION , weather.currentCondition.getDescr());
        cv.put(COLUMN_HUMIDITY, weather.currentCondition.getHumidity());
        cv.put(COLUMN_SPEEDWIND,weather.wind.getSpeed());
        cv.put(COLUMN_TIMEUPDATE, weather.currentCondition.getTime());


        return getDatabase().insert(WEATHER_TABLE, null, cv);
    }

    public static int updateWeather(Weather weather){
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CITY , weather.location.getCity());
        cv.put(COLUMN_COUNTRY , weather.location.getCountry());
        cv.put(COLUMN_ICON ,weather.currentCondition.getIcon());
        cv.put(COLUMN_TEAMPRATURE, weather.temperature.getTemp());
        cv.put(COLUMN_CONDITION, weather.currentCondition.getCondition());
        cv.put(COLUMN_DESCRIPTION , weather.currentCondition.getDescr());
        cv.put(COLUMN_HUMIDITY, weather.currentCondition.getHumidity());
        cv.put(COLUMN_SPEEDWIND,weather.wind.getSpeed());
        cv.put(COLUMN_TIMEUPDATE, weather.currentCondition.getTime());

        return getDatabase().update(WEATHER_TABLE, cv, "_idWeather=" + 1, null);
    }

    public static int update(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
        cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(alarm.getDays());
            byte[] buff = bos.toByteArray();

            cv.put(COLUMN_ALARM_DAYS, buff);

        } catch (Exception e){
        }

        cv.put(COLUMN_ALARM_CHALLENGE, alarm.getChallenge().ordinal());
        cv.put(COLUMN_ALARM_DIFFICULTY, alarm.getDifficulty().ordinal());
        cv.put(COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
        cv.put(COLUMN_ALARM_VIBRATE, alarm.getVibrate());
        cv.put(COLUMN_ALARM_NAME, alarm.getAlarmName());

        return getDatabase().update(ALARM_TABLE, cv, "_id=" + alarm.getId(), null);
    }
    public static int deleteEntry(Alarm alarm){
        return deleteEntry(alarm.getId());
    }

    public static int deleteEntry(int id){
        return getDatabase().delete(ALARM_TABLE, COLUMN_ALARM_ID + "=" + id, null);
    }

    public static int deleteAll(){
        return getDatabase().delete(ALARM_TABLE, "1", null);
    }

    public static Alarm getAlarm(int id) {
        // TODO Auto-generated method stub
        String[] columns = new String[] {
                COLUMN_ALARM_ID,
                COLUMN_ALARM_ACTIVE,
                COLUMN_ALARM_TIME,
                COLUMN_ALARM_DAYS,
                COLUMN_ALARM_CHALLENGE,
                COLUMN_ALARM_DIFFICULTY,
                COLUMN_ALARM_TONE,
                COLUMN_ALARM_VIBRATE,
                COLUMN_ALARM_NAME
        };
        Cursor c = getDatabase().query(ALARM_TABLE, columns, COLUMN_ALARM_ID+"="+id, null, null, null,
                null);
        Alarm alarm = null;

        if(c.moveToFirst()){

            alarm =  new Alarm();
            alarm.setId(c.getInt(1));
            alarm.setAlarmActive(c.getInt(2)==1);
            alarm.setAlarmTime(c.getString(3));
            byte[] repeatDaysBytes = c.getBlob(4);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(repeatDaysBytes);
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Alarm.Day[] repeatDays;
                Object object = objectInputStream.readObject();
                if(object instanceof Alarm.Day[]){
                    repeatDays = (Alarm.Day[]) object;
                    alarm.setDays(repeatDays);
                }
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            alarm.setChallenge(Alarm.Challenge.values()[c.getInt(5)]);
            alarm.setDifficulty(Alarm.Difficulty.values()[c.getInt(6)]);
            alarm.setAlarmTonePath(c.getString(7));
            alarm.setVibrate(c.getInt(8)==1);
            alarm.setAlarmName(c.getString(9));
        }
        c.close();
        return alarm;
    }

    public static Weather getWeather(){
        // TODO Auto-generated method stub
        String[] columns = new String[] {
                COLUMN_WEATHER_ID,
                COLUMN_CITY,
                COLUMN_COUNTRY,
                COLUMN_ICON,
                COLUMN_TEAMPRATURE,
                COLUMN_CONDITION,
                COLUMN_DESCRIPTION,
                COLUMN_HUMIDITY,
                COLUMN_SPEEDWIND,
                COLUMN_TIMEUPDATE
        };
        Cursor c = getDatabase().query(WEATHER_TABLE, columns, null, null, null, null,
                null);
        if(c.getCount() == 0){
            Weather weather = null;
            return weather;
        }
        Log.i("COUNT",c.getCount()+"");
        Weather weather = null;

        if(c.moveToFirst()){

            weather =  new Weather();
            weather.location.setCity(c.getString(1));
            weather.location.setCountry(c.getString(2));
            weather.currentCondition.setIcon(c.getString(3));
            weather.temperature.setTemp(c.getFloat(4));
            weather.currentCondition.setCondition(c.getString(5));
            weather.currentCondition.setDescr(c.getString(6));
            weather.currentCondition.setHumidity(c.getFloat(7));
            weather.wind.setSpeed(c.getFloat(8));
            weather.currentCondition.setTime(c.getString(9));
        }
        c.close();
        return weather;
    }

    public static Cursor getCursor() {
        // TODO Auto-generated method stub
        String[] columns = new String[] {
                COLUMN_ALARM_ID,
                COLUMN_ALARM_ACTIVE,
                COLUMN_ALARM_TIME,
                COLUMN_ALARM_DAYS,
                COLUMN_ALARM_CHALLENGE,
                COLUMN_ALARM_DIFFICULTY,
                COLUMN_ALARM_TONE,
                COLUMN_ALARM_VIBRATE,
                COLUMN_ALARM_NAME
        };
        return getDatabase().query(ALARM_TABLE, columns, null, null, null, null,
                null);
    }

    Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ALARM_TABLE + " ( "
                + COLUMN_ALARM_ID + " INTEGER primary key autoincrement, "
                + COLUMN_ALARM_ACTIVE + " INTEGER NOT NULL, "
                + COLUMN_ALARM_TIME + " TEXT NOT NULL, "
                + COLUMN_ALARM_DAYS + " BLOB NOT NULL, "
                + COLUMN_ALARM_CHALLENGE + " INTEGER NOT NULL, "
                + COLUMN_ALARM_DIFFICULTY + " INTEGER NOT NULL, "
                + COLUMN_ALARM_TONE + " TEXT NOT NULL, "
                + COLUMN_ALARM_VIBRATE + " INTEGER NOT NULL, "
                + COLUMN_ALARM_NAME + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + WEATHER_TABLE + " ( "
                + COLUMN_WEATHER_ID + " INTEGER primary key autoincrement, "
                + COLUMN_CITY + " TEXT NOT NULL, "
                + COLUMN_COUNTRY + " TEXT NOT NULL, "
                + COLUMN_ICON + " TEXT NOT NULL, "
                + COLUMN_TEAMPRATURE + " FLOAT NOT NULL, "
                + COLUMN_CONDITION + " TEXT NOT NULL, "
                + COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + COLUMN_HUMIDITY + " FLOAT NOT NULL, "
                + COLUMN_SPEEDWIND + " FLOAT NOT NULL, "
                + COLUMN_TIMEUPDATE + " TEXT NOT NULL)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
        onCreate(db);
    }

    public static List<Alarm> getAll() {
        List<Alarm> alarms = new ArrayList<Alarm>();
        Cursor cursor = Database.getCursor();
        if (cursor.moveToFirst()) {

            do {
                // COLUMN_ALARM_ID,
                // COLUMN_ALARM_ACTIVE,
                // COLUMN_ALARM_TIME,
                // COLUMN_ALARM_DAYS,
                // COLUMN_ALARM_DIFFICULTY,
                // COLUMN_ALARM_TONE,
                // COLUMN_ALARM_VIBRATE,
                // COLUMN_ALARM_NAME

                Alarm alarm = new Alarm();
                alarm.setId(cursor.getInt(0));
                alarm.setAlarmActive(cursor.getInt(1) == 1);
                alarm.setAlarmTime(cursor.getString(2));
                byte[] repeatDaysBytes = cursor.getBlob(3);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                        repeatDaysBytes);
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(
                            byteArrayInputStream);
                    Alarm.Day[] repeatDays;
                    Object object = objectInputStream.readObject();
                    if (object instanceof Alarm.Day[]) {
                        repeatDays = (Alarm.Day[]) object;
                        alarm.setDays(repeatDays);
                    }
                } catch (StreamCorruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                alarm.setChallenge(Alarm.Challenge.values()[cursor.getInt(4)]);
                alarm.setDifficulty(Alarm.Difficulty.values()[cursor.getInt(5)]);
                alarm.setAlarmTonePath(cursor.getString(6));
                alarm.setVibrate(cursor.getInt(7) == 1);
                alarm.setAlarmName(cursor.getString(8));

                alarms.add(alarm);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return alarms;
    }

}
