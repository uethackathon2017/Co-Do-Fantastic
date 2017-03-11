package com.example.anhdt.smartalarm.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anhdt.smartalarm.R;
import com.example.anhdt.smartalarm.activities.BaseActivity;
import com.example.anhdt.smartalarm.activities.MainActivity;
import com.example.anhdt.smartalarm.activities.SetAlarmActivity;
import com.example.anhdt.smartalarm.database.Database;
import com.example.anhdt.smartalarm.models.Alarm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhdt on 3/11/2017.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private AlarmLongClick mOnClickListener;
    private AlarmClick mOnAlarmClick;
    private List<Alarm> alarms = new ArrayList<Alarm>();
    private Context context;

    public AlarmAdapter(List<Alarm> alarms, Context context) {
        super();
        this.alarms = alarms;
        this.context = context;
    }

    interface AlarmLongClick extends View.OnLongClickListener{
        @Override
        boolean onLongClick(View v);
    }

    interface AlarmClick extends View.OnClickListener {
        @Override
        void onClick(View v);
    }
    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemview = inflater.inflate(R.layout.item_alarm, parent, false);
        itemview.setOnLongClickListener(mOnClickListener);
        itemview.setOnClickListener(mOnAlarmClick);
        return new AlarmViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final AlarmViewHolder holder, final int position) {
        final Alarm alarm = alarms.get(position);
        holder.tvItemAlarmHour.setText(alarm.getAlarmTimeString());
        holder.tvItemAlarmDays.setText(alarm.getRepeatDaysString());
        holder.imgSetAlarm.setSelected(alarm.getAlarmActive());
        holder.imgSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgSetAlarm.setSelected(!holder.imgSetAlarm.isSelected());
                Alarm alarm1 = alarms.get(position);
                alarm1.setAlarmActive(holder.imgSetAlarm.isSelected());
                Database.update(alarm1);
                ((MainActivity) context).callMathAlarmScheduleService();
//                if (checkBox.isChecked()) {
//                    Toast.makeText(AlarmActivity.this, alarm.getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
//                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder  {

        private TextView tvItemAlarmHour;
        private TextView tvItemAlarmDays;
        private ImageView imgSetAlarm;
        private int position;

        public AlarmViewHolder(View view) {
            super(view);
            tvItemAlarmHour = (TextView) view.findViewById(R.id.tvItemAlarmHour);
            tvItemAlarmDays = (TextView) view.findViewById(R.id.tvItemAlarmDays);
            imgSetAlarm = (ImageView) view.findViewById(R.id.imgSetAlarm);
            view.setOnLongClickListener(new AlarmLongClick() {
                @Override
                public boolean onLongClick(View v) {
                    createDialog();
                    return false;
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Alarm alarm = alarms.get(getAdapterPosition());
                    Intent intent = new Intent(context, SetAlarmActivity.class);
                    intent.putExtra("alarm", alarm);
                    context.startActivity(intent);
                }
            });
        }

        private void createDialog() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Xóa báo thức");
            dialog.setMessage("Bạn có muốn xóa báo thức này không?");
            dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Database.init(context);
                    Database.deleteEntry(alarms.get(getAdapterPosition()));
                    ((MainActivity) context).callMathAlarmScheduleService();
//                    position = getAdapterPosition();
//                    alarms.remove(position);
                    updateAlarmList();
                    AlarmAdapter.this.notifyDataSetChanged();
                }
            });
            dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
        public void updateAlarmList(){
            Database.init(context);
            AlarmAdapter.this.alarms.clear();
            List<Alarm> alarms = Database.getAll();
            AlarmAdapter.this.alarms.addAll(alarms);
            AlarmAdapter.this.notifyDataSetChanged();

        }
    }

}
