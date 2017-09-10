package com.team_bona.furymonkey.tensecondsplanktimer.Timer.TimerPicker;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team_bona.furymonkey.tensecondsplanktimer.R;
import com.team_bona.furymonkey.tensecondsplanktimer.Timer.TimerFragment;
import com.team_bona.furymonkey.tensecondsplanktimer.Timer.TimerView;
import com.team_bona.furymonkey.tensecondsplanktimer.utils.FontClass;

public class TimerPicker extends LinearLayout {
    private final CustomPicker mMinutePicker;
    private final CustomPicker mSecondPicker;
    private final Button mButton;
    private TimerView mTimerView;
    private TimerPickerDialog mTimerPickerDialog;

    private Context mContext;

    public TimerPicker(Context context) {
        this(context, null);
    }

    public TimerPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.timer_picker, this, true);

        TextView bunTextView = (TextView) findViewById(R.id.tv_picker_bun);
        TextView choTextView = (TextView) findViewById(R.id.tv_picker_cho);
        bunTextView.setTypeface(FontClass.thin);
        choTextView.setTypeface(FontClass.thin);

        mSecondPicker = (CustomPicker) findViewById(R.id.second_picker);
        mSecondPicker.setMinValue(0);
        mSecondPicker.setMaxValue(59);
        mSecondPicker.setGapValue(10);
        mSecondPicker.setCur(30);

        mMinutePicker = (CustomPicker) findViewById(R.id.minute_picker);
        mMinutePicker.setMinValue(0);
        mMinutePicker.setMaxValue(59);
        mMinutePicker.setGapValue(1);
        mMinutePicker.setCur(0);

        mButton = (Button) findViewById(R.id.btn_picker);
    }

    public void setTimerView(final TimerFragment timerFragment, TimerView timerView, TimerPickerDialog timerPickerDialog){
        this.mTimerView = timerView;
        this.mTimerPickerDialog = timerPickerDialog;

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int time = mMinutePicker.getCur() * 60 + mSecondPicker.getCur();

                if (time != 0) {
                    timerFragment.setIsRunning(true);
                    mTimerView.start(time);
                    mTimerPickerDialog.dismiss();
                } else
                    Toast.makeText(mContext, "시간을 다시 설정해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }
}