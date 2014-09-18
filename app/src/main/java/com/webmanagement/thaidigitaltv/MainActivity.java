package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
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
    static String urlPath = "https://dl.dropboxusercontent.com/s/w7ih0hrbius82rj/menu_item3.js";
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

        TextView TV_title_detail_list = (TextView) findViewById(R.id.tv_detail_list_title);
        TV_title_detail_list.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

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

                openProgramDetail();

                Toast.makeText(getApplicationContext(),get_channel_id +" "+get_channel_name + "",
                        Toast.LENGTH_SHORT).show();

                DL_drawer_layout.closeDrawer(EXP_exp_left);

                return false;
            }
        });
    }

    public void setDataToTable (String c1,String c2,String c3,boolean b1,int i1) {

        TableLayout TL_detail_list = (TableLayout) findViewById(R.id.tb_detail_list);

        int tv_color;
        if ((i1 % 2) != 0)
            tv_color = Color.rgb(252,236,232);
        else
            tv_color = Color.rgb(228,216,205);

        Log.d("logrun2", i1+" :"+(i1 % 1));

        TableRow tbrow = new TableRow(this);
        TextView t1v = new TextView(this);
        t1v.setText(c1);
        t1v.setTextColor(Color.BLACK);
        t1v.setGravity(Gravity.CENTER);
        t1v.setBackgroundColor(tv_color);
        tbrow.addView(t1v);

        TextView t2v = new TextView(this);
        t2v.setText(c2+" - "+c3);
        t2v.setTextColor(Color.BLACK);
        t2v.setGravity(Gravity.CENTER);
        t2v.setBackgroundColor(tv_color);
        tbrow.addView(t2v);

        TextView t3v = new TextView(this);
        t3v.setText("comming");
        t3v.setTextColor(Color.BLACK);
        t3v.setGravity(Gravity.CENTER);
        t3v.setBackgroundColor(tv_color);
        tbrow.addView(t3v);

        TextView t4v = new TextView(this);
        t4v.setText(" + ");
        t4v.setTextColor(Color.BLACK);
        t4v.setGravity(Gravity.CENTER);
        t4v.setBackgroundColor(tv_color);
        tbrow.addView(t4v);

        TL_detail_list.addView(tbrow);

        if (b1 == false)
            TL_detail_list.removeAllViews();

    }

    public void openProgramDetail() {

     TextView TV_title_detail_list = (TextView) findViewById(R.id.tv_detail_list_title);
     ImageView IV_title_detail_list = (ImageView) findViewById(R.id.iv_detail_list_title);


        TV_title_detail_list.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
        TV_title_detail_list.setText(detailProgram.getChan_name());
        aq.id(IV_title_detail_list).image(detailProgram.getChan_pic());
        //IV_title_detail_list.setScaleY(1.5f);
        //IV_title_detail_list.setScaleX(1.5f);

        setDataToTable ("","","",false,1);

        aq.ajax(urlPath,
                //aq.ajax("https://dl.dropboxusercontent.com/s/k9nxs7cb16luqe3/exp_android2.js",
                JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object,AjaxStatus status) {

                        if (object != null) {

                            try {
                                JSONArray items_array_prog = object.getJSONArray("allitems").getJSONObject(2).getJSONArray("program_detail");


                                    for (int j = 0; j < items_array_prog.length(); j++) {

                                       int  prog_id = items_array_prog.getJSONObject(j).getInt("prog_id");
                                       int  chan_id = items_array_prog.getJSONObject(j).getInt("chan_id");
                                       String  prog_title = items_array_prog.getJSONObject(j).getString("prog_title");
                                       String  prog_timestart = items_array_prog.getJSONObject(j).getString("prog_timestart");
                                       String  prog_timeend = items_array_prog.getJSONObject(j).getString("prog_timeend");


                                        if (detailProgram.getChan_id() == chan_id) {
                                            setDataToTable (prog_title,prog_timestart,prog_timeend,true,j);
                                            // Log.d("logrun2", items_chan_id+"  "+items_cate_id_in_chan + " "+items_chan_title);
                                        }
                                        //arrayListData.add(new DataCustomListView(item.getString("cover"),item.getString("title"),item.getString("mask")));


                                    }
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





    public void prepareListData() {


        aq.ajax(urlPath,
                //aq.ajax("https://dl.dropboxusercontent.com/s/k9nxs7cb16luqe3/exp_android2.js",
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
