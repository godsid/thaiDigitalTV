package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.sec.android.allshare.Device;
import com.sec.android.allshare.DeviceFinder;
import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.ServiceConnector;
import com.sec.android.allshare.ServiceProvider;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    int tv_item_tb_size = 16;
    boolean selectIsToDay = true;
    DatabaseAction dbAction;
    private int position_for_delete;

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

    ArrayList<DataCustomProgramDetail> dataCustomProgramDetail;

    public static  ArrayList<Integer> arrHoldProg_idDB = new ArrayList<Integer>();
    ImageView IV_ic_nav_top_left, IV_detail_today, IV_detail_list_title, IV_tv_share;


    ArrayList<DataStore_Program> arrDataStore_program = MainActivity.arrDataStore_program;
    boolean haveTVinNetwork = false;
    int scoreFirstVisible = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_detail);

        aq = new AQuery(this);
        context = ProgramDetail.this;
        calendar = Calendar.getInstance();
        date = new Date();
        dbAction = new DatabaseAction(this);
        dataCustomProgramDetail = new ArrayList<DataCustomProgramDetail>();

        //TF_font = Typeface.createFromAsset(getAssets(), frontPath);


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


        LV_program_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("run", "P "+position);

                if (GlobalVariable.getArrDelOrAdd(position).equals("add")) {

                    Intent intent = new Intent(context, SettingAlert.class);
                    intent.putExtra("i_Prog_id", dataCustomProgramDetail.get(position).id);
                    intent.putExtra("i_Prog_name", dataCustomProgramDetail.get(position).pname);
                    intent.putExtra("i_Prog_timestart", dataCustomProgramDetail.get(position).pstart);
                    intent.putExtra("i_Chan_name", GlobalVariable.getChan_name());

                    startActivity(intent);
                } else if (GlobalVariable.getArrDelOrAdd(position).equals("delete")) {

                    position_for_delete = position;
                    menuActionDelete();
                }


                Log.d("run", "Selcet List Position " + position);
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

                if (haveTVinNetwork) {

                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivity(intent);
                } else {
                    Toast.makeText(ProgramDetail.this,"ตรวจสอบ : ไม่พบ TV ในเครือข่าย",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(ProgramDetail.this,"โปรดเชื่อมต่อ TV เข้ากับเครือข่าย",Toast.LENGTH_SHORT).show();
             }
            }
        });

        chkTVinNetwork();
    } //End Oncreate


    private void chkTVinNetwork() {
        ArrayList<Device> mDeviceList;
        DeviceFinder deviceFinder = GlobalVariable.getServiceProvider().getDeviceFinder();
        deviceFinder.setDeviceFinderEventListener(Device.DeviceType.DEVICE_TV_CONTROLLER, iDeviceFinderEventListener);
        deviceFinder.refresh();
        mDeviceList = deviceFinder.getDevices(Device.DeviceDomain.LOCAL_NETWORK, Device.DeviceType.DEVICE_TV_CONTROLLER);


        if (mDeviceList.size() > 0) {
            haveTVinNetwork = true;
            IV_device_share.setImageResource(R.drawable.tv_allshare);
            IV_device_share.setEnabled(true);

        } else {
            haveTVinNetwork = false;
            IV_device_share.setImageResource(R.drawable.ic_all_share_disconnect);

        }
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


    private void menuActionDelete() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ยืนยันการลบ");
        builder.setMessage("คุณแน่ใจที่จะลบรายการ " + GlobalVariable.getArrProg_name(position_for_delete) + " ออกจากรายการโปรดหรือไม่");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean chkDeleted = dbAction.deleteFavoriteProgram(GlobalVariable.getArrProg_id(position_for_delete));
                        if (chkDeleted) {
                            Toast.makeText(getApplicationContext(), "Delete Complete", Toast.LENGTH_SHORT).show();
                            setDataToLV();
                        } else {
                            Toast.makeText(getApplicationContext(), "Can't Delete ", Toast.LENGTH_SHORT).show();
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
        programDetailAdapter = new ProgramDetailAdapter(this, dataCustomProgramDetail);
        programDetailAdapter.arrayProgramDetail.clear();
        GlobalVariable.clearArrDelOrAdd();
        GlobalVariable.clearAllArray();

        setHoldArrProg_idFromDB();

        GlobalVariable.setDay_id(g_change_day);

        aq.id(IV_detail_list_title).image(GlobalVariable.getChan_pic());
        TV_detail_list_title.setText(GlobalVariable.getChan_name());


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

            if (GlobalVariable.getChan_id() == chan_id && GlobalVariable.getDay_id() == day_id) {

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


                dataCustomProgramDetail.add(new DataCustomProgramDetail(prog_id, prog_title, prog_timestart,prog_timeend, status_onair, c));
                c++;

            }
        }

        // listProgramDetailAdapter.notifyDataSetChanged();

        LV_program_detail.setAdapter(programDetailAdapter);
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
