package com.example.anhdt.smartalarm.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.example.anhdt.smartalarm.adapters.PagerAdapter;
import com.example.anhdt.smartalarm.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by Admin on 10/03/2017.
 */

public class MainActivity extends BaseActivity {

    private SmartTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (SmartTabLayout) findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(Color.TRANSPARENT);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), 3);

        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);

    }

}