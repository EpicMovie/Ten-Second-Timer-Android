package com.team_bona.furymonkey.tensecondsplanktimer.utils.DataBase;

import android.provider.BaseColumns;

// DataBase Table
public final class DataBase {
    public static final class CreateDB implements BaseColumns{
        public static final String TABLE_NAME = "ACHIEVEMENT";
        public static final String COL_1 = "ID";
        public static final String COL_2 = "YEAR";
        public static final String COL_3 = "MONTH";
        public static final String COL_4 = "DATE";
        public static final String COL_5 = "ACHIEVE";
        public static final String COL_6 = "AT_THAT_TIME";
        public static final String CREATE =
                "create table "+TABLE_NAME+"("
                        +COL_1+" integer primary key autoincrement, "
                        +COL_2+" text not null , "
                        +COL_3+" text not null , "
                        +COL_4+" text not null , "
                        +COL_5+" text not null , "
                        +COL_6+" text not null );";
    }
}