package com.zhimei.liang.utitls;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 张佳亮 on 2015/11/6.
 * 后面的时候不用每次启动程序都要登录注册
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    private static final String  CREATE_USER="create table User " +
            "(name text,password text,address text,phoneNumber text,picture blob)";


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists User");
        onCreate(db);
    }
}
