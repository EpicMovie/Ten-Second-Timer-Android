package com.team_bona.furymonkey.tensecondsplanktimer.utils.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by furymonkey on 2017-01-25.
 */

public class DataBaseOpenHelper {
    private static final String DATABASE_NAME = "achievement.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDataBase;
    private DataBaseHelper mDataBaseHelper;
    private Context mContext;

    private class DataBaseHelper extends SQLiteOpenHelper{
        public DataBaseHelper(Context context, String name,
                               SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBase.CreateDB.CREATE);
        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DataBase.CreateDB.TABLE_NAME);
            onCreate(db);
        }
    }

    public DataBaseOpenHelper(Context context){
        mContext = context;
    }

    public DataBaseOpenHelper open() throws SQLException{
        mDataBaseHelper = new DataBaseHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mDataBase = mDataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDataBase.close();
    }

    public long insertColumn(final String YEAR, final String MONTH, final String DATE, final String ACHIEVEMENT, final String AT_THAT_TIME){
        ContentValues values = new ContentValues();
        values.put(DataBase.CreateDB.COL_2, YEAR);
        values.put(DataBase.CreateDB.COL_3, MONTH);
        values.put(DataBase.CreateDB.COL_4, DATE);
        values.put(DataBase.CreateDB.COL_5, ACHIEVEMENT);
        values.put(DataBase.CreateDB.COL_6, AT_THAT_TIME);
        return mDataBase.insert(DataBase.CreateDB.TABLE_NAME, null, values);
    }

    public boolean updateColumn(long id, final String YEAR, final String MONTH, final String DATE, final String ACHIEVEMENT, final String AT_THAT_TIME){
        ContentValues values = new ContentValues();
        values.put(DataBase.CreateDB.COL_2, YEAR);
        values.put(DataBase.CreateDB.COL_3, MONTH);
        values.put(DataBase.CreateDB.COL_4, DATE);
        values.put(DataBase.CreateDB.COL_5, ACHIEVEMENT);
        values.put(DataBase.CreateDB.COL_6, AT_THAT_TIME);
        return mDataBase.update(DataBase.CreateDB.TABLE_NAME, values, "ID="+id, null) > 0;
    }

    public boolean deleteColumn(long id){
        return mDataBase.delete(DataBase.CreateDB.TABLE_NAME, "ID="+id, null) > 0;
    }

    public boolean deleteColumn(String number){
        return mDataBase.delete(DataBase.CreateDB.TABLE_NAME, "contact="+number, null) > 0;
    }

    public Cursor getAllColumns(){
        return mDataBase.query(DataBase.CreateDB.TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getColumn(final long id){
        Cursor c = mDataBase.query(DataBase.CreateDB.TABLE_NAME, null,
                "ID="+id, null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }

    public Cursor getMatchMonth(final String YEAR, final String MONTH){
        Cursor c = mDataBase.rawQuery( "select * from ACHIEVEMENT where YEAR=" + "'" + YEAR + "' and MONTH=" + "'" + MONTH + "'" , null);
        return c;
    }

    public Cursor getMatchDate(final String DATE){
        Cursor c = mDataBase.rawQuery( "select * from ACHIEVEMENT where DATE=" + "'" + DATE + "'" , null);
        return c;
    }
}