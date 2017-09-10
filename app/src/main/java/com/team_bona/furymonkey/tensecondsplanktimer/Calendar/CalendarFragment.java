package com.team_bona.furymonkey.tensecondsplanktimer.Calendar;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.team_bona.furymonkey.tensecondsplanktimer.R;
import com.team_bona.furymonkey.tensecondsplanktimer.utils.DataBase.DataBaseOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by furymonkey on 2017-02-20.
 */
public class CalendarFragment extends Fragment {
    private final SimpleDateFormat mAchieveYearFormat = new SimpleDateFormat("yyyy");
    private final SimpleDateFormat mAchieveMonthFormat = new SimpleDateFormat("MM");
    private final SimpleDateFormat mAchieveDateFormat = new SimpleDateFormat("dd");
    private final SimpleDateFormat mListAboveDateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private final SimpleDateFormat mMonthFormat = new SimpleDateFormat("M");

    private int mStartDayIndex = 0;
    private int mLastDayIndex = 0;

    private TextView tvDate;                     // TextView that shows month & year
    private TextView mSelectedDate;
    private GridAdapter mGridAdapter;           // GridView Adapter
    private AchieveListAdapter mListAdapter;
    private ArrayList<String> mDayList = new ArrayList<String>();   // Array that store date info
    private boolean[] mMonthAchieveList = new boolean[31];
    private ArrayList<CalendarInfoClass> mAchieveList = new ArrayList<CalendarInfoClass>();
    private GridView mGridView;            // GridView that print out calendar
    private Calendar mCalendar;            // Calendar variable
    private TextView mBtnPrev;
    private TextView mBtnNext;
    private ListView mListView;            // List that shows how many achieve
    private DataBaseOpenHelper mDataBaseOpenHelper;      // DataBase helper to store List of date
    private Cursor mCursor;                // Cursor to attach Database
    private Date mCurDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        mCalendar = Calendar.getInstance();
        mDataBaseOpenHelper = new DataBaseOpenHelper(getContext());
        mDataBaseOpenHelper.open();

        tvDate = (TextView) rootView.findViewById(R.id.month);
        mGridView = (GridView) rootView.findViewById(R.id.calendar);
        mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

        mBtnPrev = (TextView) rootView.findViewById(R.id.btn_prev);
        mBtnNext = (TextView) rootView.findViewById(R.id.btn_next);

        mSelectedDate = (TextView) rootView.findViewById(R.id.selected_date);

        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPrev();
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNext();
            }
        });

        // 달력의 형태를 만들어준다. - 해당 달의 날짜 / 요일 / 일 ~ 토 까지 글자
        setCalendar(System.currentTimeMillis());
        // 해당 달의 운동을 1회 이상 한 날짜들을 체크하여 달력에 표시해준다.
        setMonthAchieveList();
        // Thread monthThread = new MonthThread(mMonthAchieveList);
        // monthThread.start();

        mGridAdapter = new GridAdapter(rootView.getContext(), mDayList, mMonthAchieveList);
        setIndex();
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isSelected(position);
            }
        });
        mGridView.setItemChecked(mStartDayIndex + mCalendar.get(Calendar.DATE), true);

        setCurrentAchieveList();
        // Thread achieveThread = new AchieveThread(mAchieveList);
        // achieveThread.start();

        mListAdapter = new AchieveListAdapter(rootView.getContext());
        mListAdapter.setData(mAchieveList);
        mListView = (ListView) rootView.findViewById(R.id.achieve_list);
        mListView.setAdapter(mListAdapter);
        return rootView;
    }

    private void isSelected(int position){
        if(mSelectedDate != null && mGridAdapter != null){
            if(position > 6 && position < mStartDayIndex){
                setPrev();
            } else if(position >= mStartDayIndex && position < mLastDayIndex) {
                mCalendar.set(Calendar.DATE, Integer.valueOf(mGridAdapter.getItem(position)));
                mSelectedDate.setText(mListAboveDateFormat.format(new Date(mCalendar.getTimeInMillis())));
                mCurDate.setTime(mCalendar.getTimeInMillis());
                setCurrentAchieveList();
                // Thread achieveThread = new AchieveThread(mAchieveList);
                // achieveThread.start();
                mListAdapter.setData(mAchieveList);
                mListAdapter.notifyDataSetChanged();
            } else if(position >= mLastDayIndex)
                setNext();
        }
    }

    private void setPrev(){
        mCalendar.set(Calendar.MONTH, mCalendar.get(mCalendar.MONTH) - 1);
        setCalendar(mCalendar.getTimeInMillis());
        setMonthAchieveList();
        //Thread monthThread = new MonthThread(mMonthAchieveList);
        //monthThread.start();
        setIndex();
        mGridAdapter.setList(mDayList, mMonthAchieveList);
        mGridView.invalidateViews();
        mGridView.setAdapter(mGridAdapter);
    }

    private void setNext(){
        mCalendar.set(Calendar.MONTH, mCalendar.get(mCalendar.MONTH) + 1);
        setCalendar(mCalendar.getTimeInMillis());
        setMonthAchieveList();
        // Thread monthThread = new MonthThread(mMonthAchieveList);
        // monthThread.start();
        setIndex();
        mGridAdapter.setList(mDayList, mMonthAchieveList);
        mGridView.invalidateViews();
        mGridView.setAdapter(mGridAdapter);
    }

    // 달력의 형태를 만들어준다. - 해당 달의 날짜 / 요일 / 일 ~ 토 까지 글자
    private void setCalendar(long timeInMillis){
        //gridview 요일 표시
        mDayList.clear();
        mDayList.add("일");
        mDayList.add("월");
        mDayList.add("화");
        mDayList.add("수");
        mDayList.add("목");
        mDayList.add("금");
        mDayList.add("토");

        // 현재 날짜로 설정한다.
        final Date date = new Date(timeInMillis);
        mCurDate = new Date(timeInMillis);

        // 현재 달을 달력에 표시해준다.
        tvDate.setText(mMonthFormat.format(date));

        // 이번 달의 시작 날짜를 확인한다.
        mCalendar.set(Calendar.DATE, mCalendar.getActualMinimum(Calendar.DATE));
        int startDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

        // 일 부터 토까지도 설정하고 해당 설정이 0 ~ 6의 Index를 가지므로 6을 더해준다
        // 그 이후 마지막 날에 대한 Index를 설정해준다.
        mStartDayIndex = startDayOfWeek + 6;
        mLastDayIndex = mStartDayIndex + mCalendar.getActualMaximum(Calendar.DATE);

        // 이전 날짜를 투명하게 표시하기 위해 이전 날짜에 대한 정보들을 가져온다.
        mCalendar.set(Calendar.MONTH, mCalendar.get(Calendar.MONTH) - 1);

        for (int i = startDayOfWeek - 1; i > 0; i--)
            // Since Date start from 0 to maximum date - 1, add 1 to show precisely
            mDayList.add(Integer.toString(mCalendar.getActualMaximum(Calendar.DATE) - i + 1));

        setCalendarDate(mCalendar.get(Calendar.MONTH) + 1);

        // Set Previous Month Data
        mCalendar.set(Calendar.MONTH, mCalendar.get(Calendar.MONTH) + 1);
        mCalendar.set(Calendar.DATE, mCalendar.getActualMinimum(Calendar.DATE));

        for (int i = 1; i <= 8 - mCalendar.get(Calendar.DAY_OF_WEEK); i++)
            mDayList.add(Integer.toString(i));

        // Roll back to current date
        mCalendar.setTimeInMillis(timeInMillis);
    }

    private void setCalendarDate(int month) {
        mCalendar.set(Calendar.MONTH, month);

        for (int i = 0; i < mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++)
            mDayList.add("" + (i + 1));
    }


    // 해당 달의 운동을 1회 이상 한 날짜들을 체크하여 달력에 표시해준다.
    private void setMonthAchieveList(){
        // mMonthAchieveList Array를 초기화시켜준다.
        for(int i=0; i<mMonthAchieveList.length; i++)
            mMonthAchieveList[i] = false;

        mCursor = mDataBaseOpenHelper.getMatchMonth(mAchieveYearFormat.format(mCurDate), mAchieveMonthFormat.format(mCurDate));
        CalendarInfoClass mCalendarInfoClass;

        while (mCursor.moveToNext()) {
            mCalendarInfoClass = new CalendarInfoClass(
                    mCursor.getString(mCursor.getColumnIndex("YEAR")),
                    mCursor.getString(mCursor.getColumnIndex("MONTH")),
                    mCursor.getString(mCursor.getColumnIndex("DATE")),
                    mCursor.getString(mCursor.getColumnIndex("ACHIEVE")),
                    mCursor.getString(mCursor.getColumnIndex("AT_THAT_TIME"))
            );
            mMonthAchieveList[Integer.parseInt(mCalendarInfoClass.date) - 1] = true;
        }

        mCursor.close();
    }

    private void setCurrentAchieveList(){
        mAchieveList.clear();

        mCursor = mDataBaseOpenHelper.getMatchDate(mAchieveDateFormat.format(mCurDate));
        CalendarInfoClass mCalendarInfoClass;

        while (mCursor.moveToNext()) {
            mCalendarInfoClass = new CalendarInfoClass(
                    mCursor.getString(mCursor.getColumnIndex("YEAR")),
                    mCursor.getString(mCursor.getColumnIndex("MONTH")),
                    mCursor.getString(mCursor.getColumnIndex("DATE")),
                    mCursor.getString(mCursor.getColumnIndex("ACHIEVE")),
                    mCursor.getString(mCursor.getColumnIndex("AT_THAT_TIME"))
            );
            mAchieveList.add(mCalendarInfoClass);
        }
        mCursor.close();
    }

    private void setIndex(){
        mGridAdapter.setStartDayIndex(mStartDayIndex);
        mGridAdapter.setLastDayIndex(mLastDayIndex);
    }

    /*
    private class MonthThread extends Thread{
        private boolean[] mMonthAchieveList = new boolean[31];

        public MonthThread(boolean[] monthAchieveList){
            mMonthAchieveList = monthAchieveList;
        }

        @Override
        public void run() {
            for(int i=0; i<mMonthAchieveList.length; i++)
                mMonthAchieveList[i] = false;

            mCursor = mDataBaseOpenHelper.getMatchMonth(mAchieveYearFormat.format(mCurDate), mAchieveMonthFormat.format(mCurDate));
            CalendarInfoClass mCalendarInfoClass;

            while (mCursor.moveToNext()) {
                mCalendarInfoClass = new CalendarInfoClass(
                        mCursor.getString(mCursor.getColumnIndex("YEAR")),
                        mCursor.getString(mCursor.getColumnIndex("MONTH")),
                        mCursor.getString(mCursor.getColumnIndex("DATE")),
                        mCursor.getString(mCursor.getColumnIndex("ACHIEVE")),
                        mCursor.getString(mCursor.getColumnIndex("AT_THAT_TIME"))
                );
                mMonthAchieveList[Integer.parseInt(mCalendarInfoClass.date) - 1] = true;
            }

            mCursor.close();
        }
    }

    private class AchieveThread extends Thread{
        private ArrayList<CalendarInfoClass> mAchieveList = new ArrayList<CalendarInfoClass>();

        public AchieveThread(ArrayList<CalendarInfoClass> achieveList){
            mAchieveList = achieveList;
        }

        @Override
        public void run() {
            mAchieveList.clear();

            mCursor = mDataBaseOpenHelper.getMatchDate(mAchieveDateFormat.format(mCurDate));
            CalendarInfoClass mCalendarInfoClass;

            while (mCursor.moveToNext()) {
                mCalendarInfoClass = new CalendarInfoClass(
                        mCursor.getString(mCursor.getColumnIndex("YEAR")),
                        mCursor.getString(mCursor.getColumnIndex("MONTH")),
                        mCursor.getString(mCursor.getColumnIndex("DATE")),
                        mCursor.getString(mCursor.getColumnIndex("ACHIEVE")),
                        mCursor.getString(mCursor.getColumnIndex("AT_THAT_TIME"))
                );
                mAchieveList.add(mCalendarInfoClass);
            }

            mCursor.close();
        }
    }
    */
}
