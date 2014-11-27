package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;

import android.widget.Toast;


import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.ServiceConnector;
import com.sec.android.allshare.ServiceProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class MainActivity extends SherlockFragmentActivity {


    //public static Typeface TF_font;
    //public String frontPath = "fonts/RSU_BOLD.ttf";
    static String urlPath = "https://dl.dropboxusercontent.com/u/40791893/pic_android/thaitv_list_item.js";
    // static String urlPath = "https://dl.dropboxusercontent.com/s/s26bmc0ok4odpcv/thaitv_list_item.js";



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

    FragmentTransaction ft;
    ActionBarDrawerToggle actionBarDrawerToggle;

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
        wv.setBackgroundResource(R.color.BGBodyDialog);
        wv.loadUrl("file:///android_asset/loader.gif");
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView body = (TextView) view.findViewById(R.id.body);
        title.setText("กรุณารอสักครู่");
        body.setText("กำลังโหลดข้อมูล...");
        progressDialog.setCancelable(false);
        progressDialog.setContentView(view);
        progressDialog.show();

        prepareMenuLeft();
        loadToDataStore();

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
            Log.d("run","selected 0");
         //   selectItem(0);
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
        Log.d("run","onOptionsItemSelected : "+item.getItemId());
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
        Log.d("run","setTitle");
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


    public void loadToDataStore() {

        aq.progress(progressDialog).ajax(urlPath, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if (object != null) {


                    try {

                        /*
                        Log.d("logrun2", "O "+object.getJSONArray("allitems"));
                        JSONArray item_array_cate = object.getJSONArray("allitems").getJSONObject(0).getJSONArray("tb_category");
                        JSONArray item_array_chan = object.getJSONArray("allitems").getJSONObject(1).getJSONArray("tb_channel");
						JSONArray item_array_type = object.getJSONArray("allitems").getJSONObject(2).getJSONArray("tb_type");
                        JSONArray item_array_prog = object.getJSONArray("allitems").getJSONObject(3).getJSONArray("tb_program");
*/
                        JSONArray item_array_cate = object.getJSONObject("allitems").getJSONArray("tb_category");
                        JSONArray item_array_chan = object.getJSONObject("allitems").getJSONArray("tb_channel");
                        JSONArray item_array_type = object.getJSONObject("allitems").getJSONArray("tb_type");
                        JSONArray item_array_prog = object.getJSONObject("allitems").getJSONArray("tb_program");


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
                        selectItem(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("logrun2", e.toString());
                    }
                } else {
                    showAlertDialog();
                    Log.d("logrun2", "Object is Null");
                }

            }
        });
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


    private void showAlertDialog() {

        String s = "ไม่สามารถโหลดข้อมูลได้";
        AlertDialog.Builder builder = GlobalVariable.simpleDialogTemplate(context, "เกิดข้อผิดพลาด", s);
        builder.setNegativeButton("ลองอีกครั้ง",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        loadToDataStore();
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
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     //   if (resultCode==123) {
     //       ViewFavoriteList = getLayoutInflater().inflate(R.layout.activity_favorite_list, ContentFrame, false);
     ///       ContentFrame.removeAllViews();
      //      favoriteList = new FavoriteList(ViewFavoriteList);
     ///       ContentFrame.addView(ViewFavoriteList);
      //  }
    }



}


