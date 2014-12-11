package com.webmanagement.thaidigitaltv;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.AQuery;
import com.google.android.gms.analytics.Tracker;
import com.sec.android.allshare.Device;
import com.sec.android.allshare.DeviceFinder;
import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.control.TVController;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ProgramDetail extends SherlockFragmentActivity {
    AQuery aq;

    TextView TV_header_program, TV_header_time, TV_header_status, TV_header_fav;
    TextView TV_detail_day,TV_now ,TV_detail_date, TV_detail_month, TV_detail_year;

    //public static Typeface TF_font;
    //public String frontPath = "fonts/RSU_BOLD.ttf";

    ListView LV_program_detail;
    private SeekBar SB_detail_date = null;
    int tv_header_tb_size = 18;
    boolean selectIsToDay = true;
    DatabaseAction dbAction;

    //ImageView IV_device_share;
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

    public static ArrayList<Integer> arrHoldProg_idDB = new ArrayList<Integer>();
    ImageView  IV_now;


    ArrayList<DataStore_Program> arrDataStore_program = MainActivity.arrDataStore_program;
    public static boolean haveTVinNetwork = false;
    int scoreFirstVisible = 0;


    private TVController mTVController = null;
    Tracker t;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_detail);

        aq = new AQuery(this);
        context = ProgramDetail.this;
        calendar = Calendar.getInstance();
        date = new Date();
        dbAction = new DatabaseAction(this);
        actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(GlobalVariable.getChan_name());


        try {
            String urlPic = MainActivity.dirImage+"/"+GlobalVariable.getChan_id()+".png";
            Log.d("run"," u : "+urlPic);
            File bitmapFile = new File(urlPic);
            Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.toString());
            Bitmap bmr = getResizedBitmap(bitmap,96 ,96);
            Drawable icon = new BitmapDrawable(getResources(), bmr);
            actionBar.setIcon(icon);
        } catch (Exception e){

            Log.d("run",e+"");
        }



        programDetailAdapter = new ProgramDetailAdapter(this, dataCustomProgramDetail);
        //TF_font = Typeface.createFromAsset(getAssets(), frontPath);

        t = ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        //TV_detail_list_title.setTypeface(TF_font);
        //TV_detail_list_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        //IV_device_share = (ImageView) findViewById(R.id.iv_device_share);

        IV_now = (ImageView) findViewById(R.id.iv_now);
        TV_now = (TextView) findViewById(R.id.tv_now);
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


        IV_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_anim_pressed));
                TV_now.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_anim_pressed));

                setDefaultToday();

            }
        });


        chkTVinNetwork();
    } //End Oncreate


    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


    private void chkTVinNetwork() {

        DeviceFinder deviceFinder = GlobalVariable.getServiceProvider().getDeviceFinder();
        deviceFinder.refresh();
        deviceFinder.setDeviceFinderEventListener(Device.DeviceType.DEVICE_TV_CONTROLLER, iDeviceFinderEventListener);
        ArrayList<Device> mDeviceList = deviceFinder.getDevices(Device.DeviceDomain.LOCAL_NETWORK, Device.DeviceType.DEVICE_TV_CONTROLLER);
        deviceFinder.refresh();
        if (mDeviceList != null) {
            if (mDeviceList.size() > 0) {
                haveTVinNetwork = true;
                GlobalVariable.arrDeviceList = mDeviceList;
                mTVController = (TVController) GlobalVariable.arrDeviceList.get(0);
                mTVController.setEventListener(mEventListener);
            } else {
                haveTVinNetwork = false;

            }
        } else {
            haveTVinNetwork = false;


        }
        invalidateOptionsMenu();
        programDetailAdapter.notifyDataSetChanged();
        Log.d("run", "mDeviceList " + mDeviceList.size());
    }

    private DeviceFinder.IDeviceFinderEventListener iDeviceFinderEventListener = new DeviceFinder.IDeviceFinderEventListener() {


        @Override
        public void onDeviceAdded(Device.DeviceType deviceType, Device device, ERROR error) {
            Log.d("run", "onDeviceAdded");
            chkTVinNetwork();
        }

        @Override
        public void onDeviceRemoved(Device.DeviceType deviceType, Device device, ERROR error) {
            Log.d("run", "onDeviceRemoved");
            chkTVinNetwork();
        }
    };

    private TVController.IEventListener mEventListener = new TVController.IEventListener() {
        @Override
        public void onStringChanged(TVController tv, String text, ERROR result) {
            chkTVinNetwork();
            Log.d("run", "IEventListener");
        }

        @Override
        public void onDisconnected(TVController tv, ERROR result) {
            chkTVinNetwork();
            setDataToLV();

            Log.d("run", "onDisconnected");
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




        int c = 0;
        for (int j = 0; j < arrDataStore_program.size(); j++) {

            int prog_id = arrDataStore_program.get(j).getProg_id();
            int chan_id = arrDataStore_program.get(j).getFr_channel_id();
            String prog_title = arrDataStore_program.get(j).getProg_name();
            String prog_timestart = arrDataStore_program.get(j).getProg_timestart();
            String prog_timeend = arrDataStore_program.get(j).getProg_timeend();
            //String day = "test type";
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

                dataCustomProgramDetail.add(new DataCustomProgramDetail(prog_id, prog_title, prog_timestart, prog_timeend, status_onair, c, have_in_db,day_id));
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.activity_program_detail, menu);
        if (haveTVinNetwork) {
            //v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_anim_pressed));

            menu.findItem(R.id.action_share).setVisible(true);
        } else {
            menu.findItem(R.id.action_share).setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        Log.d("run","onOptionsItemSelected : "+item.getItemId());
        //item.getActionView().startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_anim_pressed));
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                break;
            case R.id.action_share :
                chkTVinNetwork();
                if (!haveTVinNetwork) {
                    Toast.makeText(context,"ขาดการเชื่อมต่อกับ TV",Toast.LENGTH_LONG);
                    return super.onOptionsItemSelected(item);
                } else {
                    new DialogDeviceList(context);
                }
                break;
        }


        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onResume() {
        setDataToLV();
        chkTVinNetwork();
        super.onResume();
    }


}
