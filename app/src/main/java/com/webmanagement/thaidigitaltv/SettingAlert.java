package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.Calendar;


public class SettingAlert extends Activity {
    AQuery aq;

    private DatabaseAction dbAction;
    private int[] time_value = new int[]{5, 10, 20, 30, 40, 50, 60};
    ArrayList<DataCustomSettingTime> dataCustomSettingTime = new ArrayList<DataCustomSettingTime>();
    SettingTimeAdapter settingTimeAdapter;

    String program_name, type_name, channel_name, time_start;
    ToggleButton TGB0, TGB1, TGB2, TGB3, TGB4, TGB5, TGB6;
    CheckBox CB_settime_repeat;
    int program_id;
    ListView LV_Time;


    boolean addSuccess = false;

    ArrayList<DataStore_Program> arrDataStore_program = MainActivity.arrDataStore_program;
    ArrayList<Integer> arrTempDay = new ArrayList<Integer>();
    ArrayList<Integer> arrTempId = new ArrayList<Integer>();
    String[] arr_day = new String[]{"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};


    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alert);

        aq = new AQuery(this);
        bundle = getIntent().getExtras();
        dbAction = new DatabaseAction(this);

        arrTempDay.clear();
        arrTempId.clear();

        ImageView IV_ic_nav_top_left = (ImageView) findViewById(R.id.ic_nav_top_left);
        settingTimeAdapter = new SettingTimeAdapter(this, dataCustomSettingTime);

        LV_Time = (ListView) findViewById(R.id.lv_time);
        LV_Time.setAdapter(settingTimeAdapter);

        program_id = bundle.getInt("i_Prog_id");
        program_name = bundle.getString("i_Prog_name");
        channel_name = bundle.getString("i_Chan_name");
        time_start = bundle.getString("i_Prog_timestart");

        setDataToListView();

        ImageView IV_settime_save = (ImageView) findViewById(R.id.iv_settime_save);
        TextView TV_settime_title_prog = (TextView) findViewById(R.id.tv_settime_title_prog);

        TGB0 = (ToggleButton) findViewById(R.id.tgb_0);
        TGB1 = (ToggleButton) findViewById(R.id.tgb_1);
        TGB2 = (ToggleButton) findViewById(R.id.tgb_2);
        TGB3 = (ToggleButton) findViewById(R.id.tgb_3);
        TGB4 = (ToggleButton) findViewById(R.id.tgb_4);
        TGB5 = (ToggleButton) findViewById(R.id.tgb_5);
        TGB6 = (ToggleButton) findViewById(R.id.tgb_6);
        CB_settime_repeat = (CheckBox) findViewById(R.id.cb_settime_repeat);

        for (int i = 0; i < arrDataStore_program.size(); i++) {
            if (arrDataStore_program.get(i).getProg_name().equals(program_name)) {
                arrTempDay.add(arrDataStore_program.get(i).getFr_day_id());
                arrTempId.add(arrDataStore_program.get(i).getProg_id());
            }
        }


        if (!arrTempDay.contains(0)) {
            TGB0.setEnabled(false);
            TGB0.setBackgroundResource(R.drawable.tg_3_dis);
        }
        if (!arrTempDay.contains(1)) {
            TGB1.setEnabled(false);
            TGB1.setBackgroundResource(R.drawable.tg_2_dis);
        }
        if (!arrTempDay.contains(2)) {
            TGB2.setEnabled(false);
            TGB2.setBackgroundResource(R.drawable.tg_3_dis);
        }
        if (!arrTempDay.contains(3)) {
            TGB3.setEnabled(false);
            TGB3.setBackgroundResource(R.drawable.tg_4_dis);
        }
        if (!arrTempDay.contains(4)) {
            TGB4.setEnabled(false);
            TGB4.setBackgroundResource(R.drawable.tg_4_dis);
        }
        if (!arrTempDay.contains(5)) {
            TGB5.setEnabled(false);
            TGB5.setBackgroundResource(R.drawable.tg_5_dis);
        }
        if (!arrTempDay.contains(6)) {
            TGB6.setEnabled(false);
            TGB6.setBackgroundResource(R.drawable.tg_6_dis);
        }


        TV_settime_title_prog.setText("รายการ " + program_name);

        IV_ic_nav_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        IV_settime_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int time_selected = dataCustomSettingTime.get(settingTimeAdapter.getSelectedPosition()).time_val;
                int isRepeat;
                if (CB_settime_repeat.isChecked())
                    isRepeat = 1;
                else
                    isRepeat = 0;

                if (!TGB0.isChecked() && !TGB1.isChecked() && !TGB2.isChecked() && !TGB3.isChecked()
                        && !TGB4.isChecked() && !TGB5.isChecked() && !TGB6.isChecked()) {
                    Toast.makeText(getApplicationContext(), "คำเตือน : เลือกวันที่ต้องการแจ้งเตือนก่อน", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < arrTempDay.size(); i++) {
                    if (arrTempDay.get(i) == 0 && TGB0.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0) {
                            addSuccess = true;
                            addAlarm(arrTempId.get(i), time_selected, arrTempDay.get(i), isRepeat);
                        } else {
                            addSuccess = false;
                            break;
                        }

                    } else if (arrTempDay.get(i) == 1 && TGB1.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0) {
                            addSuccess = true;
                            addAlarm(arrTempId.get(i), time_selected, arrTempDay.get(i), isRepeat);
                        } else {
                            addSuccess = false;
                            break;
                        }

                    } else if (arrTempDay.get(i) == 2 && TGB2.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0) {
                            addSuccess = true;
                            addAlarm(arrTempId.get(i), time_selected, arrTempDay.get(i), isRepeat);
                        } else {
                            addSuccess = false;
                            break;
                        }

                    } else if (arrTempDay.get(i) == 3 && TGB3.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0) {
                            addSuccess = true;
                            addAlarm(arrTempId.get(i), time_selected, arrTempDay.get(i), isRepeat);
                        } else {
                            addSuccess = false;
                            break;
                        }

                    } else if (arrTempDay.get(i) == 4 && TGB4.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0) {
                            addSuccess = true;
                            addAlarm(arrTempId.get(i), time_selected, arrTempDay.get(i), isRepeat);
                        } else {
                            addSuccess = false;
                            break;
                        }

                    } else if (arrTempDay.get(i) == 5 && TGB5.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0) {
                            addSuccess = true;
                            addAlarm(arrTempId.get(i), time_selected, arrTempDay.get(i), isRepeat);
                        } else {
                            addSuccess = false;
                            break;
                        }

                    } else if (arrTempDay.get(i) == 6 && TGB6.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0) {
                            addSuccess = true;
                            addAlarm(arrTempId.get(i), time_selected, arrTempDay.get(i), isRepeat);
                        } else {
                            addSuccess = false;
                            break;
                        }

                    }

                }

                if (addSuccess) {

                    //  stopService(new Intent(getApplicationContext(),ServiceAlarm.class));
                    //  cancelAllAlarm();
                    //  startService(new Intent(getApplicationContext(),ServiceAlarm.class));

                    Toast.makeText(getApplicationContext(), "สำเร็จ : เพิ่มรายการแจ้งเตือนเรียบร้อย", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "ผิดพลาด : ไม่สามารถเพิ่มรายการได้", Toast.LENGTH_SHORT).show();
                }


            }


        });


    }

    private void addAlarm(int pid, int tbf, int d, int rp) {

        Intent intent = new Intent(getApplicationContext(), ReceiverAlarm.class);
        intent.putExtra("prog_id", Integer.toString(pid));
        intent.putExtra("prog_name", program_name);
        intent.putExtra("chan_name", channel_name);
        intent.putExtra("time_before", Integer.toString(tbf));
        intent.putExtra("time_start", time_start);
        intent.putExtra("day_id", Integer.toString(d));
        intent.putExtra("repeat_id", Integer.toString(rp));

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), pid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingIntent = PendingIntent.getService(this, prog_id, intent, 0);

        String arr_time_start = time_start;
        String[] split_time_start = arr_time_start.split(":");
        Log.d("run", "RRR " + split_time_start[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, d + 1);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split_time_start[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(split_time_start[1]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, -tbf);

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


        String g1 = Integer.toString((int) calendar.getTimeInMillis());
        Log.d("run", Integer.toString(pid) + " Start > " + arr_day[dayOfWeek - 1] + "  " + date + " " + hourOfDay + ":" + minute + " " + " BF " + time_start);
        Log.d("run", Integer.toString(pid) + " Next  > " + arr_day[dayOfWeekn - 1] + "  " + daten + " " + hourOfDayn + ":" + minuten + " " + " BF " + time_start);

    }




    public void setDataToListView() {

        for (int i = 0; i < time_value.length; i++) {
            dataCustomSettingTime.add(new DataCustomSettingTime(time_value[i], i));
        }
        settingTimeAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
