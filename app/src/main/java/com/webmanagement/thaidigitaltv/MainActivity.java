package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    //////////////////// Global variable ////////////
    private DetailProgram detailProgram;
    private DatabaseAction dbAction;
    public static Typeface TF_font;
    public String frontPath = "fonts/RSU_BOLD.ttf";
    static String urlPath = "https://dl.dropboxusercontent.com/u/40791893/pic_android/item4.js";

    ImageView IV_ic_nav_top_left, IV_ic_nav_top_right, IV_ic_fav_top_right, IV_detail_today, IV_detail_list_title;

    DrawerLayout DL_drawer_layout;

    FrameLayout ContentFrame;
    View ViewFavoriteList, ViewMainMenu, ViewProgramDetail;

    //static String urlPath = "https://dl.dropboxusercontent.com/s/w7ih0hrbius82rj/menu_item3.js";

    public static ArrayList<DataStore_Category> arrDataStore_category = new ArrayList<DataStore_Category>();
    public static ArrayList<DataStore_Channel> arrDataStore_channel = new ArrayList<DataStore_Channel>();
    public static ArrayList<DataStore_Program> arrDataStore_program = new ArrayList<DataStore_Program>();
    public static ArrayList<DataStore_Type> arrDataStore_type = new ArrayList<DataStore_Type>();





    ArrayList<DataCustomMenuLeft> dataCustomMenuLeft = new ArrayList<DataCustomMenuLeft>();
    MenuLeftAdapter menuLeftAdapter;


    ListView LV_menu_left;

    private static boolean stateOK = false;





    TextView TV_header_program, TV_header_time, TV_header_status, TV_header_fav, TV_detail_list_title;

    private SeekBar SB_detail_date = null;
    int tv_header_tb_size = 18;
    int tv_item_tb_size = 16;

    TextView TV_detail_day, TV_detail_date, TV_detail_month, TV_detail_year;
    Calendar calendar;
    Date date;
    String[] arr_day = new String[]{"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};
    String[] arr_month = new String[]{"มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม",
            "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};
    int g_current_day, g_current_date, g_current_month, g_current_year;
    int g_change_day;
    ProgressDialog progressDialog;



    AQuery aq;


    LinearLayout llFavoriteList;
    LinearLayout llMainMenu;
    FavoriteList favoriteList;
    MainMenuTab mainMenuTab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        detailProgram = new DetailProgram();
        dbAction = new DatabaseAction(this);
        aq = new AQuery(this);
        TF_font = Typeface.createFromAsset(getAssets(), frontPath);

        ContentFrame = (FrameLayout) findViewById(R.id.content_frame);
        llMainMenu = (LinearLayout) findViewById(R.id.ll_main_menu);
        llFavoriteList = (LinearLayout) findViewById(R.id.ll_favorite_list);






        LV_menu_left = (ListView) findViewById(R.id.lv_menu_left);

        IV_ic_nav_top_left = (ImageView) findViewById(R.id.ic_nav_top_left);

        DL_drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("กำลังโหลดข้อมูล...");
        progressDialog.setTitle("กรุณารอสักครู่");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        prepareMenuLeft();
        loadToDataStore();


        IV_ic_nav_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DL_drawer_layout.isDrawerOpen(LV_menu_left)) {
                    DL_drawer_layout.closeDrawer(LV_menu_left);
                } else {
                    DL_drawer_layout.openDrawer(LV_menu_left);
                }

            }
        });

        LV_menu_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {


                    ViewMainMenu = getLayoutInflater().inflate(R.layout.activity_main_menu, ContentFrame, false);
                    ContentFrame.removeAllViews();
                    mainMenuTab = new MainMenuTab(ViewMainMenu);
                    ContentFrame.addView(ViewMainMenu);


                } else if (position == 1) {

                    ViewFavoriteList = getLayoutInflater().inflate(R.layout.activity_favorite_list, ContentFrame, false);
                    ContentFrame.removeAllViews();
                    favoriteList = new FavoriteList(ViewFavoriteList);
                    ContentFrame.addView(ViewFavoriteList);
                }


                DL_drawer_layout.closeDrawer(LV_menu_left);
            }
        });



    }


    public void prepareMenuLeft() {
        int[] g_pic = new int[]{R.drawable.toggle1, R.drawable.toggle2};
        String[] g_title = new String[]{"ช่องรายการ", "รายการโปรด"};

        for (int i = 0; i < g_pic.length; i++) {

            dataCustomMenuLeft.add(new DataCustomMenuLeft(g_title[i], g_pic[i]));

        }
        menuLeftAdapter = new MenuLeftAdapter(getApplicationContext(), dataCustomMenuLeft);
        LV_menu_left.setAdapter(menuLeftAdapter);
        menuLeftAdapter.notifyDataSetChanged();

    }






    public void loadToDataStore() {

        aq.progress(progressDialog).ajax(urlPath, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if (object != null) {

                    try {

                        JSONArray item_array_cate = object.getJSONArray("allitems").getJSONObject(0).getJSONArray("tb_category");
                        JSONArray item_array_chan = object.getJSONArray("allitems").getJSONObject(1).getJSONArray("tb_channel");
                        JSONArray item_array_prog = object.getJSONArray("allitems").getJSONObject(3).getJSONArray("tb_program");
                        JSONArray item_array_type = object.getJSONArray("allitems").getJSONObject(4).getJSONArray("tb_type");


                        for (int j = 0; j < item_array_cate.length(); j++) {
                            DataStore_Category dataStore_category = new DataStore_Category();
                            dataStore_category.setCate_id(item_array_cate.getJSONObject(j).getInt("category_id"));
                            dataStore_category.setCate_title(item_array_cate.getJSONObject(j).getString("category_name"));
                            dataStore_category.setCate_pic(item_array_cate.getJSONObject(j).getString("category_pic"));
                            arrDataStore_category.add(dataStore_category);

                        }
                        //Log.d("run", "dataStore_category  " + arrDataStore_category.size());

                        for (int i = 0; i < item_array_chan.length(); i++) {

                            DataStore_Channel dataStore_channel = new DataStore_Channel();
                            dataStore_channel.setChan_id(item_array_chan.getJSONObject(i).getInt("channel_id"));
                            dataStore_channel.setChan_name(item_array_chan.getJSONObject(i).getString("channel_name"));
                            dataStore_channel.setChan_pic(item_array_chan.getJSONObject(i).getString("channel_pic"));
                            dataStore_channel.setFr_cate_id(item_array_chan.getJSONObject(i).getInt("category_id"));
                            arrDataStore_channel.add(dataStore_channel);
                        }
                        //Log.d("run", "dataStore_channel  " + arrDataStore_channel.size());

                        for (int i = 0; i < item_array_type.length(); i++) {
                            DataStore_Type dataStore_type = new DataStore_Type();
                            dataStore_type.setType_id(item_array_type.getJSONObject(i).getInt("type_id"));
                            dataStore_type.setType_name(item_array_type.getJSONObject(i).getString("type_name"));
                            arrDataStore_type.add(dataStore_type);
                        }

                        for (int j = 0; j < item_array_prog.length(); j++) {

                            DataStore_Program dataStore_program = new DataStore_Program();
                            dataStore_program.setProg_id(item_array_prog.getJSONObject(j).getInt("prog_id"));
                            dataStore_program.setProg_name(item_array_prog.getJSONObject(j).getString("prog_name"));
                            dataStore_program.setProg_timeend(item_array_prog.getJSONObject(j).getString("time_end").replace(".", ":"));
                            dataStore_program.setProg_timestart(item_array_prog.getJSONObject(j).getString("time_start").replace(".", ":"));
                            dataStore_program.setFr_channel_id(item_array_prog.getJSONObject(j).getInt("channel_id"));
                            dataStore_program.setFr_day_id(item_array_prog.getJSONObject(j).getInt("day_id"));
                            dataStore_program.setFr_type_id(item_array_prog.getJSONObject(j).getInt("type_id"));
                            arrDataStore_program.add(dataStore_program);

                        }

                        ViewMainMenu = getLayoutInflater().inflate(R.layout.activity_main_menu, ContentFrame, false);
                        ContentFrame.removeAllViews();
                        mainMenuTab = new MainMenuTab(ViewMainMenu);
                        ContentFrame.addView(ViewMainMenu);


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

    @Override
    protected void onResume() {
        //if (stateOK)
        //setEexpLeftChildSelected();
        Log.d("run", "onResume");
        super.onResume();
    }


    @Override
    protected void onPause() {
        Log.d("run", "onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d("run", "onDestroy");
        super.onDestroy();
    }
}
