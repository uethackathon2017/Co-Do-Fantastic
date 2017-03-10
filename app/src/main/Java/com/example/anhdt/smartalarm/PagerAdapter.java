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
                 MainFragment mainFragment = new MainFragment();
                return mainFragment;
            case 1:
                TabContainsGameFragment tabContainsGameFragment = new TabContainsGameFragment();
                return tabContainsGameFragment;

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
                MainFragment mainFragment = new MainFragment();
                return "Alert";
            case 1:
                TabContainsGameFragment tabContainsGameFragment = new TabContainsGameFragment();
                return "Introduce";

            default:
                return null;
        }
    }
}