package com.team_bona.furymonkey.tensecondsplanktimer.Timer.TimerPicker;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team_bona.furymonkey.tensecondsplanktimer.R;
import com.team_bona.furymonkey.tensecondsplanktimer.utils.FontClass;

/**
 * Created by furymonkey on 2017-03-08.
 */
public class CustomPicker extends LinearLayout{
    private int mMax = 0;
    private int mMin = 0;
    private int mCur = 0;
    private int mGap = 0;

    private LayoutInflater mInflater;
    private TextView mBtnUp, mBtnDown;
    private TextView mTextViewContents;

    public CustomPicker(Context context) {
        this(context, null);
    }

    public CustomPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setMaxValue(final int max){ mMax = max; }

    public void setMinValue(final int min){ mMin = min; }

    public void setGapValue(final int gap){ mGap = gap; }

    public void setCur(final int cur){
        mCur = cur;
        setCurDisplay();
    }

    public void setCurDisplay(){
        mTextViewContents.setText(String.format("%02d", mCur));
    }

    public int getCur(){
        return mCur;
    }

    private void initView(){
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout viewGroup = (LinearLayout) mInflater.inflate(R.layout.item_custom_picker, this, false);
        addView(viewGroup);

        mBtnUp = (TextView) findViewById(R.id.customPickerUpperBtn);
        mBtnDown = (TextView) findViewById(R.id.customPickerLowerBtn);
        mTextViewContents = (TextView) findViewById(R.id.customPickerContents);
        mTextViewContents.setTypeface(FontClass.thin);

        mBtnUp.setOnTouchListener(new OnRepeatTouchListener(300, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mCur + mGap > mMax)
                            mCur = mCur + mGap - mMax - 1;
                        else
                            mCur += mGap;
                        setCurDisplay();
                    }
        }));

        mBtnDown.setOnTouchListener(new OnRepeatTouchListener(300, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCur - mGap < mMin)
                    mCur = mMax - (mCur + mGap - 1);
                else
                    mCur -= mGap;
                setCurDisplay();
            }
        }));
    }
}
