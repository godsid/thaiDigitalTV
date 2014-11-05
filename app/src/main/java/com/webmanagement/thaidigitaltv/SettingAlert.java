package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
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
import java.util.concurrent.TimeUnit;


public class SettingAlert extends Activity {
    AQuery aq;

    private DatabaseAction dbAction;
    private int[] time_value = new int[]{5, 10, 20, 30, 40, 50, 60};
    ArrayList<DataCustomSettingTime> dataCustomSettingTime = new ArrayList<DataCustomSettingTime>();
    SettingTimeAdapter settingTimeAdapter;

    String program_name, type_name, channel_name, time_start, action_type;
    ToggleButton TGB0, TGB1, TGB2, TGB3, TGB4, TGB5, TGB6;
    CheckBox CB_settime_repeat;
    int program_id,i_repeat_id,i_day_id,i_time_before;
    ListView LV_Time;

    ImageView IV_settime_save;

    boolean addSuccess = false;

    ArrayList<DataStore_Program> arrDataStore_program = MainActivity.arrDataStore_program;
    ArrayList<Integer> arrTempDay = new ArrayList<Integer>();
    ArrayList<Integer> arrTempId = new ArrayList<Integer>();
    String[] arr_day = new String[]{"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};
    Context context;

    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alert);

        aq = new AQuery(this);
        bundle = getIntent().getExtras();
        dbAction = new DatabaseAction(this);
        context = SettingAlert.this;
        arrTempDay.clear();
        arrTempId.clear();

        ImageView IV_ic_nav_top_left = (ImageView) findViewById(R.id.ic_nav_top_left);
        settingTimeAdapter = new SettingTimeAdapter(this, dataCustomSettingTime);

        LV_Time = (ListView) findViewById(R.id.lv_time);
        LV_Time.setAdapter(settingTimeAdapter);

        if(!bundle.isEmpty()) {
            program_id = bundle.getInt("i_Prog_id");
            program_name = bundle.getString("i_Prog_name");
            channel_name = bundle.getString("i_Chan_name");
            time_start = bundle.getString("i_Prog_timestart");
            action_type = bundle.getString("i_Action_type");
            if (action_type.equals("edit")) {
                i_time_before = bundle.getInt("i_prog_timebf");
                i_day_id = bundle.getInt("i_Day_id");
                i_repeat_id = bundle.getInt("i_Repeat_id");
            }

        }

        setDataToListView();

        IV_settime_save = (ImageView) findViewById(R.id.iv_settime_save);
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


        for (int i = 0; i < 6; i++) {
            if (!arrTempDay.contains(i)) {
                setDisableChkBoxDay(i);
            }
        }

        disableChkBoxFromDB();


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

                if (action_type.equals("add")) {
                    addDataToDB();
                } else if(action_type.equals("edit")) {
                    updateDataToDB();
                }

            }


        });

        if (action_type.equals("edit")) {
            isActionEdit();
        }

    }

    private void setDisableChkBoxDay(int i) {
        switch (i) {
            case 0 :
                TGB0.setEnabled(false);
                TGB0.setBackgroundResource(R.drawable.toggle_0_dis);
                break;
            case 1 :
                TGB1.setEnabled(false);
                TGB1.setBackgroundResource(R.drawable.toggle_1_dis);
                break;
            case 2 :
                TGB2.setEnabled(false);
                TGB2.setBackgroundResource(R.drawable.toggle_2_dis);
                break;
            case 3 :
                TGB3.setEnabled(false);
                TGB3.setBackgroundResource(R.drawable.toggle_3_dis);
                break;
            case 4 :
                TGB4.setEnabled(false);
                TGB4.setBackgroundResource(R.drawable.toggle_4_dis);
                break;
            case 5 :
                TGB5.setEnabled(false);
                TGB5.setBackgroundResource(R.drawable.toggle_5_dis);
                break;
            case 6 :
                TGB6.setEnabled(false);
                TGB6.setBackgroundResource(R.drawable.toggle_6_dis);
                break;
        }

    }
    private void disableChkBoxFromDB() {

            SQLiteCursor cur = (SQLiteCursor) dbAction.readAllFavoriteProgram();
            while (!cur.isAfterLast()) {
                String prog_name = cur.getString(2);
                int day_id = cur.getInt(6);
                if (prog_name.equals(program_name)) {

                    setDisableChkBoxDay(day_id);

                }
                cur.moveToNext();
            }
            cur.close();

    }

    private void isActionEdit() {
        switch (i_day_id) {
            case 0 :
                TGB0.setEnabled(true);
                TGB0.setChecked(true);
                TGB0.setBackgroundResource(R.drawable.toggle0);
                break;
            case 1 :
                TGB1.setEnabled(true);
                TGB1.setChecked(true);
                TGB1.setBackgroundResource(R.drawable.toggle1);
                break;
            case 2 :
                TGB2.setEnabled(true);
                TGB2.setChecked(true);
                TGB2.setBackgroundResource(R.drawable.toggle2);
                break;
            case 3 :
                TGB3.setEnabled(true);
                TGB3.setChecked(true);
                TGB3.setBackgroundResource(R.drawable.toggle3);
                break;
            case 4 :
                TGB4.setEnabled(true);
                TGB4.setChecked(true);
                TGB4.setBackgroundResource(R.drawable.toggle4);
                break;
            case 5 :
                TGB5.setEnabled(true);
                TGB5.setChecked(true);
                TGB5.setBackgroundResource(R.drawable.toggle5);
                break;
            case 6 :
                TGB6.setEnabled(true);
                TGB6.setChecked(true);
                TGB6.setBackgroundResource(R.drawable.toggle6);
                Log.d("run","6 "+TGB6.isEnabled());
                break;
        }
        int selectP = 0;
        for(int i =0; i < time_value.length; i++) {
            if (time_value[i] == i_time_before) {
             selectP = i;

                break;
            }
        }
        settingTimeAdapter.setSelectedPosition(selectP);

        if (i_repeat_id == 1)
            CB_settime_repeat.setChecked(true);

        IV_settime_save.setImageResource(R.drawable.ic_update);
    }

    private void updateDataToDB() {
        cancelAlarm(program_id);
        dbAction.deleteFavoriteProgram(program_id);
        addDataToDB();
    }

    private void addDataToDB() {
        int time_selected = dataCustomSettingTime.get(settingTimeAdapter.getSelectedPosition()).time_val;
        int isRepeat;
        if (CB_settime_repeat.isChecked())
            isRepeat = 1;
        else
            isRepeat = 0;

        if (!TGB0.isChecked() && !TGB1.isChecked() && !TGB2.isChecked() && !TGB3.isChecked()
                && !TGB4.isChecked() && !TGB5.isChecked() && !TGB6.isChecked()) {
            Toast.makeText(getApplicationContext(), "คำเตือน : กรุณาเลือกวันที่ต้องการแจ้งเตือน", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(getApplicationContext(), "สำเร็จ : ทำรายการเรียบร้อย", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "ผิดพลาด : ไม่สามารถเพิ่มรายการได้", Toast.LENGTH_SHORT).show();
        }
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

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        calSet.setTimeInMillis(System.currentTimeMillis());
        calSet.set(Calendar.DAY_OF_WEEK, d + 1);
        calSet.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split_time_start[0]));
        calSet.set(Calendar.MINUTE, Integer.parseInt(split_time_start[1]));
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        calSet.add(Calendar.MINUTE, -tbf);

        Calendar calNext = (Calendar) calSet.clone();
        calNext.setTimeInMillis(calNext.getTimeInMillis());
        calNext.add(Calendar.DAY_OF_WEEK, 7);

        if(calSet.compareTo(calNow) <= 0){
            //Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 7);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), TimeUnit.DAYS.toMillis(7), pendingIntent);

        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), 1000*15, pendingIntent);


        int dayOfWeek = calSet.get(Calendar.DAY_OF_WEEK);
        int date = calSet.get(Calendar.DATE);
        int hourOfDay = calSet.get(Calendar.HOUR_OF_DAY); // 24 hour clock
        int minute = calSet.get(Calendar.MINUTE);

        int dayOfWeekn = calNext.get(Calendar.DAY_OF_WEEK);
        int daten = calNext.get(Calendar.DATE);
        int hourOfDayn = calNext.get(Calendar.HOUR_OF_DAY); // 24 hour clock
        int minuten = calNext.get(Calendar.MINUTE);

        Log.d("run", Integer.toString(pid) + " Start > " + arr_day[dayOfWeek - 1] + "  " + date + " " + hourOfDay + ":" + minute + " " + " BF " + time_start);
        Log.d("run", Integer.toString(pid) + " Next  > " + arr_day[dayOfWeekn - 1] + "  " + daten + " " + hourOfDayn + ":" + minuten + " " + " BF " + time_start);

    }


    private void cancelAlarm(int prog_id) {

        Intent intent2 = new Intent(context, ReceiverAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, prog_id, intent2, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);


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
