package com.webmanagement.thaidigitaltv;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by SystemDLL on 13/10/2557.
 */
public class ReceiverAlarm extends BroadcastReceiver {

    private DatabaseAction dbAction;
    private Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            this.mContext = context;
            dbAction = new DatabaseAction(mContext);
           // cancelAllAlarm();
  //          addAllAlarm();


            //Toast.makeText(mContext, "onReceive", Toast.LENGTH_SHORT).show();

            String prog_id = intent.getStringExtra("prog_id");
            String prog_name = intent.getStringExtra("prog_name");
            String chan_name = intent.getStringExtra("chan_name");
            String time_before = intent.getStringExtra("time_before");
            String time_start = intent.getStringExtra("time_start");
            String repeat_id = intent.getStringExtra("repeat_id");
            String day_id = intent.getStringExtra("day_id");


            Intent intent1 = new Intent(mContext, ServiceAlarm.class);
            intent1.putExtra("prog_id", prog_id);
            intent1.putExtra("prog_name", prog_name);
            intent1.putExtra("chan_name", chan_name);
            intent1.putExtra("time_before", time_before);
            intent1.putExtra("time_start", time_start);
            intent1.putExtra("repeat_id", repeat_id);
            intent1.putExtra("day_id", day_id);

            mContext.startService(intent1);

           Log.d("run", "Receiver " + prog_id);


        } catch (Exception e) {
            Log.d("run", "Error Receiver " + e);
        }
    }


    private void cancelAllAlarm() {
        dbAction = new DatabaseAction(mContext);
        SQLiteCursor cur = (SQLiteCursor) dbAction.readAllFavoriteProgram();

        while (!cur.isAfterLast()) {

            int prog_id = Integer.parseInt(cur.getString(1));
            Log.d("run", "cancelAllAlarm " + prog_id);
            Intent intent2 = new Intent(mContext, ReceiverAlarm.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, prog_id, intent2, 0);
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);

            cur.moveToNext();
        }
        cur.close();

    }

    private void addAllAlarm() {

        SQLiteCursor cur = (SQLiteCursor) dbAction.readAllFavoriteProgram();

        while (!cur.isAfterLast()) {
            int prog_id = Integer.parseInt(cur.getString(1));
            String prog_name = cur.getString(2);
            String chan_name = cur.getString(3);
            String time_start = cur.getString(4);
            String time_before = cur.getString(5);
            int day_id = cur.getInt(6);
            int repeat_id = cur.getInt(7);


         Intent intent1 = new Intent(mContext, ServiceAlarm.class);
        //Intent intent1 = new Intent(context, DisplayAlarm.class);
        intent1.putExtra("prog_id", prog_id);
        intent1.putExtra("prog_name", prog_name);
        intent1.putExtra("chan_name", chan_name);
        intent1.putExtra("time_before", time_before);
        intent1.putExtra("time_start", time_start);
        intent1.putExtra("repeat_id", repeat_id);
        intent1.putExtra("day_id", day_id);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, prog_id, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingIntent = PendingIntent.getService(this, prog_id, intent, 0);

        String arr_time_start = time_start;
        String[] split_time_start = arr_time_start.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, day_id + 1);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split_time_start[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(split_time_start[1]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, -Integer.parseInt(time_before));

        Calendar calendarNext = Calendar.getInstance();
        calendarNext.setTimeInMillis(calendar.getTimeInMillis());
        calendarNext.add(Calendar.DAY_OF_WEEK, 7);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), calendarNext.getTimeInMillis(), pendingIntent);

        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), 1000*15, pendingIntent);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int date = calendar.get(Calendar.DATE);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
        int minute = calendar.get(Calendar.MINUTE);

        int dayOfWeekn = calendarNext.get(Calendar.DAY_OF_WEEK);
        int daten = calendarNext.get(Calendar.DATE);
        int hourOfDayn = calendarNext.get(Calendar.HOUR_OF_DAY); // 24 hour clock
        int minuten = calendarNext.get(Calendar.MINUTE);


        Intent service1 = new Intent(mContext, ServiceAlarm.class);
        mContext.startService(service1);

            Log.d("run", "Receiver > " + prog_id);
            cur.moveToNext();
        }
        cur.close();


    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        Toast.makeText(myContext, "peekService", Toast.LENGTH_SHORT).show();
        return super.peekService(myContext, service);
    }
}
