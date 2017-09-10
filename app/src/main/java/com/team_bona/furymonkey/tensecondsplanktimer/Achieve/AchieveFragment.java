package com.team_bona.furymonkey.tensecondsplanktimer.Achieve;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team_bona.furymonkey.tensecondsplanktimer.R;
import com.team_bona.furymonkey.tensecondsplanktimer.utils.DataBase.DataBaseOpenHelper;
import com.team_bona.furymonkey.tensecondsplanktimer.utils.FontClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by furymonkey on 2017-02-20.
 */

public class AchieveFragment extends Fragment {
    private final SimpleDateFormat mAchieveYearFormat = new SimpleDateFormat("yyyy");
    private final SimpleDateFormat mAchieveMonthFormat = new SimpleDateFormat("MM");
    private final SimpleDateFormat mAchieveDateFormat = new SimpleDateFormat("dd");
    private final SimpleDateFormat mAtThatTimeFormat = new SimpleDateFormat("hh:mm a");

    private int mPassedTime = 0;
    private Calendar mCalendar;
    private CurrentInCircle mCurrentInCircle;

    private Paint mTimeTextPaint;
    private Paint mWordTextPaint;
    private Paint mAboveTextPaint;
    private Paint mAroundCirclePaint;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_achieve, container, false);

        mCalendar = Calendar.getInstance();
        setPaint();

        SharedPreferences sp = getActivity().getSharedPreferences("total_time", Context.MODE_PRIVATE);
        SharedPreferences.Editor temp = sp.edit();
        int totalTime = sp.getInt("total_time", 0);

        SimpleDateFormat aboveStringDateFormat = new SimpleDateFormat("yyyy. MM월 F주차");

        totalTime += mPassedTime;

        temp.putInt("total_time", totalTime);
        temp.commit();

        String today = aboveStringDateFormat.format(new Date(mCalendar.getTimeInMillis()));
        String total = String.format("총 %d초", totalTime);

        TextView textDate = (TextView) rootView.findViewById(R.id.textDate);
        TextView textTotal = (TextView) rootView.findViewById(R.id.textTotal);

        mCurrentInCircle = (CurrentInCircle) rootView.findViewById(R.id.currentInCircle);
        mCurrentInCircle.setPassedTime(mPassedTime);

        textDate.setText(today);
        textDate.setTypeface(FontClass.regular);

        textTotal.setText(total);
        textTotal.setTypeface(FontClass.bold);

        return rootView;
    }

    @Nullable
    public void setPassedTime(int passedTime) {
        mPassedTime = passedTime;

        if(mPassedTime >= 1) {
            InsertThread thread = new InsertThread(getContext(), mPassedTime, mAchieveYearFormat, mAchieveMonthFormat, mAchieveDateFormat, mAtThatTimeFormat);
            thread.start();
        }
    }

    public int getPassedTime(){
        return mPassedTime;
    }

    private void setPaint(){
        mAroundCirclePaint = new Paint();
        mAroundCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.circleColor));
        mAroundCirclePaint.setAntiAlias(true);
        mAroundCirclePaint.setStyle(Paint.Style.STROKE);

        mAboveTextPaint = new Paint();
        mAboveTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.wordColor));
        mAboveTextPaint.setAntiAlias(true);
        mAboveTextPaint.setTextSize(15);

        mTimeTextPaint = new Paint();
        mTimeTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.wordColor));
        mTimeTextPaint.setAntiAlias(true);
        mTimeTextPaint.setTextSize(50);

        mWordTextPaint = new Paint();
        mWordTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.wordColor));
        mWordTextPaint.setAntiAlias(true);
        mWordTextPaint.setTextSize(20);
    }

    private static class InsertThread extends Thread{
        private int mPassedTime;
        private Context mContext;
        private DataBaseOpenHelper mDataBaseOpenHelper;
        private SimpleDateFormat mAchieveYearFormat;
        private SimpleDateFormat mAchieveMonthFormat;
        private SimpleDateFormat mAchieveDateFormat;
        private SimpleDateFormat mAtThatTimeFormat;

        public InsertThread(final Context context, final int passedTime, final SimpleDateFormat... simpleDateFormats){
            mContext = context;
            mPassedTime = passedTime;

            mAchieveYearFormat = simpleDateFormats[0];
            mAchieveMonthFormat = simpleDateFormats[1];
            mAchieveDateFormat = simpleDateFormats[2];
            mAtThatTimeFormat = simpleDateFormats[3];
        }

        @Override
        public void run() {
            Date dateInfo = new Date(System.currentTimeMillis());

            if(mDataBaseOpenHelper == null) {
                mDataBaseOpenHelper = new DataBaseOpenHelper(mContext);
                mDataBaseOpenHelper.open();
            }

            mDataBaseOpenHelper.insertColumn(
                    mAchieveYearFormat.format(dateInfo),
                    mAchieveMonthFormat.format(dateInfo),
                    mAchieveDateFormat.format(dateInfo),
                    Integer.toString(mPassedTime),
                    mAtThatTimeFormat.format(dateInfo));

            mDataBaseOpenHelper.close();
        }
    }
}