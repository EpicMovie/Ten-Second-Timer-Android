package com.team_bona.furymonkey.tensecondsplanktimer.Calendar;

/**
 * Created by furymonkey on 2017-02-24.
 */
public class CalendarInfoClass {
    public String year;
    public String month;
    public String date;
    public String achieve;
    public String atThatTime;

    public CalendarInfoClass(){
        this("", "", "", "", "");
    }

    public CalendarInfoClass(String year, String month, String date, String achieve , String atThatTime){
        this.year = year;
        this.month = month;
        this.date = date;
        this.achieve = achieve;
        this.atThatTime = atThatTime;
    }
}