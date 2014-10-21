package com.webmanagement.thaidigitaltv;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;


public class ServiceAlarm extends Service {


    @Override
    public void onCreate() {

        Toast.makeText(getApplicationContext(), "onCreate Service", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @SuppressWarnings("static-access")

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "onDestroy Service", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "onBind Service", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(getApplicationContext(), "onUnbind Service", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Toast.makeText(getApplicationContext(), "onRebind Service", Toast.LENGTH_SHORT).show();
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getApplicationContext(), "onStartCommand Service", Toast.LENGTH_SHORT).show();

        Context context = getApplicationContext();

        String prog_id = intent.getStringExtra("prog_id");
        String prog_name = intent.getStringExtra("prog_name");
        String chan_name = intent.getStringExtra("chan_name");
        String time_before = intent.getStringExtra("time_before");
        String time_start = intent.getStringExtra("time_start");
        String repeat_id = intent.getStringExtra("repeat_id");
        String day_id = intent.getStringExtra("day_id");
        String pic = intent.getStringExtra("pic");


        Intent intent1 = new Intent(context, DisplayAlarm.class);
        intent1.putExtra("prog_id", prog_id);
        intent1.putExtra("prog_name", prog_name);
        intent1.putExtra("chan_name", chan_name);
        intent1.putExtra("time_before", time_before);
        intent1.putExtra("time_start", time_start);
        intent1.putExtra("repeat_id", repeat_id);
        intent1.putExtra("day_id", day_id);
        intent1.putExtra("pic", pic);

        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

        Log.d("run", "Service " + prog_id);
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }


}
