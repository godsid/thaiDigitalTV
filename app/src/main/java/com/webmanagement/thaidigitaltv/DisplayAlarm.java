package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DisplayAlarm extends Activity {
    Ringtone r;
    Vibrator v;
    AQuery aq;
    private DatabaseAction dbAction;
    int prog_id = 0, day_id,repeat_id;
    String time_before;
    String[] arr_day = new String[]{"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_alert_time);
        aq = new AQuery(this);
        dbAction = new DatabaseAction(this);
        //stopVibrator();
        v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);

        v.cancel();
        r.stop();

        Button bt_accept = (Button) findViewById(R.id.bt_disp_accept);
        Button buttonKS = (Button) findViewById(R.id.bt_kill_service);
        Button bt_accept_open_app = (Button) findViewById(R.id.bt_disp_accept_and_open);
        TextView tv_title = (TextView) findViewById(R.id.tv_disp_title);
        TextView tv_time = (TextView) findViewById(R.id.tv_disp_time);
        ImageView iv_channel = (ImageView) findViewById(R.id.iv_disp_chan);

        try {

            Intent intent = getIntent();
            prog_id = Integer.parseInt(intent.getStringExtra("prog_id"));
            String prog_name = intent.getStringExtra("prog_name");
            String chan_name = intent.getStringExtra("chan_name");
            time_before = intent.getStringExtra("time_before");
            String time_start = intent.getStringExtra("time_start");
            day_id = Integer.parseInt(intent.getStringExtra("day_id"));
            repeat_id = Integer.parseInt(intent.getStringExtra("repeat_id"));

            if (repeat_id == 0) {
                dbAction.deleteFavoriteProgram(prog_id);
                Intent intent2 = new Intent(getBaseContext(), ReceiverAlarm.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), prog_id, intent2, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                alarmManager.cancel(pendingIntent);
                Log.d("run", "repeat_id " + repeat_id);


            }

            tv_title.setText(prog_id + " : " + prog_name + "  " + chan_name);
            tv_time.setText("แจ้งเตือนอีก " + time_before + " ก่อนถึงเวลา " + time_start);
            aq.id(iv_channel).image(getIntent().getStringExtra("pic"));

            long[] pattern = {1000, 0, 0, 100, 100, 0, 100, 2000, 0, 0,};
            v.vibrate(pattern, 0);
            r.play();

        } catch (Exception e) {
            Log.d("run", "File DisplayAlarm : " + e);
        }

        bt_accept_open_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat_id != 0)
                    Toast.makeText(getApplicationContext(),alertNextTime(time_before),Toast.LENGTH_LONG).show();
                r.stop();
                stopVibrator();
                finish();

                PackageManager manager = getPackageManager();
                try {
                    Intent i;
                    i = manager.getLaunchIntentForPackage("com.webmanagement.thaidigitaltv");
                    if (i == null)
                        throw new PackageManager.NameNotFoundException();
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {

                }
            }
        });

        buttonKS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ReceiverAlarm.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), prog_id, intent, 0);

                //Intent intent = new Intent(getBaseContext(),AlertTimeControl.class);
                // PendingIntent pendingIntent = PendingIntent.getService(getBaseContext(), 0, intent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                alarmManager.cancel(pendingIntent);

                //Intent intent = new Intent();
                //intent.setClass(MyScheduledActivity.this, AndroidScheduledActivity.class);
                // startActivity(intent);
                stopVibrator();
                r.stop();
                finish();
            }
        });

        bt_accept.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (repeat_id != 0)
                    Toast.makeText(getApplicationContext(),alertNextTime(time_before),Toast.LENGTH_LONG).show();

                r.stop();
                stopVibrator();
                finish();
            }
        });
    }

    private String alertNextTime(String time_before) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, Integer.parseInt(time_before));

        //int date = calendar.get(Calendar.DATE);
        String hm = sdf.format(calendar.getTime());

        Calendar calendarNext = Calendar.getInstance();
        calendarNext.setTimeInMillis(calendar.getTimeInMillis());
        calendarNext.add(Calendar.DAY_OF_WEEK, 7);


        return "แจ้งเตือนอีกครั้ง วัน"+arr_day[day_id]+" เวลา"+hm;

    }

    private void stopVibrator() {
        v.cancel();
    }


    @Override
    public void onBackPressed() {
       return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_alert_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
