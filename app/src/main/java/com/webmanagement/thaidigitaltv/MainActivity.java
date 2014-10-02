package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.Progress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends Activity {
    ImageView IV_ic_nav_top_left, IV_ic_nav_top_right,IV_ic_fav_top_right,IV_detail_today,IV_detail_list_title;
    ExpandableListView EXP_exp_left, EXP_exp_right;
    DrawerLayout DL_drawer_layout;
    DetailProgram detailProgram;
    private DatabaseAction dbAction;

    private ExpandableListAdapter_Left ExpAdapter;
    //static String urlPath = "https://dl.dropboxusercontent.com/s/w7ih0hrbius82rj/menu_item3.js";
    static String urlPath = "https://dl.dropboxusercontent.com/u/40791893/pic_android/item4.js";
    ArrayList<GroupExpLeft> group_list;
    ArrayList<ItemExpLeft> channel_list;

    ListProgramDetailAdapter listProgramDetailAdapter;
    ArrayList<DataCustomProgramDetail> dataCustomProgramDetail = new ArrayList<DataCustomProgramDetail>();
    ListView listView;

    private static  boolean stateOK = false;
    private int position_for_delete;

    private int exp_left_group_pos,exp_left_child_pos;

    Typeface TF_font;
    String frontPath = "fonts/RSU_BOLD.ttf";

    TextView TV_header_program, TV_header_time, TV_header_status, TV_header_fav, TV_detail_list_title;

    private SeekBar SB_detail_date = null;
    int tv_header_tb_size = 18;
    int tv_item_tb_size = 16;

    TextView TV_detail_day, TV_detail_date, TV_detail_month, TV_detail_year;
    Calendar calendar;
    Date date;
    String[] arr_day = new String[] {"อาทิตย์","จันทร์","อังคาร","พุธ","พฤหัสบดี","ศุกร์","เสาร์"};
    String[] arr_month = new String[] {"มกราคม","กุมภาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฎาคม",
            "สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันวาคม"  };
    int g_current_day,g_current_date,g_current_month,g_current_year;
    int g_change_day;
    ProgressDialog progressDialog;

    SimpleDateFormat simpleDateFormat;

    AQuery aq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        detailProgram = new DetailProgram();
        dbAction = new DatabaseAction(this);
        calendar = Calendar.getInstance();
        date = new Date();
        aq = new AQuery(this);

        LinearLayout hiddenLayout = (LinearLayout) findViewById(R.id.ll_detail_list);
        if (hiddenLayout == null) {

            FrameLayout myLayout = (FrameLayout) findViewById(R.id.content_frame);
            View hiddenInfo = getLayoutInflater().inflate(R.layout.activity_detail_list, myLayout, false);
            myLayout.addView(hiddenInfo);
        }


        listProgramDetailAdapter = new ListProgramDetailAdapter(getApplicationContext(),dataCustomProgramDetail);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(listProgramDetailAdapter);



        IV_ic_nav_top_left = (ImageView) findViewById(R.id.ic_nav_top_left);
        IV_ic_nav_top_right = (ImageView) findViewById(R.id.ic_nav_top_right);
        EXP_exp_left = (ExpandableListView) findViewById(R.id.exp_left);
        EXP_exp_right = (ExpandableListView) findViewById(R.id.exp_right);
        DL_drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        IV_ic_fav_top_right = (ImageView) findViewById(R.id.ic_fav_top_right);

        IV_detail_today = (ImageView) findViewById(R.id.iv_detail_today);

        TF_font = Typeface.createFromAsset(getAssets(), frontPath);

        TV_detail_list_title = (TextView) findViewById(R.id.tv_detail_list_title);
        TV_detail_list_title.setTypeface(TF_font);
        TV_detail_list_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        IV_detail_list_title = (ImageView) findViewById(R.id.iv_detail_list_title);

        TV_detail_day = (TextView) findViewById(R.id.tv_detail_day);
        TV_detail_date = (TextView) findViewById(R.id.tv_detail_date);
        TV_detail_month = (TextView) findViewById(R.id.tv_detail_month);
        TV_detail_year = (TextView) findViewById(R.id.tv_detail_year);

        SB_detail_date = (SeekBar) findViewById(R.id.sb_detail_date);

        TV_header_program = (TextView) findViewById(R.id.tv_header_program);
        TV_header_time = (TextView) findViewById(R.id.tv_header_time);
        TV_header_status = (TextView) findViewById(R.id.tv_header_status);
        TV_header_fav = (TextView) findViewById(R.id.tv_header_fav);

        g_current_day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        g_current_date = calendar.get(Calendar.DAY_OF_MONTH);
        g_current_month = calendar.get(Calendar.MONTH);
        g_current_year = calendar.get(Calendar.YEAR)+543;

        setChangeDay(g_current_day);



        SB_detail_date.setMax(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        SB_detail_date.setProgress(g_current_date);


        TV_detail_day.setTypeface(TF_font);
        TV_detail_date.setTypeface(TF_font);
        TV_detail_month.setTypeface(TF_font);
        TV_detail_year.setTypeface(TF_font);

        TV_detail_day.setText(arr_day[g_current_day]);
        TV_detail_date.setText(Integer.toString(g_current_date));
        TV_detail_month.setText(arr_month[g_current_month]);
        TV_detail_year.setText(Integer.toString(g_current_year));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("กำลังโหลดข้อมูล...");
        progressDialog.setTitle("กรุณารอสักครู่");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();


       prepareMenuLeft();


        // aq.id(IV_title_detail_list)
        //       .image(detailProgram.getChan_pic())


        openFirstProgramDetail();




        IV_ic_fav_top_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFavoriteList();
            }
        });


        IV_ic_nav_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DL_drawer_layout.isDrawerOpen(EXP_exp_left)) {
                    DL_drawer_layout.closeDrawer(EXP_exp_left);
                } else {
                    DL_drawer_layout.openDrawer(EXP_exp_left);
                }

                if (DL_drawer_layout.isDrawerOpen(EXP_exp_right)) {
                    DL_drawer_layout.closeDrawer(EXP_exp_right);
                    DL_drawer_layout.openDrawer(EXP_exp_left);
                }

            }
        });


        IV_ic_nav_top_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DL_drawer_layout.isDrawerOpen(EXP_exp_right)) {
                    DL_drawer_layout.closeDrawer(EXP_exp_right);
                } else {
                    DL_drawer_layout.openDrawer(EXP_exp_right);
                }

                if (DL_drawer_layout.isDrawerOpen(EXP_exp_left)) {
                    DL_drawer_layout.closeDrawer(EXP_exp_left);
                    DL_drawer_layout.openDrawer(EXP_exp_right);
                }
            }
        });





        TV_header_program.setTypeface(TF_font);
        TV_header_time.setTypeface(TF_font);
        TV_header_status.setTypeface(TF_font);
        TV_header_fav.setTypeface(TF_font);

        TV_header_program.setTextSize(tv_header_tb_size);
        TV_header_time.setTextSize(tv_header_tb_size);
        TV_header_status.setTextSize(tv_header_tb_size);
        TV_header_fav.setTextSize(tv_header_tb_size);



        EXP_exp_left.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String get_channel_name = group_list.get(groupPosition).getItems().get(childPosition).getName();
                int get_channel_id = group_list.get(groupPosition).getItems().get(childPosition).getId();
                String get_channel_pic = group_list.get(groupPosition).getItems().get(childPosition).getImage();
                //get position highlight
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);
                //end
                exp_left_group_pos = groupPosition;
                exp_left_child_pos = childPosition;

                detailProgram.setChan_id(get_channel_id);
                detailProgram.setChan_name(get_channel_name);
                detailProgram.setChan_pic(get_channel_pic);

                openProgramDetail();

                Toast.makeText(getApplicationContext(), get_channel_id + " " + get_channel_name + "",
                        Toast.LENGTH_SHORT).show();

                DL_drawer_layout.closeDrawer(EXP_exp_left);

                return false;

            }
        });


        SB_detail_date.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){

                if (progress <= 0)
                    progress = 1;

                calendar.set(Calendar.DATE,progress);
                int c_day = calendar.get(Calendar.DAY_OF_WEEK)-1;
                TV_detail_day.setText(arr_day[c_day]);

                setChangeDay(c_day);

                TV_detail_date.setText(Integer.toString(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                openProgramDetail();
            }
        });


        IV_detail_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultToday();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                detailProgram.seItem_selected(position);

                detailProgram.setProg_id(dataCustomProgramDetail.get(position).id);
                detailProgram.setProg_name(dataCustomProgramDetail.get(position).col_1);
                detailProgram.setTime_start(dataCustomProgramDetail.get(position).col_2);

                detailProgram.setType_name("TypeTEST");

                if (listProgramDetailAdapter.getArrDelorAdd(position).equals("add")) {
                    goSettimeList();
                    Log.d("run","IF"+listProgramDetailAdapter.getArrDelorAdd(position)+","+position);
                } else if(listProgramDetailAdapter.getArrDelorAdd(position).equals("delete")){
                    position_for_delete = position;
                    menuActionDelete();
                    //setEexpLeftChildSelected(2);
                    Log.d("run","EL"+listProgramDetailAdapter.getArrDelorAdd(position)+","+position);
                }


                Log.d("run","Selcet List Position "+position);
            }
        });


    }

    public void setChangeDay(int day) {
        g_change_day = day;
    }


    public void setDefaultToday() {
        setChangeDay(g_current_day);
        SB_detail_date.setProgress(g_current_date);
        TV_detail_day.setText(arr_day[g_current_day]);
        openProgramDetail();

    }

    public static boolean setStateOK(boolean b) {
        return stateOK = b;
    }
    @Override
    protected void onResume() {

        if (stateOK)
            setEexpLeftChildSelected();

        Log.d("run","onResume");
        super.onResume();
    }


    @Override
    protected void onPause() {

        Log.d("run","onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d("run","onDestroy");
        super.onDestroy();
    }

    public void setEexpLeftChildSelected() {
        int child_pos = 0;
        try {
            if (exp_left_group_pos == 0)
                child_pos = exp_left_group_pos + 1;
            else
                child_pos = exp_left_group_pos - 1;

            EXP_exp_left.setSelectedChild(exp_left_group_pos,child_pos,true);
            openProgramDetail();
        } catch (Exception e) {
            EXP_exp_left.setSelectedChild(exp_left_group_pos,(exp_left_child_pos),true);
            openProgramDetail();
        }

    }


    public void menuActionDelete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ยืนยันการลบ");
        builder.setMessage("คุณแน่ใจที่จะลบรายการ " + detailProgram.getProg_name(position_for_delete) + " ออกจากรายการโปรดหรือไม่");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean chkDeleted = dbAction.deleteFavoriteProgram(detailProgram.getProg_id(position_for_delete));
                        if (chkDeleted) {
                            Toast.makeText(MainActivity.this, "Delete Complete", Toast.LENGTH_SHORT).show();
                            setEexpLeftChildSelected();
                        }else {
                            Toast.makeText(MainActivity.this, "Can't Delete ", Toast.LENGTH_SHORT).show();
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



    public void goSettimeList() {

        Intent intent = new Intent(MainActivity.this, SettingTimeList.class);
        startActivity(intent);
    }

    public void gotoFavoriteList() {
        stateOK = false;
        Intent intent = new Intent(MainActivity.this, FavoriteList.class);
        startActivity(intent);
    }


    public void openFirstProgramDetail() {

        listProgramDetailAdapter.clearArrDelOrAdd();
        listProgramDetailAdapter.clearHoldArrProg_IdFromDB();
        listProgramDetailAdapter.arrayProgramDetail.clear();
        detailProgram.clearAllArray();

        listProgramDetailAdapter.setHoldArrProg_idFromDB();

        detailProgram.setDay_id(g_change_day);

        aq.progress(progressDialog)
                .ajax(urlPath, JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {

                        if (object != null) {

                            try {
                                JSONArray items_array_prog = object.getJSONArray("allitems").getJSONObject(3).getJSONArray("tb_program");
                                JSONArray items_array_chan = object.getJSONArray("allitems").getJSONObject(1).getJSONArray("tb_channel");

                                String items_chan_title, items_chan_pic;
                                int items_chan_id;

                                items_chan_title = items_array_chan.getJSONObject(0).getString("channel_name");
                                items_chan_pic = items_array_chan.getJSONObject(0).getString("channel_pic");
                                items_chan_id = items_array_chan.getJSONObject(0).getInt("channel_id");


                                detailProgram.setChan_id(items_chan_id);
                                detailProgram.setChan_name(items_chan_title);
                                detailProgram.setChan_pic(items_chan_pic);

                                TV_detail_list_title.setText(items_chan_title);
                                aq.id(IV_detail_list_title).image(items_chan_pic);
                                int c = 0;
                                for (int j = 0; j < items_array_prog.length(); j++) {

                                    int prog_id = items_array_prog.getJSONObject(j).getInt("prog_id");
                                    int chan_id = items_array_prog.getJSONObject(j).getInt("channel_id");
                                    String prog_title = items_array_prog.getJSONObject(j).getString("prog_name");
                                    String prog_timestart = items_array_prog.getJSONObject(j).getString("time_start");
                                    String prog_timeend = items_array_prog.getJSONObject(j).getString("time_end");
                                    String prog_type = "test type";
                                    String p_time = prog_timestart + "\n" + prog_timeend;
                                    int day_id = items_array_prog.getJSONObject(j).getInt("day_id");

                                    if (detailProgram.getChan_id() == chan_id && detailProgram.getDay_id() == day_id) {

                                        simpleDateFormat = new SimpleDateFormat("HH:mm");



                                        if (prog_timestart.compareTo(prog_timeend) < 0) {
                                            System.out.println("date1 is before date2");
                                        } else if (date1.compareTo(date2) > 0) {
                                            System.out.println("date1 is after date2");
                                        } else {
                                            System.out.println("date1 is equal to date2");
                                        }

                                        dataCustomProgramDetail.add(new DataCustomProgramDetail(prog_id, prog_title, p_time, true, c));

                                        //Log.d("logrun2", "prog_id,prog_title " + prog_id + "," + prog_title);
                                        detailProgram.setProg_id(prog_id);
                                        detailProgram.setProg_name(prog_title);
                                        detailProgram.setTime_start(prog_timestart);
                                        detailProgram.setType_name("TypeTEST");
                                        c++;
                                    }
                                }

                                listProgramDetailAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("logrun2", e.toString());
                            }
                        } else {
                            Log.d("logrun2", "Object is Null");
                        }

                        // super.callback(url, object, status);
                    }
                });

        Log.d("run","C "+detailProgram.getChan_pic() );

    }



    public void openProgramDetail() {

        listProgramDetailAdapter.clearArrDelOrAdd();
        listProgramDetailAdapter.clearHoldArrProg_IdFromDB();
        listProgramDetailAdapter.arrayProgramDetail.clear();
        detailProgram.clearAllArray();

        listProgramDetailAdapter.setHoldArrProg_idFromDB();

        detailProgram.setDay_id(g_change_day);

        aq.id(IV_detail_list_title).image(detailProgram.getChan_pic());
        TV_detail_list_title.setText(detailProgram.getChan_name());

        aq.progress(progressDialog)
                .ajax(urlPath, JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {

                        if (object != null) {

                            try {
                                JSONArray items_array_prog = object.getJSONArray("allitems").getJSONObject(3).getJSONArray("tb_program");

                                int c = 0;
                                for (int j = 0; j < items_array_prog.length(); j++) {

                                    int prog_id = items_array_prog.getJSONObject(j).getInt("prog_id");
                                    int chan_id = items_array_prog.getJSONObject(j).getInt("channel_id");
                                    String prog_title = items_array_prog.getJSONObject(j).getString("prog_name");
                                    String prog_timestart = items_array_prog.getJSONObject(j).getString("time_start");
                                    String prog_timeend = items_array_prog.getJSONObject(j).getString("time_end");
                                    String prog_type = "test type";
                                    String p_time = prog_timestart + "\n" + prog_timeend;
                                    int day_id = items_array_prog.getJSONObject(j).getInt("day_id");


                                    if (detailProgram.getChan_id() == chan_id && detailProgram.getDay_id() == day_id) {

                                        detailProgram.setProg_id(prog_id);
                                        detailProgram.setProg_name(prog_title);
                                        detailProgram.setTime_start(prog_timestart);

                                        detailProgram.setType_name("TypeTEST");

                                        dataCustomProgramDetail.add(new DataCustomProgramDetail(prog_id, prog_title, p_time, true, c));
                                        c++;

                                    }
                                }


                                listProgramDetailAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("logrun2", e.toString());
                            }
                        } else {
                            Log.d("logrun2", "Object is Null");
                        }

                        // super.callback(url, object, status);
                    }
                });

    }


    public void prepareMenuLeft() {
        aq.ajax(urlPath,
                JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {


                        if (object != null) {

                            try {

                                JSONArray items_array_cate = object.getJSONArray("allitems").getJSONObject(0).getJSONArray("tb_category");
                                JSONArray items_array_chan = object.getJSONArray("allitems").getJSONObject(1).getJSONArray("tb_channel");

                                String items_cate_title, items_cate_pic, items_chan_title, items_chan_pic;
                                int items_cate_id, items_chan_id, items_cate_id_in_chan;

                                group_list = new ArrayList<GroupExpLeft>();

                                for (int i = 0; i < items_array_cate.length(); i++) {

                                    items_cate_title = items_array_cate.getJSONObject(i).getString("category_name");
                                    items_cate_pic = items_array_cate.getJSONObject(i).getString("category_pic");
                                    items_cate_id = items_array_cate.getJSONObject(i).getInt("category_id");

                                    //Log.d("logrun2", items_cate_id+"  "+items_cate_title);


                                    GroupExpLeft gru = new GroupExpLeft();
                                    gru.setName(items_cate_title);
                                    gru.setImage(items_cate_pic);

                                    channel_list = new ArrayList<ItemExpLeft>();

                                    for (int j = 0; j < items_array_chan.length(); j++) {
                                        items_chan_title = items_array_chan.getJSONObject(j).getString("channel_name");
                                        items_chan_pic = items_array_chan.getJSONObject(j).getString("channel_pic");
                                        items_cate_id_in_chan = items_array_chan.getJSONObject(j).getInt("category_id");
                                        items_chan_id = items_array_chan.getJSONObject(j).getInt("channel_id");

                                        if (items_cate_id == items_cate_id_in_chan) {

                                            ItemExpLeft ch = new ItemExpLeft();
                                            ch.setName(items_chan_title);
                                            ch.setImage(items_chan_pic);
                                            ch.setId(items_chan_id);
                                            channel_list.add(ch);
                                        }
                                    }
                                    gru.setItems(channel_list);
                                    group_list.add(gru);

                                }


                                ExpAdapter = new ExpandableListAdapter_Left(MainActivity.this, group_list);
                                EXP_exp_left.setAdapter(ExpAdapter);


                                Log.d("logrun2", "Prepare List  = "+group_list.size() + " ");

                                //ExpAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("logrun2", e.toString());
                            }
                        } else {
                            Log.d("logrun2", "Object is Null");
                        }

                    }
                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        return false;
    }
}
