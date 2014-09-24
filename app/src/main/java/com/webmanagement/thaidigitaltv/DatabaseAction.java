package com.webmanagement.thaidigitaltv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SystemDLL on 23/9/2557.
 */
public class DatabaseAction {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private static final String TB_NAME = "tb_channel_favorite";
    private static final String COLUMN_CHANID = "chan_id";
    private static final String COLUMN_TIMEBEFORE = "time_before";

    public DatabaseAction(Context context) {

        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long addFavoriteChannel(int id,String t) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHANID,id);
        values.put(COLUMN_TIMEBEFORE,t);
        return database.insert(TB_NAME,null,values);
    }

    public Cursor readAllFavoriteChannel() {
        Cursor cursor = database.query(true,TB_NAME,new String[]{
                COLUMN_CHANID,COLUMN_TIMEBEFORE},null,null,null,null,null,null);

        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }


    public int updateFavoriteChannel(String i,String t) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHANID,i);
        values.put(COLUMN_TIMEBEFORE,t);
        return database.update(TB_NAME,values,COLUMN_CHANID+"=?",new String[] {i});
    }

    public boolean deleteFavoriteChannel(String i){
        return database.delete(TB_NAME,COLUMN_CHANID+"="+i,null) > 0;
    }






}
