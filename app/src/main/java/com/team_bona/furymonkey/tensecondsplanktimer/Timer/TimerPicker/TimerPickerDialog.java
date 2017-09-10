package com.team_bona.furymonkey.tensecondsplanktimer.Timer.TimerPicker;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Window;

import com.team_bona.furymonkey.tensecondsplanktimer.Timer.TimerFragment;
import com.team_bona.furymonkey.tensecondsplanktimer.Timer.TimerView;

public class TimerPickerDialog extends AlertDialog {
    private TimerPicker mTimerPicker;

    public TimerPickerDialog(Context context, TimerFragment timerFragment, TimerView timerView) {
        super(context);

        mTimerPicker = new TimerPicker(context);
        mTimerPicker.setTimerView(timerFragment, timerView, this);
        setView(mTimerPicker);
    }
}
