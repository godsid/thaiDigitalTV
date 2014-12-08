package com.webmanagement.thaidigitaltv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by SystemDLL on 23/9/2557.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "db_thaitvdigital";
    private static final int DB_VERSION = 1;

    public static final String TB_Favorite = "tb_list_favorite";
    public static final String TB_Category = "tb_category";
    public static final String TB_Channel = "tb_channel";
    public static final String TB_Program = "tb_program";
    public static final String TB_Type = "tb_type";
    public static final String TB_Version = "tb_version";


    private static final String createFavorite = "create table " + TB_Favorite +
            " (list_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "program_id INTEGER," +
            "program_name TEXT NOT NULL," +
            "channel_name TEXT NOT NULL," +
            "time_start TEXT NOT NULL," +
            "time_before INTEGER," +
            "day_id INTEGER," +
            "alarm_repeat INTEGER" +
            ");";

    private static final String createCategory = "create table " + TB_Category +
            " (category_id INTEGER PRIMARY KEY," +
            "category_name TEXT NOT NULL" +
            ");";

    private static final String createChannel = "create table " + TB_Channel +
            " (channel_id INTEGER PRIMARY KEY," +
            "channel_name TEXT NOT NULL," +
            "channel_pic TEXT NOT NULL," +
            "category_id INTEGER" +
            ");";

    private static final String createProgram = "create table " + TB_Program +
            " (prog_id INTEGER PRIMARY KEY," +
            "prog_name TEXT NOT NULL," +
            "time_start TEXT NOT NULL," +
            "time_end TEXT NOT NULL," +
            "day_id INTEGER," +
            "channel_id INTEGER," +
            "type_id INTEGER" +
            ");";

    private static final String createType = "create table " + TB_Type +
            " (type_id INTEGER PRIMARY KEY," +
            "type_name TEXT NOT NULL" +
            ");";

    private static final String createVersion = "create table " + TB_Version +
            " (version_number INTEGER" +
            ");";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d("run", "DB CREATED");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createFavorite);
        db.execSQL(createCategory);
        db.execSQL(createChannel);
        db.execSQL(createProgram);
        db.execSQL(createType);
        db.execSQL(createVersion);

        Log.d("run2", "Table CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TB_Favorite);
        db.execSQL("DROP TABLE IF EXISTS "+TB_Category);
        db.execSQL("DROP TABLE IF EXISTS "+TB_Channel);
        db.execSQL("DROP TABLE IF EXISTS "+TB_Program);
        db.execSQL("DROP TABLE IF EXISTS "+TB_Type);
        db.execSQL("DROP TABLE IF EXISTS "+TB_Version);
        db.execSQL(createFavorite);
        db.execSQL(createCategory);
        db.execSQL(createChannel);
        db.execSQL(createProgram);
        db.execSQL(createType);
        db.execSQL(createVersion);
        Log.d("run2", "oldV :" + oldVersion + " upgradeTO :" + newVersion);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }


}

