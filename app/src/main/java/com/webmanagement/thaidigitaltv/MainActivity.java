package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity{
    ImageView IV_ic_nav_top_left,IV_ic_nav_top_right;
    ExpandableListView EXP_exp_left,EXP_exp_right;
    DrawerLayout DL_drawer_layout;
    DetailProgram detailProgram = new DetailProgram();


    private ExpandableListAdapter_Left ExpAdapter;

    ArrayList<GroupExpLeft> group_list;
    ArrayList<ItemExpLeft> channel_list;

    AQuery aq;


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

        LinearLayout hiddenLayout = (LinearLayout)findViewById(R.id.ll_detail_list);
        if(hiddenLayout == null){

            FrameLayout myLayout = (FrameLayout)findViewById(R.id.content_frame);
            View hiddenInfo = getLayoutInflater().inflate(R.layout.activity_detail_list, myLayout, false);
            myLayout.addView(hiddenInfo);
        }

/*
        EXP_exp_left.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                 Toast.makeText(getApplicationContext(),
                "Group Clicked " + group_list.get(groupPosition),
                 Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        EXP_exp_left.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),group_list.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        EXP_exp_left.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),group_list.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });
*/
        // Listview on child click listener
        EXP_exp_left.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
                String get_channel_name = group_list.get(groupPosition).getItems().get(childPosition).getName();
                int get_channel_id = group_list.get(groupPosition).getItems().get(childPosition).getId();
                String  get_channel_pic = group_list.get(groupPosition).getItems().get(childPosition).getImage();

                detailProgram.setChan_id(get_channel_id);
                detailProgram.setChan_name(get_channel_name);
                detailProgram.setChan_pic(get_channel_pic);


                Toast.makeText(getApplicationContext(),get_channel_id +" "+get_channel_name + "",
                        Toast.LENGTH_SHORT).show();
                openProgramDetail();
                DL_drawer_layout.closeDrawer(EXP_exp_left);

                return false;
            }
        });
    }


    public void openProgramDetail() {
                       //Check if the Layout already exists



                TextView tv_title = (TextView) findViewById(R.id.tv_detail_list_title);
                ImageView iv_title = (ImageView) findViewById(R.id.iv_detail_list_title);

        tv_title.setText(detailProgram.getChan_name());
        aq.id(iv_title).image(detailProgram.getChan_pic());

        TableLayout ll = (TableLayout) findViewById(R.id.tb_detail_list);
        ll.removeAllViews();

        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" Sl.No ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Product ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Unit Price ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText(" Stock Remaining ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        ll.addView(tbrow0);



        for (int i = 0; i <12; i++) {

            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + i);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText("Product " + i);
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText("Rs." + i);
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            TextView t4v = new TextView(this);
            t4v.setText("" + i * 15 / 32 * 10);
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            ll.addView(tbrow);

        }


    }


    public void prepareListData() {


        aq.ajax("https://dl.dropboxusercontent.com/u/40791893/pic_android/menu_items3.js",
       // aq.ajax("https://dl.dropboxusercontent.com/s/yk30i4avjhxs6ue/exp_right.js",
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
                                            ch.setId(items_chan_id);
                                            channel_list.add(ch);

                                           // Log.d("logrun2", items_chan_id+"  "+items_cate_id_in_chan + " "+items_chan_title);
                                        }
                                        //arrayListData.add(new DataCustomListView(item.getString("cover"),item.getString("title"),item.getString("mask")));


                                    }
                                    gru.setItems(channel_list);
                                    group_list.add(gru);

                                }


                                ExpAdapter = new ExpandableListAdapter_Left(MainActivity.this, group_list);
                                EXP_exp_left.setAdapter(ExpAdapter);



                                Log.d("logrun2", group_list.size()+" ");

                                //ExpAdapter.notifyDataSetChanged();

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
