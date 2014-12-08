package com.webmanagement.thaidigitaltv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import android.widget.TextView;

import android.widget.Toast;


import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.ServiceConnector;
import com.sec.android.allshare.ServiceProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends SherlockFragmentActivity {


    //public static Typeface TF_font;
    //public String frontPath = "fonts/RSU_BOLD.ttf";
    static String urlData = "https://dl.dropboxusercontent.com/s/rntbhs8x2kykwc0/thaitv_list_item.js";
    static String urlVersion = "https://dl.dropboxusercontent.com/s/l2fyzgiixrtw1yx/thaitv_data_version.js";
    private DatabaseAction dbAction;

    DrawerLayout DL_drawer_layout;

    FrameLayout ContentFrame;

    public static ArrayList<DataStore_Category> arrDataStore_category = new ArrayList<DataStore_Category>();
    public static ArrayList<DataStore_Channel> arrDataStore_channel = new ArrayList<DataStore_Channel>();
    public static ArrayList<DataStore_Program> arrDataStore_program = new ArrayList<DataStore_Program>();
    public static ArrayList<DataStore_Type> arrDataStore_type = new ArrayList<DataStore_Type>();

    ArrayList<DataCustomMenuLeft> dataCustomMenuLeft = new ArrayList<DataCustomMenuLeft>();
    MenuLeftAdapter menuLeftAdapter;

    ListView LV_menu_left;

    int[] g_pic = new int[]{R.drawable.ic_channel_tv, R.drawable.ic_favorite_flase};
    String[] g_title = new String[]{"ช่องรายการ", "รายการโปรด"};
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Dialog progressDialog;

    AQuery aq;

    Context context;
    Tracker t;
    TextView body;
    FragmentTransaction ft;
    ActionBarDrawerToggle actionBarDrawerToggle;

    public static File dirImage;
    private static final String dirImageName = ".thaidigitaltv";
    private int versionFromUrl = 0;
    private int versionFromLocal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get a Tracker (should auto-report)
        //   t = ((MyApplication)getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
        //    t.setScreenName("Main Activity");
        //    t.send(new HitBuilders.AppViewBuilder().build());

        context = MainActivity.this;
        aq = new AQuery(this);


        dbAction = new DatabaseAction(context);

        ERROR err = ServiceConnector.createServiceProvider(this, new ServiceConnector.IServiceConnectEventListener() {

            @Override
            public void onCreated(ServiceProvider serviceProvider, ServiceConnector.ServiceState serviceState) {
                if (serviceProvider == null)
                    return;
                GlobalVariable.setServiceProvider(serviceProvider);
                Log.d("run", "Service provider created! " + GlobalVariable.getServiceProvider());
            }

            @Override
            public void onDeleted(ServiceProvider serviceProvider) {
                GlobalVariable.setServiceProvider(null);
                Log.d("run", "Service provider Deleted! " + GlobalVariable.getServiceProvider());
            }
        });


        if (err == ERROR.FRAMEWORK_NOT_INSTALLED) {
            // AllShare Framework Service is not installed.
            Log.d("run", "AllShare Framework Service is not installed.");
        } else if (err == ERROR.INVALID_ARGUMENT) {
            // Input argument is invalid. Check and try again
            Log.d("run", "Input argument is invalid. Check and try again.");
        } else {
            // Success on calling the function.
            Log.d("run", "Success on calling the function.");
        }

        //TF_font = Typeface.createFromAsset(getAssets(), frontPath);
        //TV_detail_list_title.setTypeface(TF_font);
        //TV_detail_list_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        mTitle = mDrawerTitle = getTitle();

        ContentFrame = (FrameLayout) findViewById(R.id.content_frame);

        LV_menu_left = (ListView) findViewById(R.id.lv_menu_left);

        DL_drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        DL_drawer_layout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(R.layout.dialog_template, null);
        WebView wv = (WebView) view.findViewById(R.id.loader);
        wv.setVisibility(View.VISIBLE);
        //wv.setBackgroundResource(R.color.BGBodyDialog);
        wv.setBackgroundColor(getResources().getColor(R.color.BG_List));
        wv.loadUrl("file:///android_asset/loader.gif");
        TextView title = (TextView) view.findViewById(R.id.title);
        body = (TextView) view.findViewById(R.id.body);
        title.setText("กรุณารอสักครู่");

        progressDialog.setCancelable(false);
        progressDialog.setContentView(view);
        progressDialog.show();

        createImgDir();
        prepareMenuLeft();
        loadData();

        LV_menu_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });


        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                DL_drawer_layout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                super.onDrawerOpened(drawerView);
            }
        };

        DL_drawer_layout.setDrawerListener(actionBarDrawerToggle);

        if (savedInstanceState == null) {
            Log.d("run", "selected 0");
        }

    }


    private void selectItem(int position) {

        ft = getSupportFragmentManager().beginTransaction();

        switch (position) {
            case 0:
                ft.replace(R.id.content_frame, new MainPager());
                break;
            case 1:
                ft.replace(R.id.content_frame, new FavoriteList());
                break;
        }
        ft.commit();

        LV_menu_left.setItemChecked(position, true);
        setTitle(g_title[position]);
        DL_drawer_layout.closeDrawer(LV_menu_left);
    }


    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        Log.d("run", "onOptionsItemSelected : " + item.getItemId());
        if (item.getItemId() == android.R.id.home) {

            if (DL_drawer_layout.isDrawerOpen(LV_menu_left)) {
                DL_drawer_layout.closeDrawer(LV_menu_left);
            } else {
                DL_drawer_layout.openDrawer(LV_menu_left);
            }
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setTitle(CharSequence title) {
        //Log.d("run", "setTitle");
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }





    public void prepareMenuLeft() {


        for (int i = 0; i < g_pic.length; i++) {

            dataCustomMenuLeft.add(new DataCustomMenuLeft(g_title[i], g_pic[i]));

        }
        menuLeftAdapter = new MenuLeftAdapter(getApplicationContext(), dataCustomMenuLeft);
        LV_menu_left.setAdapter(menuLeftAdapter);
        menuLeftAdapter.notifyDataSetChanged();

    }

/*
    public void loadToDataStore() {

        aq.progress(progressDialog).ajax(urlData, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if (object != null) {

                    try {

                    //    Log.d("logrun2", "O "+object.getJSONArray("allitems"));
                      //  JSONArray item_array_cate = object.getJSONArray("allitems").getJSONObject(0).getJSONArray("tb_category");

                        JSONArray item_array_cate = object.getJSONObject("allitems").getJSONArray("tb_category");
                        JSONArray item_array_chan = object.getJSONObject("allitems").getJSONArray("tb_channel");
                        JSONArray item_array_type = object.getJSONObject("allitems").getJSONArray("tb_type");
                        JSONArray item_array_prog = object.getJSONObject("allitems").getJSONArray("tb_program");


                        for (int j = 0; j < item_array_cate.length(); j++) {
                            DataStore_Category dataStore_category = new DataStore_Category();
                            dataStore_category.setCate_id(item_array_cate.getJSONObject(j).getInt("category_id"));
                            dataStore_category.setCate_name(item_array_cate.getJSONObject(j).getString("category_name").replaceAll("\\+$", ""));
                            dataStore_category.setCate_pic(item_array_cate.getJSONObject(j).getString("category_pic").replaceAll("\\s", ""));
                            arrDataStore_category.add(dataStore_category);

                        }
                        //Log.d("run", "dataStore_category  " + arrDataStore_category.size());

                        for (int i = 0; i < item_array_chan.length(); i++) {

                            DataStore_Channel dataStore_channel = new DataStore_Channel();
                            dataStore_channel.setChan_id(item_array_chan.getJSONObject(i).getInt("channel_id"));
                            dataStore_channel.setChan_name(item_array_chan.getJSONObject(i).getString("channel_name").replaceAll("\\+$", ""));
                            dataStore_channel.setChan_pic(item_array_chan.getJSONObject(i).getString("channel_pic"));
                            dataStore_channel.setFr_cate_id(item_array_chan.getJSONObject(i).getInt("category_id"));
                            arrDataStore_channel.add(dataStore_channel);

                            SaveImage(item_array_chan.getJSONObject(i).getString("channel_pic"),item_array_chan.getJSONObject(i).getInt("channel_id"));

                        }
                        //Log.d("run", "dataStore_channel  " + arrDataStore_channel.size());

                        for (int i = 0; i < item_array_type.length(); i++) {
                            DataStore_Type dataStore_type = new DataStore_Type();
                            dataStore_type.setType_id(item_array_type.getJSONObject(i).getInt("type_id"));
                            dataStore_type.setType_name(item_array_type.getJSONObject(i).getString("type_name").replaceAll("\\+$", ""));
                            arrDataStore_type.add(dataStore_type);
                        }

                        for (int j = 0; j < item_array_prog.length(); j++) {

                            DataStore_Program dataStore_program = new DataStore_Program();
                            dataStore_program.setProg_id(item_array_prog.getJSONObject(j).getInt("prog_id"));
                            dataStore_program.setProg_name(item_array_prog.getJSONObject(j).getString("prog_name").replaceAll("\\+$", ""));
                            dataStore_program.setProg_timeend(item_array_prog.getJSONObject(j).getString("time_end").replace(".", ":").replaceAll("\\s", ""));
                            dataStore_program.setProg_timestart(item_array_prog.getJSONObject(j).getString("time_start").replace(".", ":").replaceAll("\\s", ""));
                            dataStore_program.setFr_channel_id(item_array_prog.getJSONObject(j).getInt("channel_id"));
                            dataStore_program.setFr_day_id(item_array_prog.getJSONObject(j).getInt("day_id"));
                            dataStore_program.setFr_type_id(item_array_prog.getJSONObject(j).getInt("type_id"));
                            arrDataStore_program.add(dataStore_program);

                        }
                        selectItem(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("logrun2", e.toString());
                    }
                } else {
                    showAlertDialog("ไม่สามารถโหลดข้อมูลได้");
                    Log.d("logrun2", "Object is Null");
                }

            }
        });
    }

    */

    private void loadData() {
        getVersionFromUrl();
        selectItem(1);
        selectItem(0);
    }

    private void loadDataFromLocal() {
        Log.d("run", "loadDataFromLocal");

        loadCategory();
        loadChannel();
        loadProgram();
        loadType();
        selectItem(1);
        selectItem(0);
    }

    private void loadCategory() {
        SQLiteCursor cur = (SQLiteCursor) dbAction.readCategory();

        while (!cur.isAfterLast()) {

            int category_id = cur.getInt(0);
            String category_name = cur.getString(1);

            DataStore_Category dataStore_category = new DataStore_Category();
            dataStore_category.setCate_id(category_id);
            dataStore_category.setCate_name(category_name);
            //dataStore_category.setCate_pic(item_array_cate.getJSONObject(j).getString("category_pic").replaceAll("\\s", ""));
            arrDataStore_category.add(dataStore_category);

            cur.moveToNext();
        }
        cur.close();
        Log.d("run","loadCategory");
    }

    private void loadChannel() {
        SQLiteCursor cur = (SQLiteCursor) dbAction.readChannel();

        while (!cur.isAfterLast()) {

            int channel_id = cur.getInt(0);
            String channel_name = cur.getString(1);
            String channel_pic = cur.getString(2);
            int category_id = cur.getInt(3);

            DataStore_Channel dataStore_channel = new DataStore_Channel();
            dataStore_channel.setChan_id(channel_id);
            dataStore_channel.setChan_name(channel_name);
            dataStore_channel.setChan_pic(channel_pic);
            dataStore_channel.setFr_cate_id(category_id);
            arrDataStore_channel.add(dataStore_channel);
            cur.moveToNext();
        }
        cur.close();
        Log.d("run","loadChannel");
    }

    private void loadProgram() {
        SQLiteCursor cur = (SQLiteCursor) dbAction.readProgram();

        while (!cur.isAfterLast()) {

            int prog_id = cur.getInt(0);
            String prog_name = cur.getString(1);
            String time_start = cur.getString(2);
            String time_end = cur.getString(3);
            int day_id = cur.getInt(4);
            int channel_id = cur.getInt(5);
            int type_id = cur.getInt(6);

            DataStore_Program dataStore_program = new DataStore_Program();
            dataStore_program.setProg_id(prog_id);
            dataStore_program.setProg_name(prog_name);
            dataStore_program.setProg_timestart(time_start);
            dataStore_program.setProg_timeend(time_end);
            dataStore_program.setFr_day_id(day_id);
            dataStore_program.setFr_channel_id(channel_id);
            dataStore_program.setFr_type_id(type_id);
            arrDataStore_program.add(dataStore_program);

            cur.moveToNext();
        }
        cur.close();
        Log.d("run","loadProgram");
    }

    private void loadType() {
        SQLiteCursor cur = (SQLiteCursor) dbAction.readType();

        while (!cur.isAfterLast()) {

            int type_id = cur.getInt(0);
            String type_name = cur.getString(1);

            DataStore_Type dataStore_type = new DataStore_Type();
            dataStore_type.setType_id(type_id);
            dataStore_type.setType_name(type_name);
            arrDataStore_type.add(dataStore_type);
            cur.moveToNext();
        }
        cur.close();
        Log.d("run","loadType");
    }

    private void loadDataFromUrl() {
        body.setText("กำลังโหลดข้อมูล...");
        aq.progress(progressDialog).ajax(urlData, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if (object != null) {

                    try {
                        /*
                        Log.d("logrun2", "O "+object.getJSONArray("allitems"));
                        JSONArray item_array_cate = object.getJSONArray("allitems").getJSONObject(0).getJSONArray("tb_category");
*/
                        JSONArray item_array_cate = object.getJSONObject("allitems").getJSONArray("tb_category");
                        JSONArray item_array_chan = object.getJSONObject("allitems").getJSONArray("tb_channel");
                        JSONArray item_array_type = object.getJSONObject("allitems").getJSONArray("tb_type");
                        JSONArray item_array_prog = object.getJSONObject("allitems").getJSONArray("tb_program");


                        for (int j = 0; j < item_array_cate.length(); j++) {
                            DataStore_Category dataStore_category = new DataStore_Category();
                            int a = item_array_cate.getJSONObject(j).getInt("category_id");
                            String b = item_array_cate.getJSONObject(j).getString("category_name").replaceAll("\\+$", "");
                            dataStore_category.setCate_id(a);
                            dataStore_category.setCate_name(b);
                            arrDataStore_category.add(dataStore_category);

                        }
                        //Log.d("run", "dataStore_category  " + arrDataStore_category.size());

                        for (int i = 0; i < item_array_chan.length(); i++) {

                            DataStore_Channel dataStore_channel = new DataStore_Channel();
                            int a = item_array_chan.getJSONObject(i).getInt("channel_id");
                            String b = item_array_chan.getJSONObject(i).getString("channel_name").replaceAll("\\+$", "");
                            String c = item_array_chan.getJSONObject(i).getString("channel_pic");
                            int d = item_array_chan.getJSONObject(i).getInt("category_id");
                            dataStore_channel.setChan_id(a);
                            dataStore_channel.setChan_name(b);
                            dataStore_channel.setChan_pic(c);
                            dataStore_channel.setFr_cate_id(d);
                            arrDataStore_channel.add(dataStore_channel);
                        }
                        //Log.d("run", "dataStore_channel  " + arrDataStore_channel.size());

                        for (int i = 0; i < item_array_type.length(); i++) {
                            DataStore_Type dataStore_type = new DataStore_Type();
                            int a = item_array_type.getJSONObject(i).getInt("type_id");
                            String b = item_array_type.getJSONObject(i).getString("type_name").replaceAll("\\+$", "");
                            dataStore_type.setType_id(a);
                            dataStore_type.setType_name(b);
                            arrDataStore_type.add(dataStore_type);
                        }

                        for (int j = 0; j < item_array_prog.length(); j++) {

                            DataStore_Program dataStore_program = new DataStore_Program();
                            int a = item_array_prog.getJSONObject(j).getInt("prog_id");
                            String b = item_array_prog.getJSONObject(j).getString("prog_name").replaceAll("\\+$", "");
                            String c = item_array_prog.getJSONObject(j).getString("time_start").replace(".", ":").replaceAll("\\s", "");
                            String d = item_array_prog.getJSONObject(j).getString("time_end").replace(".", ":").replaceAll("\\s", "");
                            int e = item_array_prog.getJSONObject(j).getInt("day_id");
                            int f = item_array_prog.getJSONObject(j).getInt("channel_id");
                            int g = item_array_prog.getJSONObject(j).getInt("type_id");

                            dataStore_program.setProg_id(a);
                            dataStore_program.setProg_name(b);
                            dataStore_program.setProg_timestart(c);
                            dataStore_program.setProg_timeend(d);
                            dataStore_program.setFr_day_id(e);
                            dataStore_program.setFr_channel_id(f);
                            dataStore_program.setFr_type_id(g);
                            arrDataStore_program.add(dataStore_program);

                        }
                        selectItem(0);
                        new SaveNewVersionToDB().execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("logrun2", e.toString());
                    }
                } else {
                    showAlertDialog("ไม่สามารถโหลดข้อมูลได้");
                    Log.d("logrun2", "Object is Null");
                }

            }
        });

        Log.d("run", "loadDataFromUrl");
    }

    private void createImgDir() {
    String sdCardDirectory = Environment.getExternalStorageDirectory().toString();
    dirImage = new File(sdCardDirectory + "/"+dirImageName);
    if (!dirImage.exists()) {
        dirImage.mkdirs();
    }
}
    private void SaveImage(String u, int n) {

        File image = new File(dirImage, Integer.toString(n) + ".png");
        try {
            aq.download(u, image, new AjaxCallback<File>() {
                public void callback(String url, File file, AjaxStatus status) {

                    if (file != null) {
                        Log.d("run", "Save : " + dirImage.toString());
                    } else {
                        Log.d("run", "Fail : " + dirImage.toString());
                    }
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getVersionFromUrl() {
        body.setText("กำลังตรวจสอบเวอร์ชั่น...");
        aq.progress(progressDialog).ajax(urlVersion, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                if (object != null) {

                    try {
                        versionFromUrl = object.getInt("version_number");
                        Log.d("run", "Ver From URL : " + versionFromUrl);
                        getVersionFromLocal();
                        if (versionFromUrl > versionFromLocal) {
                            dbAction.deleteAllData();
                            dbAction.updateVersion(versionFromUrl);
                            loadDataFromUrl();
                        } else {
                            loadDataFromLocal();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("logrun2", e.toString());
                    }
                } else {
                    showAlertDialog("ไม่สามารถโหลดเลขเวอร์ชั่นได้");
                    Log.d("logrun2", "Object is Null");
                }

            }
        });
    }


    private void getVersionFromLocal() {
        SQLiteCursor cur = (SQLiteCursor) dbAction.readVersion();
        if(cur.getCount() == 0) {
                dbAction.addVersion(0);
                versionFromLocal = 0;
        } else {
            versionFromLocal = cur.getInt(0);
        }
        Log.d("run", "Ver From Local : " + versionFromLocal);
        cur.close();
    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();

        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
            else Toast.makeText(getBaseContext(), "กดอีกครั้งเพื่อออก", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }


    }


    private void showAlertDialog(String s) {

        AlertDialog.Builder builder = GlobalVariable.simpleDialogTemplate(context, "เกิดข้อผิดพลาด", s);
        builder.setNegativeButton("ลองอีกครั้ง",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        loadData();
                    }
                });
        builder.setPositiveButton("ออก",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        builder.show();

    }





    private class SaveNewVersionToDB extends AsyncTask<Void, Void, Void> {
        private SaveNewVersionToDB() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Simulates a background task

            try {
                Log.d("run","SaveNewVersionToDB");
                for (int j = 0; j < arrDataStore_category.size(); j++) {
                    int a = arrDataStore_category.get(j).getCate_id();
                    String b = arrDataStore_category.get(j).getCate_name();
                    dbAction.addCategory(a,b);
                }

                for (int i = 0; i < arrDataStore_channel.size(); i++) {
                    int a = arrDataStore_channel.get(i).getChan_id();
                    String b = arrDataStore_channel.get(i).getChan_name();
                    String c = arrDataStore_channel.get(i).getChan_pic();
                    int d = arrDataStore_channel.get(i).getFr_cate_id();
                    dbAction.addChannel(a,b,dirImage.toString()+"/"+Integer.toString(a) + ".png",d);
                    SaveImage(c,a);
                }

                for (int i = 0; i < arrDataStore_type.size(); i++) {
                    int a = arrDataStore_type.get(i).getType_id();
                    String b = arrDataStore_type.get(i).getType_name();
                    dbAction.addType(a,b);
                }

                for (int j = 0; j < arrDataStore_program.size(); j++) {

                    int a = arrDataStore_program.get(j).getProg_id();
                    String b = arrDataStore_program.get(j).getProg_name();
                    String c = arrDataStore_program.get(j).getProg_timestart();
                    String d = arrDataStore_program.get(j).getProg_timeend();
                    int e = arrDataStore_program.get(j).getFr_day_id();
                    int f = arrDataStore_program.get(j).getFr_channel_id();
                    int g = arrDataStore_program.get(j).getFr_type_id();
                    dbAction.addProgram(a,b,c,d,e,f,g);

                }

            }
            catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            arrDataStore_channel.clear();
            loadChannel();

        }
    }



    @Override
    protected void onResume() {
        //if (stateOK)
        //setEexpLeftChildSelected();
        Log.d("run", "MainActivity : onResume");
        super.onResume();
    }


    @Override
    protected void onPause() {
        Log.d("run", "MainActivity : onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (GlobalVariable.getServiceProvider() != null && isFinishing() == true)
            GlobalVariable.setServiceProvider(null);
        BitmapCache.getBitmapCache().clear();

        arrDataStore_category.clear();
        arrDataStore_channel.clear();
        arrDataStore_program.clear();
        arrDataStore_type.clear();

        Log.d("run", "MainActivity : onDestroy");
        GoogleAnalytics.getInstance(this).reportActivityStop(this);

        if (isTaskRoot()) {
            AQUtility.cleanCacheAsync(this);
        }

        super.onDestroy();
    }

    @Override
    protected void onStart() {
       //
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        super.onStart();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 123) {
            selectItem(0);
            selectItem(1);
            Log.d("run", "onActivityResult " + resultCode);
        }
    }

    @Override
    public void onLowMemory() {
        BitmapAjaxCallback.clearCache();
    }


}





