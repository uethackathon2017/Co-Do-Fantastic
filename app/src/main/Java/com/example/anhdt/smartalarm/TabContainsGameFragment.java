package com.example.anhdt.smartalarm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anhdt.smartalarm.R;

/**
 * Created by Admin on 10/03/2017.
 */

public class TabContainsGameFragment extends Fragment {


    public TabContainsGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_contain_game, container, false);
    }

}
