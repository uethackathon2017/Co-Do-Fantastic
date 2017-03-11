package com.example.anhdt.smartalarm.challenges.countstep.accelerometer;

/**
 * Created by anhdt on 3/11/2017.
 */

public enum  AccelerometerSignals {
    MAGNITUDE,
    MOV_AVERAGE
    ;

    public static final int count = AccelerometerSignals.values().length;
    public static final String[] OPTIONS = {"|V|","\\u0394g"};
}
