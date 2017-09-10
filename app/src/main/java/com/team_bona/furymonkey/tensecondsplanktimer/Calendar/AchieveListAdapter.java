package com.team_bona.furymonkey.tensecondsplanktimer.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.team_bona.furymonkey.tensecondsplanktimer.R;
import com.team_bona.furymonkey.tensecondsplanktimer.utils.FontClass;

import java.util.ArrayList;

/**
 * Created by furymonkey on 2017-02-10.
 */
public class AchieveListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    ArrayList<CalendarInfoClass> mAchieveList = new ArrayList<CalendarInfoClass>();

    public AchieveListAdapter(Context context) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<CalendarInfoClass> achieveList){
        mAchieveList = achieveList;
    }

    @Override
    public int getCount() {
        return mAchieveList.size();
    }

    @Override
    public CalendarInfoClass getItem(int i) {
        return mAchieveList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if(view == null)
            view = mInflater.inflate(R.layout.item_list, null);

        TextView achieve = (TextView)view.findViewById(R.id.achieve);
        TextView time = (TextView)view.findViewById(R.id.atThatTime);

        achieve.setText(getItem(i).achieve + "＂");
        achieve.setTypeface(FontClass.bold);
        time.setText(getItem(i).atThatTime);
        time.setTypeface(FontClass.regular);

        return view;
    }
}
