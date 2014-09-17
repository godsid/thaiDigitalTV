package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;
import android.view.Window;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity{
    ImageView IV_ic_nav_top_left,IV_ic_nav_top_right;
    ExpandableListView EXP_exp_left,EXP_exp_right;
    DrawerLayout DL_drawer_layout;

    private DrawerLayout drawer;
    private ExpandableListView drawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ExpanableListAdapter_Left ExpAdapter;

    ArrayList<GroupExpLeft> group_list;
    ArrayList<ItemExpLeft> channel_list;

    AQuery aq;

    AlertDialog alertDialogStores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        aq = new AQuery(this);

        IV_ic_nav_top_left = (ImageView) findViewById(R.id.ic_nav_top_left);
        IV_ic_nav_top_right = (ImageView) findViewById(R.id.ic_nav_top_right);
        EXP_exp_left = (ExpandableListView) findViewById(R.id.exp_left);
        EXP_exp_right = (ExpandableListView) findViewById(R.id.exp_right);
        DL_drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);


        prepareListData();


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

    }




    public void prepareListData() {


        aq.ajax("https://dl.dropboxusercontent.com/u/40791893/pic_android/menu_items2.js",
                JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject object,AjaxStatus status) {

                        if (object != null) {

                            try {

                                JSONArray items_array_cate = object.getJSONArray("allitems").getJSONObject(0).getJSONArray("category_list");
                                JSONArray items_array_chan = object.getJSONArray("allitems").getJSONObject(1).getJSONArray("channel_list");

                                String items_cate_title,items_cate_pic,items_chan_title,items_chan_pic;
                                int items_cate_id,items_chan_id,items_cate_id_in_chan;

                                group_list = new ArrayList<GroupExpLeft>();

                                for (int i = 0; i < items_array_cate.length(); i++) {

                                    items_cate_title = items_array_cate.getJSONObject(i).getString("cate_title");
                                    items_cate_pic = items_array_cate.getJSONObject(i).getString("cate_pic");
                                    items_cate_id = items_array_cate.getJSONObject(i).getInt("cate_id");

                                     //Log.d("logrun2", items_cate_id+"  "+items_cate_title);


                                    GroupExpLeft gru = new GroupExpLeft();
                                    gru.setName(items_cate_title);
                                    gru.setImage(items_cate_pic);

                                    channel_list = new ArrayList<ItemExpLeft>();

                                    for (int j = 0; j < items_array_chan.length(); j++) {
                                        items_chan_title = items_array_chan.getJSONObject(j).getString("chan_title");
                                        items_chan_pic = items_array_chan.getJSONObject(j).getString("chan_pic");
                                        items_cate_id_in_chan = items_array_chan.getJSONObject(j).getInt("cate_id");
                                        items_chan_id = items_array_chan.getJSONObject(j).getInt("chan_id");

                                        if (items_cate_id == items_cate_id_in_chan) {

                                            ItemExpLeft ch = new ItemExpLeft();
                                            ch.setName(items_chan_title);
                                            ch.setImage(items_chan_pic);
                                            channel_list.add(ch);

                                           // Log.d("logrun2", items_chan_id+"  "+items_cate_id_in_chan + " "+items_chan_title);
                                        }
                                        //arrayListData.add(new DataCustomListView(item.getString("cover"),item.getString("title"),item.getString("mask")));


                                    }
                                    gru.setItems(channel_list);
                                    group_list.add(gru);

                                }



                                ExpAdapter = new ExpanableListAdapter_Left(MainActivity.this, group_list);
                                EXP_exp_left.setAdapter(ExpAdapter);


                                Log.d("logrun2", group_list.size()+" ");

                                ExpAdapter.notifyDataSetChanged();

                            }  catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("logrun2", e.toString());
                            }
                        } else {
                            Log.d("logrun2","Object is Null");
                        }

                        // super.callback(url, object, status);
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
