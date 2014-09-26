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

    private static final String TB_NAME = "tb_list_favorite";
    private static final String C_list_id = "list_id";
    private static final String C_program_id = "program_id";
    private static final String C_program_name = "program_name";
    private static final String C_type_name = "type_name";
    private static final String C_channel_name = "channel_name";
    private static final String C_time_start = "time_start";
    private static final String C_time_before = "time_before";



    public DatabaseAction(Context context) {

        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long addFavoriteProgram(int pid,String prn,String tyn,String chn,String tis,int tib) {
        ContentValues values = new ContentValues();
        values.put(C_program_id,pid);
        values.put(C_program_name,prn);
        values.put(C_type_name,tyn);
        values.put(C_channel_name,chn);
        values.put(C_time_start,tis);
        values.put(C_time_before,tib);

        return database.insert(TB_NAME,null,values);
    }

    public Cursor readAllFavoriteProgram() {
        Cursor cursor = database.query(true,TB_NAME,new String[]{
                C_list_id,C_program_id,C_program_name,C_type_name,C_channel_name,C_time_start,C_time_before},null,null,null,null,null,null);

        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }


    public int updateFavoriteProgram(String lid,String pid,String prn,String tyn,String chn,String tib) {
        ContentValues values = new ContentValues();
        values.put(C_program_id,pid);
        values.put(C_program_name,prn);
        values.put(C_type_name,tyn);
        values.put(C_channel_name,chn);
        values.put(C_time_start,tib);
        values.put(C_time_before,tib);
        return database.update(TB_NAME,values,C_list_id+"=?",new String[] {lid});
    }

    public boolean deleteFavoriteProgram(String i){
        return database.delete(TB_NAME,C_list_id+"="+i,null) > 0;
    }


    public boolean deleteAllFavoriteProgram(){
        return database.delete(TB_NAME,null,null) > 0;
    }





}
