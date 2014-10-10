package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;

import java.util.ArrayList;


public class SettingTimeList {

    View rootView;
    Context context;
    Activity activity;
    AQuery aq;
    FrameLayout ContentFrame;
    View  ViewProgramDetail;
    LayoutInflater inflater;
    ListProgram listProgram;

    private DatabaseAction dbAction;
    private int[] time_value = new int[]{5, 10, 20, 30, 40, 50, 60};
    private DetailProgram detailProgram;
    ArrayList<DataCustomSettingTime> dataCustomSettingTime = new ArrayList<DataCustomSettingTime>();
    SettingTimeAdapter settingTimeAdapter;

    String program_name, type_name, channel_name, time_before, time_start;
    ToggleButton TGB0,TGB1,TGB2,TGB3,TGB4,TGB5,TGB6;
    CheckBox CB_settime_repeat;
    int program_id;
    ListView LV_Time;
    int select_item;
    boolean addSuccess = false;

    ArrayList<DataStore_Program> arrDataStore_program = MainActivity.arrDataStore_program;
    ArrayList<Integer> arrTempDay = new ArrayList<Integer>();
    ArrayList<Integer> arrTempId = new ArrayList<Integer>();


    public SettingTimeList(View view) {
        this.rootView = view;
        this.context = rootView.getContext();
        this.activity = (Activity) context;
        aq = new AQuery(context);

        dbAction = new DatabaseAction(context);
        detailProgram = new DetailProgram();
        arrTempDay.clear();
        arrTempId.clear();

        ContentFrame = (FrameLayout) activity.findViewById(R.id.content_frame);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );


        settingTimeAdapter = new SettingTimeAdapter(context, dataCustomSettingTime);

        LV_Time = (ListView) rootView.findViewById(R.id.lv_time);
        LV_Time.setAdapter(settingTimeAdapter);

        select_item = detailProgram.getItem_selected();
        program_id = detailProgram.getProg_id(select_item);
        program_name = detailProgram.getProg_name(select_item);
        channel_name = detailProgram.getChan_name();
        time_start = detailProgram.getTime_start(select_item);

        setDataToListView();

        ImageView IV_settime_save = (ImageView) rootView.findViewById(R.id.iv_settime_save);
        TextView TV_settime_title_prog = (TextView)rootView.findViewById(R.id.tv_settime_title_prog);

        TGB0 = (ToggleButton)rootView.findViewById(R.id.tgb_0);
        TGB1 = (ToggleButton)rootView.findViewById(R.id.tgb_1);
        TGB2 = (ToggleButton)rootView.findViewById(R.id.tgb_2);
        TGB3 = (ToggleButton)rootView.findViewById(R.id.tgb_3);
        TGB4 = (ToggleButton)rootView.findViewById(R.id.tgb_4);
        TGB5 = (ToggleButton)rootView.findViewById(R.id.tgb_5);
        TGB6 = (ToggleButton)rootView.findViewById(R.id.tgb_6);
        CB_settime_repeat = (CheckBox)rootView.findViewById(R.id.cb_settime_repeat);

        for (int i = 0;i<arrDataStore_program.size();i++) {
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





        TV_settime_title_prog.setText("รายการ "+program_name);



        IV_settime_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time_selected = dataCustomSettingTime.get(settingTimeAdapter.getSelectedPosition()).time_val;
                for (int i = 0;i < arrTempDay.size();i++) {
                    if (arrTempDay.get(i) == 0 && TGB0.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), 0);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    } else if(arrTempDay.get(i) == 1 && TGB1.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), 0);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    } else if(arrTempDay.get(i) == 2 && TGB2.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), 0);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    } else if(arrTempDay.get(i) == 3 && TGB3.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), 0);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    } else if(arrTempDay.get(i) == 4 && TGB4.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), 0);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    } else if(arrTempDay.get(i) == 5 && TGB5.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), 0);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    } else if(arrTempDay.get(i) == 6 && TGB6.isChecked()) {
                        long resAdd = dbAction.addFavoriteProgram(arrTempId.get(i), program_name, channel_name, time_start, time_selected, arrTempDay.get(i), 0);
                        if (resAdd > 0)
                            addSuccess = true;
                        else {
                            addSuccess = false;
                            break;
                        }

                    }

                }

               if (addSuccess) {
                   Toast.makeText(context, "Add Complete", Toast.LENGTH_LONG).show();
                   ViewProgramDetail = activity.getLayoutInflater().inflate(R.layout.activity_detail_list, ContentFrame, false);
                   ContentFrame.removeAllViews();
                   listProgram = new ListProgram(ViewProgramDetail);
                   ContentFrame.addView(ViewProgramDetail);

               } else {
                   Toast.makeText(context, "Can't Add", Toast.LENGTH_LONG).show();
               }



                }


        });
//Log.d("run2", program_id + "," + program_name + "," + type_name + "," + channel_name + "," + time_start + " , " + time_selected + " : " + resAdd);
    }





    public void setDataToListView() {

        for (int i = 0; i < time_value.length; i++) {
            dataCustomSettingTime.add(new DataCustomSettingTime(time_value[i], i));
        }
        settingTimeAdapter.notifyDataSetChanged();
    }



}
