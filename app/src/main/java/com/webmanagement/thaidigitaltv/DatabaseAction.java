package com.webmanagement.thaidigitaltv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by SystemDLL on 23/9/2557.
 */
public class DatabaseAction {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private static final String TB_Favorite = DatabaseHelper.TB_Favorite;
    private static final String f_list_id = "list_id";
    private static final String f_program_id = "program_id";
    private static final String f_program_name = "program_name";
    private static final String f_channel_name = "channel_name";
    private static final String f_time_start = "time_start";
    private static final String f_time_before = "time_before";
    private static final String f_day_id = "day_id";
    private static final String f_alarm_repeat = "alarm_repeat";

    private static final String TB_Category = DatabaseHelper.TB_Category;
    private static final String category_id = "category_id";
    private static final String category_name = "category_name";

    private static final String TB_Channel = DatabaseHelper.TB_Channel;
    private static final String channel_id = "channel_id";
    private static final String channel_name = "channel_name";
    private static final String channel_pic = "channel_pic";
    private static final String fk_category_id = "category_id";

    private static final String TB_Program = DatabaseHelper.TB_Program;
    private static final String prog_id = "prog_id";
    private static final String prog_name = "prog_name";
    private static final String time_start = "time_start";
    private static final String time_end = "time_end";
    private static final String day_id = "day_id";
    private static final String fk_channel_id = "channel_id";
    private static final String fk_type_id = "type_id";

    private static final String TB_Type = DatabaseHelper.TB_Type;
    private static final String type_id = "type_id";
    private static final String type_name = "type_name";

    private static final String TB_Version = DatabaseHelper.TB_Version;
    private static final String version_number = "version_number";
    


    public DatabaseAction(Context context) {
        try {
            dbHelper = new DatabaseHelper(context);
            database = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            context.deleteDatabase(DatabaseHelper.DB_NAME);
            dbHelper = new DatabaseHelper(context);
            database = dbHelper.getWritableDatabase();
            Log.d("run","Error : DatabaseAction | "+e);
        }

    }

    public long addFavoriteProgram(int pid, String prn, String chn, String tis, int tib, int dai, int alr) {
        ContentValues values = new ContentValues();
        values.put(f_program_id, pid);
        values.put(f_program_name, prn);
        values.put(f_channel_name, chn);
        values.put(f_time_start, tis);
        values.put(f_time_before, tib);
        values.put(f_day_id, dai);
        values.put(f_alarm_repeat, alr);

        return database.insert(TB_Favorite, null, values);
    }

    public Cursor readAllFavoriteProgram() {
        Cursor cursor = database.query(true, TB_Favorite, new String[]{
                f_list_id, f_program_id, f_program_name, f_channel_name, f_time_start, f_time_before, f_day_id, f_alarm_repeat}, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public int updateFavoriteProgram(String lid, String pid, String prn, String chn, String tib, int dai, int alr) {
        ContentValues values = new ContentValues();
        values.put(f_program_id, pid);
        values.put(f_program_name, prn);
        values.put(f_channel_name, chn);
        values.put(f_time_start, tib);
        values.put(f_time_before, tib);
        values.put(f_day_id, dai);
        values.put(f_alarm_repeat, alr);

        return database.update(TB_Favorite, values, f_list_id + "=?", new String[]{lid});
    }

    public boolean deleteFavoriteProgram(int i) {
        return database.delete(TB_Favorite, f_program_id + "=" + i, null) > 0;
    }

    public int countFavoriteProgram() {
        String countQuery = "SELECT  * FROM " + TB_Favorite;
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public Cursor readFavoriteById(int id) {
        String countQuery = "SELECT  * FROM " + TB_Favorite + " WHERE " + f_program_id + "=" + id;
        Cursor cursor = database.rawQuery(countQuery, null);
        return cursor;
    }


    public boolean deleteAllFavoriteProgram() {
        return database.delete(TB_Favorite, null, null) > 0;
    }



    //===================================== Category ============================================

    public long addCategory(int a, String b) {
        ContentValues values = new ContentValues();
        values.put(category_id, a);
        values.put(category_name, b);
        return database.insert(TB_Category, null, values);
    }

    public Cursor readCategory() {
        Cursor cursor = database.query(true, TB_Category, new String[]{
                category_id, category_name}, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    //===================================== Channel ============================================

    public long addChannel(int a, String b, String c,int d) {
        ContentValues values = new ContentValues();
        values.put(channel_id, a);
        values.put(channel_name, b);
        values.put(channel_pic, c);
        values.put(fk_category_id, d);
        return database.insert(TB_Channel, null, values);
    }

    public Cursor readChannel() {
        Cursor cursor = database.query(true, TB_Channel, new String[]{
                channel_id, channel_name, channel_pic, fk_category_id}, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }



//===================================== Programs ============================================

    public long addProgram(int a, String b, String c,String d,int e,int f,int g) {
        ContentValues values = new ContentValues();
        values.put(prog_id, a);
        values.put(prog_name, b);
        values.put(time_start, c);
        values.put(time_end, d);
        values.put(day_id, e);
        values.put(fk_channel_id, f);
        values.put(fk_type_id, g);
        return database.insert(TB_Program, null, values);
    }

    public Cursor readProgram() {
        Cursor cursor = database.query(true, TB_Program, new String[]{
                prog_id, prog_name, time_start, time_end, day_id, fk_channel_id, fk_type_id}, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    //===================================== Type ============================================

    public long addType(int a, String b) {
        ContentValues values = new ContentValues();
        values.put(type_id, a);
        values.put(type_name, b);
        return database.insert(TB_Type, null, values);
    }

    public Cursor readType() {
        Cursor cursor = database.query(true, TB_Type, new String[]{
                type_id, type_name}, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    //===================================== Version ============================================

    public long addVersion(int a) {
        ContentValues values = new ContentValues();
        values.put(version_number, a);
        Log.d("run","addVersion "+a);
        return database.insert(TB_Version, null, values);
    }

    public Cursor readVersion() {
        Cursor cursor = database.query(true, TB_Version, new String[]{
                version_number}, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updateVersion(int a) {
        ContentValues values = new ContentValues();
        values.put(version_number, a);

        return database.update(TB_Version, values, null, null);
    }

    //===========================================================================

    public void deleteAllData() {
       // database.execSQL("TRUNCATE TABLE "+TB_Category);
        //database.execSQL("TRUNCATE TABLE "+TB_Channel);
       // database.execSQL("TRUNCATE TABLE "+TB_Program);
        //database.execSQL("TRUNCATE TABLE "+TB_Type);
        database.delete(TB_Category,null,null);
        database.delete(TB_Channel,null,null);
        database.delete(TB_Program,null,null);
        database.delete(TB_Type,null,null);
    }






}
