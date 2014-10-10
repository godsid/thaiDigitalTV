package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteCursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SystemDLL on 8/10/2557.
 */
public class ListProgram {
    View rootView;
    Context context;
    DetailProgram detailProgram;
    AQuery aq;
    Activity activity;

    FrameLayout ContentFrame;
    View  ViewSettingTime;
    LayoutInflater inflater;
    SettingTimeList settingTimeList;

    TextView TV_header_program, TV_header_time, TV_header_status, TV_header_fav, TV_detail_list_title;
    TextView TV_detail_day, TV_detail_date, TV_detail_month, TV_detail_year;
    ImageView IV_detail_today, IV_detail_list_title;
    ListView LV_program_detail;
    private SeekBar SB_detail_date = null;
    int tv_header_tb_size = 18;
    int tv_item_tb_size = 16;
    boolean selectIsToDay = true;
    DatabaseAction dbAction;
    private int position_for_delete;

    Calendar calendar;
    Date date;
    String[] arr_day = new String[]{"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};
    String[] arr_month = new String[]{"มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม",
            "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};
    int g_current_day, g_current_date, g_current_month, g_current_year;
    int g_change_day;

    ListProgramDetailAdapter listProgramDetailAdapter;
    ArrayList<DataCustomProgramDetail> dataCustomProgramDetail;

    public static  ArrayList<Integer> arrHoldProg_idDB = new ArrayList<Integer>();

    ArrayList<DataStore_Program> arrDataStore_program = MainActivity.arrDataStore_program;

    public ListProgram(View view) {

        this.rootView = view;
        this.context = rootView.getContext();
        this.activity = (Activity)context;
        aq = new AQuery(activity);
        detailProgram = new DetailProgram();
        calendar = Calendar.getInstance();
        date = new Date();
        dbAction = new DatabaseAction(context);
        dataCustomProgramDetail = new ArrayList<DataCustomProgramDetail>();


        ContentFrame = (FrameLayout) activity.findViewById(R.id.content_frame);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );


        IV_detail_today = (ImageView) rootView.findViewById(R.id.iv_detail_today);
        TV_detail_list_title = (TextView) activity.findViewById(R.id.tv_detail_list_title);
        IV_detail_list_title = (ImageView) activity.findViewById(R.id.iv_detail_list_title);
        TV_detail_day = (TextView) rootView.findViewById(R.id.tv_detail_day);
        TV_detail_date = (TextView) rootView.findViewById(R.id.tv_detail_date);
        TV_detail_month = (TextView) rootView.findViewById(R.id.tv_detail_month);
        TV_detail_year = (TextView) rootView.findViewById(R.id.tv_detail_year);
     //   TV_detail_list_title.setTypeface(MainActivity.TF_font);

        SB_detail_date = (SeekBar) rootView.findViewById(R.id.sb_detail_date);

        TV_header_program = (TextView) rootView.findViewById(R.id.tv_header_program);
        TV_header_time = (TextView) rootView.findViewById(R.id.tv_header_time);
        TV_header_status = (TextView) rootView.findViewById(R.id.tv_header_status);
        TV_header_fav = (TextView) rootView.findViewById(R.id.tv_header_fav);

        LV_program_detail = (ListView) rootView.findViewById(R.id.lv_program_detail);
        listProgramDetailAdapter = new ListProgramDetailAdapter(context, dataCustomProgramDetail);
        LV_program_detail.setAdapter(listProgramDetailAdapter);

        g_current_day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        g_current_date = calendar.get(Calendar.DAY_OF_MONTH);
        g_current_month = calendar.get(Calendar.MONTH);
        g_current_year = calendar.get(Calendar.YEAR) + 543;
        setChangeDay(g_current_day);

        SB_detail_date.setMax(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        SB_detail_date.setProgress(g_current_date);

/*
        TV_detail_day.setTypeface(MainActivity.TF_font);
        TV_detail_date.setTypeface(MainActivity.TF_font);
        TV_detail_month.setTypeface(MainActivity.TF_font);
        TV_detail_year.setTypeface(MainActivity.TF_font);
*/
        TV_detail_day.setText(arr_day[g_current_day]);
        TV_detail_date.setText(Integer.toString(g_current_date));
        TV_detail_month.setText(arr_month[g_current_month]);
        TV_detail_year.setText(Integer.toString(g_current_year));
/*
        TV_header_program.setTypeface(MainActivity.TF_font);
        TV_header_time.setTypeface(MainActivity.TF_font);
        TV_header_status.setTypeface(MainActivity.TF_font);
        TV_header_fav.setTypeface(MainActivity.TF_font);
*/
        TV_header_program.setTextSize(tv_header_tb_size);
        TV_header_time.setTextSize(tv_header_tb_size);
        TV_header_status.setTextSize(tv_header_tb_size);
        TV_header_fav.setTextSize(tv_header_tb_size);

        setDataToLV();


        SB_detail_date.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress <= 0)
                    progress = 1;
                chkIsToDay(progress);
                calendar.set(Calendar.DATE, progress);
                int c_day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                TV_detail_day.setText(arr_day[c_day]);

                setChangeDay(c_day);

                TV_detail_date.setText(Integer.toString(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                setDataToLV();
            }
        });


        IV_detail_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultToday();
            }
        });


        LV_program_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                detailProgram.seItem_selected(position);

                detailProgram.setProg_id(dataCustomProgramDetail.get(position).id);
                detailProgram.setProg_name(dataCustomProgramDetail.get(position).col_1);
                detailProgram.setTime_start(dataCustomProgramDetail.get(position).col_2);



                if (detailProgram.arrDelOrAdd.get(position).equals("add")) {

                    ViewSettingTime = activity.getLayoutInflater().inflate(R.layout.activity_setting_time_list, ContentFrame, false);
                    ContentFrame.removeAllViews();
                    settingTimeList = new SettingTimeList(ViewSettingTime);
                    ContentFrame.addView(ViewSettingTime);

                } else if (detailProgram.arrDelOrAdd.get(position).equals("delete")) {
                    position_for_delete = position;
                      menuActionDelete();
                    }


                Log.d("run", "Selcet List Position " + position);
            }
        });


    } // End of Constructor

    public void setHoldArrProg_idFromDB() {
        arrHoldProg_idDB.clear();
        SQLiteCursor cur = (SQLiteCursor) dbAction.readAllFavoriteProgram();

        while (!cur.isAfterLast()) {

            arrHoldProg_idDB.add(Integer.parseInt(cur.getString(1)));
            cur.moveToNext();

        }

        cur.close();

    }

    public boolean chkIsToDay(int i) {
        if (i == g_current_date)
            return selectIsToDay = true;
        else
            return selectIsToDay = false;
    }

    public void setChangeDay(int day) {
        g_change_day = day;
    }


    public void setDefaultToday() {
        setChangeDay(g_current_day);
        SB_detail_date.setProgress(g_current_date);
        TV_detail_day.setText(arr_day[g_current_day]);
        setDataToLV();


    }

    public void setDataToLV() {

            detailProgram.arrDelOrAdd.clear();
            listProgramDetailAdapter.arrayProgramDetail.clear();
            detailProgram.clearAllArray();

           setHoldArrProg_idFromDB();

            detailProgram.setDay_id(g_change_day);

            aq.id(IV_detail_list_title).image(detailProgram.getChan_pic());
            TV_detail_list_title.setText(detailProgram.getChan_name());


            int c = 0;
            for (int j = 0; j < arrDataStore_program.size(); j++) {

                int prog_id = arrDataStore_program.get(j).getProg_id();
                int chan_id = arrDataStore_program.get(j).getFr_channel_id();
                String prog_title = arrDataStore_program.get(j).getProg_name();
                String prog_timestart = arrDataStore_program.get(j).getProg_timestart();
                String prog_timeend = arrDataStore_program.get(j).getProg_timeend();
                String prog_type = "test type";
                String p_time = prog_timestart + "\n" + prog_timeend;
                int day_id = arrDataStore_program.get(j).getFr_day_id();
                boolean status_onair = false;

                if (detailProgram.getChan_id() == chan_id && detailProgram.getDay_id() == day_id) {

                    try {

                        Date date = new Date();

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                        Date TimeStart = simpleDateFormat.parse(prog_timestart);
                        Date TimeEnd = simpleDateFormat.parse(prog_timeend);
                        String TimeNow = simpleDateFormat.format(date);
                        String TS = simpleDateFormat.format(TimeStart);
                        String TE = simpleDateFormat.format(TimeEnd);
                        //Log.d("run",TimeStart+" , "+TimeEnd+" , "+TimeNow);
                        //  Log.d("run", day_id + " "+g_current_date+day_id + " "+g_current_date);
                        if (selectIsToDay) {
                            if (simpleDateFormat.parse(TimeNow).before((simpleDateFormat.parse(TE)))) {
                                if (simpleDateFormat.parse(TimeNow).before(simpleDateFormat.parse(TS))) {
                                    //  Log.d("run", c + " : " + TimeNow + " Not yet " + TS + " - " + TE);
                                    status_onair = false;
                                } else {
                                    //  Log.d("run", c + " : " + TimeNow + " NOW " + TS + " - " + TE);
                                    status_onair = true;
                                }
                            } else {
                                //   Log.d("run", c + " : " + TimeNow + " Over " + TS + " - " + TE);
                                status_onair = false;
                            }
                        }
                    } catch (Exception e) {
                        Log.d("run", "Error Parse Date " + e);
                    }

                    detailProgram.setProg_id(prog_id);
                    detailProgram.setProg_name(prog_title);
                    detailProgram.setTime_start(prog_timestart);


                    dataCustomProgramDetail.add(new DataCustomProgramDetail(prog_id, prog_title, p_time, status_onair, c));
                    c++;

                }
            }

            listProgramDetailAdapter.notifyDataSetChanged();

    }

    private void menuActionDelete() {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ยืนยันการลบ");
        builder.setMessage("คุณแน่ใจที่จะลบรายการ " + detailProgram.getProg_name(position_for_delete) + " ออกจากรายการโปรดหรือไม่");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean chkDeleted = dbAction.deleteFavoriteProgram(detailProgram.getProg_id(position_for_delete));
                        if (chkDeleted) {
                            Toast.makeText(context, "Delete Complete", Toast.LENGTH_SHORT).show();
                            setDataToLV();
                        } else {
                            Toast.makeText(context, "Can't Delete ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.show();
    }



}
