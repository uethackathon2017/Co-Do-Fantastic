package com.example.anhdt.smartalarm.countstep.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.anhdt.smartalarm.R;

/**
 * Created by anhdt on 3/11/2017.
 */

public class MyDialogEndTime extends Dialog implements View.OnClickListener{

    private Button mBtnOk;

    public MyDialogEndTime(Context context) {

        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_endtime);
        initView();
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    private void initView() {
        mBtnOk = (Button) findViewById(R.id.bt_ok_end_time);
        mBtnOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok_end_time:
                dismiss();
                break;
        }
    }

}
