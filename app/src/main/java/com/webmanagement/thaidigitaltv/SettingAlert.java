package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
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
    private Store_Variable storeVariable;
    ArrayList<DataCustomSettingTime> dataCustomSettingTime = new ArrayList<DataCustomSettingTime>();
    SettingTimeAdapter settingTimeAdapter;

    String program_name, type_name, channel_name, time_before, time_start;
    ToggleButton TGB0, TGB1, TGB2, TGB3, TGB4, TGB5, TGB6;
    CheckBox CB_settime_repeat;
    int program_id;
    ListView LV_Time;
    int select_item;
    boolean addSuccess = false;

    ArrayList<DataStore_Program> arrDataStore_program = MainActivity.arrDataStore_program;
    ArrayList<Integer> arrTempDay = new ArrayList<Integer>();
    ArrayList<Integer> arrTempId = new ArrayList<Integer>();

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alert);

        aq = new AQuery(this);

        dbAction = new DatabaseAction(this);
        storeVariable = new Store_Variable();
        arrTempDay.clear();
        arrTempId.clear();

        ImageView IV_ic_nav_top_left = (ImageView) findViewById(R.id.ic_nav_top_left);
        settingTimeAdapter = new SettingTimeAdapter(this, dataCustomSettingTime);

        LV_Time = (ListView) findViewById(R.id.lv_time);
        LV_Time.setAdapter(settingTimeAdapter);

        select_item = storeVariable.getItem_selected();
        program_id = storeVariable.getProg_id(select_item);
        program_name = storeVariable.getProg_name(select_item);
        channel_name = storeVariable.getChan_name();
        time_start = storeVariable.getTime_start(select_item);

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


        if (!arrTempDay.contains(0)) {  //TGB0.setVisibility(View.VISIBLE);
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
               // Toast.makeText(getApplicationContext(), "B", Toast.LENGTH_SHORT).show();
               // setAlarm();

                int time_selected = dataCustomSettingTime.get(settingTimeAdapter.getSelectedPosition()).time_val;
                int isRepeat;
                if (CB_settime_repeat.isChecked())
                    isRepeat = 1;
                else
                isRepeat = 0;
                for (int i = 0;i < arrTempDay.size();i++) {
                    if (arrTempDay.get(i) == 0 && TGB0.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    } else if(arrTempDay.get(i) == 1 && TGB1.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    } else if(arrTempDay.get(i) == 2 && TGB2.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    } else if(arrTempDay.get(i) == 3 && TGB3.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    } else if(arrTempDay.get(i) == 4 && TGB4.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    } else if(arrTempDay.get(i) == 5 && TGB5.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    } else if(arrTempDay.get(i) == 6 && TGB6.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), isRepeat);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    }

                }

               if (addSuccess) {
                   Toast.makeText(getApplicationContext(), "Add Complete", Toast.LENGTH_LONG).show();
                   finish();
               } else {
                   Toast.makeText(getApplicationContext(), "Can't Add", Toast.LENGTH_LONG).show();
               }



            }


        });


    }

    private void setAlarm() {

        alarmMgr = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        Intent intent = new Intent(this, DisplayAlertTime.class);
        intent.putExtra("intent_name", 2);
     //   alarmIntent = PendingIntent.getBroadcast(activity, 0, intent, 0);

// Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.SECOND, 10);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 5, alarmIntent);

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
