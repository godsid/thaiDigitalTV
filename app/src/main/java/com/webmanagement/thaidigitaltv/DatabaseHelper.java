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
    public static final String TB_NAME = "tb_list_favorite";
    private static final int DB_VERSION = 4;
    private static final String TB_CREATE = "create table "+TB_NAME+
            " (list_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "program_id INTEGER," +
            "program_name TEXT NOT NULL," +
            "type_name TEXT NOT NULL," +
            "channel_name TEXT NOT NULL," +
            "time_start TEXT NOT NULL," +
            "time_before INTEGER" +
            ");";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d("run","DB CREATED");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TB_CREATE);
        Log.d("run2","Table CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // Log.d("run2","old :"+oldVersion+" new :"+newVersion);
      //  db.execSQL("DROP TABLE IF EXISTS tb_list_favorite");
       // db.execSQL(TB_CREATE);
        //onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
