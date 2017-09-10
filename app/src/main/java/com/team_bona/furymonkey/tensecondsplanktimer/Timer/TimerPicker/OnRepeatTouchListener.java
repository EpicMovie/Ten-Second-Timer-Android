package com.team_bona.furymonkey.tensecondsplanktimer.Timer.TimerPicker;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by furymonkey on 2017-04-13.
 */

public class OnRepeatTouchListener implements View.OnTouchListener {
    private Handler handler = new Handler();

    private final int mInterval;
    private final View.OnClickListener mOnClickListener;

    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, mInterval);
            mOnClickListener.onClick(downView);
        }
    };

    private View downView;

    /**
     * @param interval The interval after second and subsequent click events
     * @param onClickListener The OnClickListener, that will be called periodically
     */
    public OnRepeatTouchListener(final int interval, View.OnClickListener onClickListener) {
        if (onClickListener == null)
            throw new IllegalArgumentException("null runnable");
        if (interval < 0)
            throw new IllegalArgumentException("negative interval");

        this.mInterval = interval;
        mOnClickListener = onClickListener;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacks(handlerRunnable);
                handler.postDelayed(handlerRunnable, mInterval);
                downView = view;
                downView.setPressed(true);
                mOnClickListener.onClick(view);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handler.removeCallbacks(handlerRunnable);
                downView.setPressed(false);
                downView = null;
                return true;
        }

        return false;
    }
}
