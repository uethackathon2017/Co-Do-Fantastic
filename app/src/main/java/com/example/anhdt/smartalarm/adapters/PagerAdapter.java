package com.example.anhdt.smartalarm.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;

import com.example.anhdt.smartalarm.fragments.MainFragment;
import com.example.anhdt.smartalarm.fragments.NewsFragment;
import com.example.anhdt.smartalarm.fragments.WeatherFragment;
import com.example.anhdt.smartalarm.services.GPSTracker;

/**
 * Created by Admin on 10/03/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter{
    int mNumOfTabs;
    private Context context;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new MainFragment();
            case 1:
                return new NewsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Báo thức";
            case 1:
                return "Thời tiết & Tin tức";
            default:
                return null;
        }
    }
}