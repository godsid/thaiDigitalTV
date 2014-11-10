package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.sec.android.allshare.Device;
import com.sec.android.allshare.DeviceFinder;
import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.control.TVController;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class ProgramDetail extends Activity {
    AQuery aq;

    TextView TV_header_program, TV_header_time, TV_header_status, TV_header_fav, TV_detail_list_title;
    TextView TV_detail_day, TV_detail_date, TV_detail_month, TV_detail_year;

    //public static Typeface TF_font;
    //public String frontPath = "fonts/RSU_BOLD.ttf";

    ListView LV_program_detail;
    private SeekBar SB_detail_date = null;
    int tv_header_tb_size = 18;
    boolean selectIsToDay = true;
    DatabaseAction dbAction;

    ImageView IV_device_share;
    Context context;

    Calendar calendar;
    Date date;
    String[] arr_day = new String[]{"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};
    String[] arr_month = new String[]{"มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม",
            "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};
    int g_current_day, g_current_date, g_current_month, g_current_year;
    int g_change_day;

    ProgramDetailAdapter programDetailAdapter;
    ArrayList<DataCustomProgramDetail> dataCustomProgramDetail = new ArrayList<DataCustomProgramDetail>();

    public static  ArrayList<Integer> arrHoldProg_idDB = new ArrayList<Integer>();
    ImageView IV_ic_nav_top_left, IV_detail_today, IV_detail_list_title;


    ArrayList<DataStore_Program> arrDataStore_program = MainActivity.arrDataStore_program;
    public static boolean haveTVinNetwork = false;
    int scoreFirstVisible = 0;


    private TVController mTVController = null;
    Tracker t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_detail);

        aq = new AQuery(this);
        context = ProgramDetail.this;
        calendar = Calendar.getInstance();
        date = new Date();
        dbAction = new DatabaseAction(this);

        programDetailAdapter = new ProgramDetailAdapter(this,dataCustomProgramDetail);
        //TF_font = Typeface.createFromAsset(getAssets(), frontPath);

        t = ((MyApplication)getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        TV_detail_list_title = (TextView) findViewById(R.id.tv_detail_list_title);
        //TV_detail_list_title.setTypeface(TF_font);
        //TV_detail_list_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        IV_detail_list_title = (ImageView) findViewById(R.id.iv_detail_list_title);

        IV_device_share = (ImageView) findViewById(R.id.iv_device_share);

        IV_ic_nav_top_left = (ImageView) findViewById(R.id.ic_nav_top_left);

        IV_detail_today = (ImageView) findViewById(R.id.iv_detail_today);
        TV_detail_day = (TextView) findViewById(R.id.tv_detail_day);
        TV_detail_date = (TextView) findViewById(R.id.tv_detail_date);
        TV_detail_month = (TextView) findViewById(R.id.tv_detail_month);
        TV_detail_year = (TextView) findViewById(R.id.tv_detail_year);
        //   TV_detail_list_title.setTypeface(MainTF_font);

        SB_detail_date = (SeekBar) findViewById(R.id.sb_detail_date);

        TV_header_program = (TextView) findViewById(R.id.tv_header_program);
        TV_header_time = (TextView) findViewById(R.id.tv_header_time);
        TV_header_status = (TextView) findViewById(R.id.tv_header_status);
        TV_header_fav = (TextView) findViewById(R.id.tv_header_fav);

        LV_program_detail = (ListView) findViewById(R.id.lv_program_detail);
        LV_program_detail.setAdapter(programDetailAdapter);

        g_current_day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        g_current_date = calendar.get(Calendar.DAY_OF_MONTH);
        g_current_month = calendar.get(Calendar.MONTH);
        g_current_year = calendar.get(Calendar.YEAR) + 543;
        setChangeDay(g_current_day);

        SB_detail_date.setMax(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        SB_detail_date.setProgress(g_current_date);

        TV_detail_day.setText(arr_day[g_current_day]);
        TV_detail_date.setText(Integer.toString(g_current_date));
        TV_detail_month.setText(arr_month[g_current_month]);
        TV_detail_year.setText(Integer.toString(g_current_year));

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


        IV_ic_nav_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        IV_device_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogDeviceList(context);
            }
        });

        chkTVinNetwork();
    } //End Oncreate


    private void chkTVinNetwork() {

        DeviceFinder deviceFinder = GlobalVariable.getServiceProvider().getDeviceFinder();
        deviceFinder.setDeviceFinderEventListener(Device.DeviceType.DEVICE_TV_CONTROLLER, iDeviceFinderEventListener);
        ArrayList<Device> mDeviceList = deviceFinder.getDevices(Device.DeviceDomain.LOCAL_NETWORK, Device.DeviceType.DEVICE_TV_CONTROLLER);
        //deviceFinder.refresh();
        if (mDeviceList != null) {
            if (mDeviceList.size() > 0) {
                haveTVinNetwork = true;
                IV_device_share.setImageResource(R.drawable.ic_share);
                IV_device_share.setEnabled(true);
                GlobalVariable.arrDeviceList = mDeviceList;
                mTVController = (TVController) GlobalVariable.arrDeviceList.get(0);
                mTVController.setEventListener(mEventListener);
            } else {
                IV_device_share.setEnabled(false);
                haveTVinNetwork = false;
                IV_device_share.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
        } else {
            IV_device_share.setEnabled(false);
            haveTVinNetwork = false;
            IV_device_share.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        programDetailAdapter.notifyDataSetChanged();
        Log.d("run","mDeviceList "+mDeviceList.size());
    }

    private final DeviceFinder.IDeviceFinderEventListener iDeviceFinderEventListener = new DeviceFinder.IDeviceFinderEventListener() {


        @Override
        public void onDeviceAdded(Device.DeviceType deviceType, Device device, ERROR error) {
            Log.d("run","onDeviceAdded");
            chkTVinNetwork();
        }

        @Override
        public void onDeviceRemoved(Device.DeviceType deviceType,Device device, ERROR error) {
            Log.d("run","onDeviceRemoved");
            chkTVinNetwork();
        }
    };

    private TVController.IEventListener mEventListener = new TVController.IEventListener()
    {
        @Override
        public void onStringChanged(TVController tv, String text, ERROR result)
        {
            chkTVinNetwork();
            Log.d("run","IEventListener");
        }

        @Override
        public void onDisconnected(TVController tv, ERROR result)
        {
            chkTVinNetwork();
            setDataToLV();

            Log.d("run","onDisconnected");
        }

    };




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
        Log.d("run", "========== ProgramDetail setDataToLV =========");
        //programDetailAdapter = new ProgramDetailAdapter(this, dataCustomProgramDetail);
        programDetailAdapter.arrayProgramDetail.clear();
        GlobalVariable.clearAllArray();

        setHoldArrProg_idFromDB();

        GlobalVariable.setDay_id(g_change_day);

        aq.id(IV_detail_list_title).image(GlobalVariable.getChan_pic());
        TV_detail_list_title.setText(GlobalVariable.getChan_name());

        t.setScreenName("ช่อง_"+GlobalVariable.getChan_id()+"_"+GlobalVariable.getChan_name());
        t.send(new HitBuilders.AppViewBuilder().build());

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
            boolean have_in_db;

            if (GlobalVariable.getChan_id() == chan_id && GlobalVariable.getDay_id() == day_id) {

                try {

                    Date date = new Date();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                    Date TimeStart = simpleDateFormat.parse(prog_timestart);
                    Date TimeEnd = simpleDateFormat.parse(prog_timeend);
                    String TimeNow = simpleDateFormat.format(date);
                    String TS = simpleDateFormat.format(TimeStart);
                    String TE = simpleDateFormat.format(TimeEnd);

                    if (selectIsToDay) {
                        if (simpleDateFormat.parse(TimeNow).before((simpleDateFormat.parse(TE)))) {
                            if (simpleDateFormat.parse(TimeNow).before(simpleDateFormat.parse(TS))) {
                                //  Log.d("run", c + " : " + TimeNow + " Not yet " + TS + " - " + TE);
                                status_onair = false;
                            } else {
                                //  Log.d("run", c + " : " + TimeNow + " NOW " + TS + " - " + TE);
                                status_onair = true;
                                scoreFirstVisible = c;

                            }
                        } else {
                            //   Log.d("run", c + " : " + TimeNow + " Over " + TS + " - " + TE);
                            status_onair = false;
                        }
                    }
                } catch (Exception e) {
                    Log.d("run", "Error Parse Date " + e);
                }

                GlobalVariable.addArrProg_id(prog_id);
                GlobalVariable.addArrProg_name(prog_title);
                GlobalVariable.addArrProg_timestart(prog_timestart);
                if (ProgramDetail.arrHoldProg_idDB.contains(prog_id)) {
                    have_in_db = true;
                } else {
                    have_in_db = false;
                }

                dataCustomProgramDetail.add(new DataCustomProgramDetail(prog_id, prog_title, prog_timestart,prog_timeend, status_onair, c, have_in_db));
                c++;

            }
        }

        programDetailAdapter.notifyDataSetChanged();


        if (selectIsToDay) {
            LV_program_detail.setSelection(scoreFirstVisible);
        } else {
            LV_program_detail.setSelection(0);
        }


    }


    @Override
    protected void onResume() {
        setDataToLV();
        chkTVinNetwork();
        super.onResume();
    }

    /*
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
    */


}
