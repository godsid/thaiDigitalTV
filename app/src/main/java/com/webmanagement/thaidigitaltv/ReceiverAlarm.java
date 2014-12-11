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

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            this.mContext = context;
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


    @Override
    public IBinder peekService(Context myContext, Intent service) {
        Toast.makeText(myContext, "peekService", Toast.LENGTH_SHORT).show();
        return super.peekService(myContext, service);
    }


}
