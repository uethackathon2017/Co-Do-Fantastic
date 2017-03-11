package com.example.anhdt.smartalarm.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anhdt.smartalarm.R;
import com.example.anhdt.smartalarm.activities.SetAlarmActivity;
import com.example.anhdt.smartalarm.adapters.AlarmAdapter;
import com.example.anhdt.smartalarm.database.Database;
import com.example.anhdt.smartalarm.models.Alarm;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton addAlarm;
    private RecyclerView recycleListAlarm;
    private Activity activity;
    private AlarmAdapter alarmAdapter;
    private List<Alarm> alarms;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarms = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(alarms, activity);
        updateAlarmList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }
    private void initComponent(View view) {
        addAlarm = (FloatingActionButton) view.findViewById(R.id.add_alarm);
        addAlarm.setOnClickListener(this);
        recycleListAlarm = (RecyclerView) view.findViewById(R.id.recycleListAlarm);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recycleListAlarm.setLayoutManager(linearLayoutManager);
        recycleListAlarm.setAdapter(alarmAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_alarm:
                Intent intent = new Intent(activity, SetAlarmActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAlarmList();
    }

    public void updateAlarmList(){
        Database.init(activity);
        this.alarms.clear();
        List<Alarm> alarms = Database.getAll();
        this.alarms.addAll(alarms);
        alarmAdapter.notifyDataSetChanged();

    }
}
