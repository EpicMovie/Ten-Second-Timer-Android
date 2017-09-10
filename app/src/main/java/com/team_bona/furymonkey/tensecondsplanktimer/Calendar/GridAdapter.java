package com.team_bona.furymonkey.tensecondsplanktimer.Calendar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.team_bona.furymonkey.tensecondsplanktimer.R;
import com.team_bona.furymonkey.tensecondsplanktimer.utils.FontClass;

import java.util.List;

/**
 * Created by furymonkey on 2017-02-10.
 */
public class GridAdapter extends BaseAdapter {
    private int mStartDayIndex = 0;
    private int mLastDayIndex = 0;
    private List<String> list;
    private LayoutInflater inflater;
    private boolean[] mMonthAchieveList;
    Context mContext;

    public GridAdapter(Context context, List<String> list, boolean[] monthAchieveList) {
        mContext = context;
        this.list = list;
        mMonthAchieveList = monthAchieveList;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<String> list, boolean[] monthAchieveList){
        this.list = list;
        mMonthAchieveList = monthAchieveList;
        notifyDataSetChanged();
    }

    public void setStartDayIndex(int i){ mStartDayIndex = i; }

    public void setLastDayIndex(int i){ mLastDayIndex = i; }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);

            holder = new ViewHolder();
            holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_date_gridview);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder)convertView.getTag();

        holder.tvItemGridView.setText(getItem(position));

        if(position <= 6)
            holder.tvItemGridView.setTypeface(FontClass.bold);
        else
            holder.tvItemGridView.setTypeface(FontClass.regular);

        if(position > 6 && position < mStartDayIndex)
            holder.tvItemGridView.setAlpha(0.5f);

        if(position >= mLastDayIndex)
            holder.tvItemGridView.setAlpha(0.5f);


        if(position >= mStartDayIndex && position < mLastDayIndex) {
            if (mMonthAchieveList[position - mStartDayIndex])
                convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorCalSelector));
        }

        return convertView;
    }
    
    private class ViewHolder {
        TextView tvItemGridView;
    }
}

