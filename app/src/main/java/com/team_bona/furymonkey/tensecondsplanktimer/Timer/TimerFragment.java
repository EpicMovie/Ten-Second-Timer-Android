package com.team_bona.furymonkey.tensecondsplanktimer.Timer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.team_bona.furymonkey.tensecondsplanktimer.R;
import com.team_bona.furymonkey.tensecondsplanktimer.Timer.TimerPicker.TimerPickerDialog;

import java.util.Timer;
import java.util.concurrent.RunnableFuture;

/**
 * Created by furymonkey on 2017-02-20.
 */
public class TimerFragment extends Fragment {
    private boolean mPausePressed = false;
    private boolean isRunning = false;

    private TimerPickerDialog mTimerPickerDialog;
    private TimerView mTimerView;
    private ViewPager mViewPager;
    private Context mContext;
    private TimerFragment mTimerFragment;

    public TimerFragment(){
        mTimerFragment = this;
    }

    public void setViewPager(ViewPager viewPager){
        mViewPager = viewPager;
    }

    public void setIsRunning(boolean isRunning){
        this.isRunning = isRunning;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();

        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        mTimerView = (TimerView) rootView.findViewById(R.id.timer);
        final LinearLayout timerLinearLayout = (LinearLayout) rootView.findViewById(R.id.timer_linear_layout);
        final RelativeLayout pauseRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.pause_relative_layout);

        timerLinearLayout.setVisibility(View.VISIBLE);
        pauseRelativeLayout.setVisibility(View.GONE);

        mTimerView.setOnCompleteListener(new TimerView.OnCompleteListener() {
            @Override
            public void onComplete() {
                mViewPager.setCurrentItem(1);
            }
        });

        /*
        final ImageButton tenSecondsMoreButton = (ImageButton) rootView.findViewById(R.id.btn_ten_more);
        tenSecondsMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerView.setisTenClicked(true);
                tenSecondsMoreButton.setVisibility(View.VISIBLE);
            }
        });*/
        /*
        timerView.setOnTenSecondsLeftListener(new TimerView.OnTenSecondsLeftListener(){
                @Override
                public void onLeft() {
                if(timerView.getPassedTime() > (mTimerView.getTotalTime() - 10) && !mTimerView.getisTenClicked())
                    tenSecondsMoreButton.setVisibility(View.VISIBLE);
                }
        });
        */

        mTimerView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    if(!isRunning) {
                        mTimerPickerDialog = new TimerPickerDialog(mContext, mTimerFragment, mTimerView);
                        mTimerPickerDialog.show();
                    } else if (isRunning && !mPausePressed) {
                        pauseRelativeLayout.setVisibility(View.VISIBLE);
                        timerLinearLayout.setAlpha(0.4f);
                        mPausePressed = true;
                        mTimerView.pause();
                    } else if(isRunning && mPausePressed){
                        pauseRelativeLayout.setVisibility(View.GONE);
                        timerLinearLayout.setAlpha(1f);
                        mPausePressed = false;
                        mTimerView.resume();
                    }
            }});

        return rootView;
    }

    public int getTimerViewPassedTime(){
        if(mTimerView != null)
            return mTimerView.getPassedTime();
        else
            return 0;
    }

    public void stopTimer(){
        isRunning = false;
        mPausePressed = false;
        mTimerView.stop();
    }
}
