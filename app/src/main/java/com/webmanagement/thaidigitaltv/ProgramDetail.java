package com.webmanagement.thaidigitaltv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ProgramDetail extends SherlockFragmentActivity {
    AQuery aq;

    TextView TV_header_program, TV_header_time, TV_header_status, TV_header_fav;
    TextView TV_detail_day,TV_now ,TV_detail_date, TV_detail_month, TV_detail_year;

    int tv_header_tb_size = 18;
    DatabaseAction dbAction;

    Context context;

    Calendar calendar;
    Date date;
    String[] arr_day = new String[]{"","อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};
    String[] arr_month = new String[]{"มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม",
            "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};

    int current_day;
    int current_month;
    int current_year;
    int current_date;


    ImageView  IV_now;

    private TVController mTVController = null;
    Tracker t;

    private ActionBar actionBar;
    private ViewPager viewPager;

    FragmentProgramDetailAdapter fragmentProgramDetailAdapter;
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

        t = ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        IV_now = (ImageView) findViewById(R.id.iv_now);
        TV_now = (TextView) findViewById(R.id.tv_now);
        TV_detail_day = (TextView) findViewById(R.id.tv_detail_day);
        TV_detail_date = (TextView) findViewById(R.id.tv_detail_date);
        TV_detail_month = (TextView) findViewById(R.id.tv_detail_month);
        TV_detail_year = (TextView) findViewById(R.id.tv_detail_year);
        //   TV_detail_list_title.setTypeface(MainTF_font);

        TV_header_program = (TextView) findViewById(R.id.tv_header_program);
        TV_header_time = (TextView) findViewById(R.id.tv_header_time);
        TV_header_status = (TextView) findViewById(R.id.tv_header_status);
        TV_header_fav = (TextView) findViewById(R.id.tv_header_fav);


        current_day = calendar.get(Calendar.DAY_OF_WEEK);
        current_date = calendar.get(Calendar.DAY_OF_MONTH);
        current_month = calendar.get(Calendar.MONTH);
        current_year = calendar.get(Calendar.YEAR) + 543;

        GlobalVariable.setMAX_DAY_OF_MONTH(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        TV_detail_day.setText(arr_day[current_day]);
        TV_detail_date.setText(Integer.toString(current_date));
        TV_detail_month.setText(arr_month[current_month]);
        TV_detail_year.setText(Integer.toString(current_year));

        TV_header_program.setTextSize(tv_header_tb_size);
        TV_header_time.setTextSize(tv_header_tb_size);
        TV_header_status.setTextSize(tv_header_tb_size);
        TV_header_fav.setTextSize(tv_header_tb_size);


        viewPager = (ViewPager) findViewById(R.id.pager_program_detail);
        FragmentManager fm = getSupportFragmentManager();


        fragmentProgramDetailAdapter = new FragmentProgramDetailAdapter(fm);
        viewPager.setAdapter(fragmentProgramDetailAdapter);
        viewPager.setCurrentItem(current_date-1);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

             }

            @Override
            public void onPageSelected(int i) {
                TV_detail_date.setText(Integer.toString(i+1));
                calendar.set(Calendar.DATE, i+1);
                int c_day = calendar.get(Calendar.DAY_OF_WEEK);
                TV_detail_day.setText(arr_day[c_day]);
                Log.d("run","onPageSelected "+i+" "+c_day);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        IV_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_anim_pressed));
                TV_now.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_anim_pressed));
                viewPager.setCurrentItem(current_date-1);
                FragmentProgramDetail.setDefaultSelection();

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
                GlobalVariable.setHaveTVNetwork(true);
                GlobalVariable.arrDeviceList = mDeviceList;
                mTVController = (TVController) GlobalVariable.arrDeviceList.get(0);
                mTVController.setEventListener(mEventListener);
            } else {
                GlobalVariable.setHaveTVNetwork(false);

            }
        } else {
            GlobalVariable.setHaveTVNetwork(false);
        }
        invalidateOptionsMenu();
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
            Log.d("run", "onDisconnected");
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.activity_program_detail, menu);
        if (GlobalVariable.isHaveTVNetwork()) {
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
                if (!GlobalVariable.isHaveTVNetwork()) {
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
        chkTVinNetwork();
        //FragmentProgramDetail.programDetailAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
