package com.example.anhdt.smartalarm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.anhdt.smartalarm.MainFragment;

/**
 * Created by Admin on 10/03/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new MainFragment();
            case 1:
                return new WeatherFragment();
            case 2:
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
                return "Thời tiết";
            case 2:
                return "Tin tức";
            default:
                return null;
        }
    }
}