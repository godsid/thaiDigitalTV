package com.webmanagement.thaidigitaltv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by SystemDLL on 23/9/2557.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "db_thaitvdigital";
    private static final int DB_VERSION = 3;
    private static final String DB_CREATE = "create table tb_channel_favorite(chan_id integer primary key,time_before text not null);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d("run","DB CREATED");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
        Log.d("run","Table CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("run","old :"+oldVersion+" new :"+newVersion);
        db.execSQL("DROP TABLE IF EXISTS tb_channel_favorite");
        db.execSQL(DB_CREATE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
