package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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

public class MainActivity extends Activity {
    ImageView IV_ic_nav_top_left, IV_ic_nav_top_right,IV_ic_fav_top_right;
    ExpandableListView EXP_exp_left, EXP_exp_right;
    DrawerLayout DL_drawer_layout;
    DetailProgram detailProgram;

    private ExpandableListAdapter_Left ExpAdapter;
    static String urlPath = "https://dl.dropboxusercontent.com/s/w7ih0hrbius82rj/menu_item3.js";
    ArrayList<GroupExpLeft> group_list;
    ArrayList<ItemExpLeft> channel_list;

    Typeface TF_font;
    String frontPath = "fonts/RSU_BOLD.ttf";

    TextView TV_header_program, TV_header_time, TV_header_status, TV_header_fav, TV_detail_list_title;

    int tv_header_tb_size = 18;
    int tv_item_tb_size = 16;



    AQuery aq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detailProgram = new DetailProgram();
        aq = new AQuery(this);

        IV_ic_nav_top_left = (ImageView) findViewById(R.id.ic_nav_top_left);
        IV_ic_nav_top_right = (ImageView) findViewById(R.id.ic_nav_top_right);
        EXP_exp_left = (ExpandableListView) findViewById(R.id.exp_left);
        EXP_exp_right = (ExpandableListView) findViewById(R.id.exp_right);
        DL_drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        IV_ic_fav_top_right = (ImageView) findViewById(R.id.ic_fav_top_right);


        TF_font = Typeface.createFromAsset(getAssets(), frontPath);

        TV_detail_list_title = (TextView) findViewById(R.id.tv_detail_list_title);
        TV_detail_list_title.setTypeface(TF_font);
        TV_detail_list_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

        prepareListData();

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

        LinearLayout hiddenLayout = (LinearLayout) findViewById(R.id.ll_detail_list);
        if (hiddenLayout == null) {

            FrameLayout myLayout = (FrameLayout) findViewById(R.id.content_frame);
            View hiddenInfo = getLayoutInflater().inflate(R.layout.activity_detail_list, myLayout, false);
            myLayout.addView(hiddenInfo);
        }

        TV_header_program = (TextView) findViewById(R.id.tv_header_program);
        TV_header_time = (TextView) findViewById(R.id.tv_header_time);
        TV_header_status = (TextView) findViewById(R.id.tv_header_status);
        TV_header_fav = (TextView) findViewById(R.id.tv_header_fav);

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


    }


    public void showSettimeList() {

        Intent intent = new Intent(MainActivity.this, SettingTimeList.class);
        startActivity(intent);
    }

    public void gotoFavoriteList() {

        Intent intent = new Intent(MainActivity.this, FavoriteList.class);
        startActivity(intent);
    }

    public void setDataToTable(int id, String c1, String c2, String c3,String c4, boolean b1,int c ,int i1) {

        TableLayout TL_detail_list = (TableLayout) findViewById(R.id.tb_detail_list);

        if (b1 == false) {
            TL_detail_list.removeAllViews();
        } else {
            Log.d("logrun2", c1.length() + "  : " + TV_header_program.getPivotY());
            int bg_tv_color, item_tv_color = Color.rgb(90, 90, 90);
            int tv_layout_height = 85;

            if ((i1 % 2) != 0)
                bg_tv_color = Color.rgb(252, 236, 232);
            else
                bg_tv_color = Color.rgb(228, 216, 205);

            final TableRow tb_row = new TableRow(this);

            TextView tv_col_1 = new TextView(this);
            tv_col_1.setWidth(TV_header_program.getWidth());

            if (c1.length() <= 15)
                tv_col_1.setText(c1 + "\n");
            else
                tv_col_1.setText(c1);

            tv_col_1.setTextColor(item_tv_color);
            tv_col_1.setGravity(Gravity.CENTER);
            tv_col_1.setBackgroundColor(bg_tv_color);
            tv_col_1.setTextSize(tv_item_tb_size);
            tv_col_1.setHeight(tv_layout_height);

            tb_row.addView(tv_col_1);

            TextView tv_col_2 = new TextView(this);
            tv_col_2.setWidth(TV_header_time.getWidth());
            tv_col_2.setText(c2 + "\n" + c3);
            tv_col_2.setTextColor(item_tv_color);
            tv_col_2.setGravity(Gravity.CENTER);
            tv_col_2.setBackgroundColor(bg_tv_color);
            tv_col_2.setTextSize(tv_item_tb_size);
            tv_col_2.setHeight(tv_layout_height);
            tb_row.addView(tv_col_2);

            TextView tv_col_3 = new TextView(this);
            tv_col_3.setWidth(TV_header_status.getWidth());
            tv_col_3.setText("\n");
            tv_col_3.setGravity(Gravity.CENTER);
            tv_col_3.setPadding(15, 0, 10, 0);
            tv_col_3.setHeight(tv_layout_height);
            tv_col_3.setBackgroundColor(bg_tv_color);
            tv_col_3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_onair_2, 0, 0, 0);
            tb_row.addView(tv_col_3);

            TextView tv_col_4 = new TextView(this);
            tv_col_4.setWidth(TV_header_fav.getWidth());
            tv_col_4.setText("\n");
            tv_col_4.setGravity(Gravity.CENTER);
            tv_col_4.setPadding(45, 0, 10, 0);
            tv_col_4.setHeight(tv_layout_height);
            tv_col_4.setBackgroundColor(bg_tv_color);
            tv_col_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_3, 0, 0, 0);

            tb_row.setId(c);

            tb_row.addView(tv_col_4);

            detailProgram.setProg_id(id);
            detailProgram.setProg_name(c1);
            detailProgram.setTime_start(c2);
            detailProgram.setType_name(c4);

            tv_col_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    detailProgram.seItem_selected(tb_row.getId());
                    showSettimeList();
                    //Toast.makeText(getApplicationContext(), "Click row at :" + tb_row.getId(), Toast.LENGTH_SHORT).show();

                }
            });
            TL_detail_list.addView(tb_row);
        }

    }







    public void openProgramDetail() {


        TextView TV_title_detail_list = (TextView) findViewById(R.id.tv_detail_list_title);
        ImageView IV_title_detail_list = (ImageView) findViewById(R.id.iv_detail_list_title);

        TV_title_detail_list.setText(detailProgram.getChan_name());

        aq.id(IV_title_detail_list).image(detailProgram.getChan_pic());

        setDataToTable(0, "", "", "","", false,0, 1);
        detailProgram.clearAllArray();
        aq.ajax(urlPath, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {

                        if (object != null) {

                            try {
                                JSONArray items_array_prog = object.getJSONArray("allitems").getJSONObject(2).getJSONArray("program_detail");

                                int c = 0;
                                for (int j = 0; j < items_array_prog.length(); j++) {

                                    int prog_id = items_array_prog.getJSONObject(j).getInt("prog_id");
                                    int chan_id = items_array_prog.getJSONObject(j).getInt("chan_id");
                                    String prog_title = items_array_prog.getJSONObject(j).getString("prog_title");
                                    String prog_timestart = items_array_prog.getJSONObject(j).getString("prog_timestart");
                                    String prog_timeend = items_array_prog.getJSONObject(j).getString("prog_timeend");
                                    String prog_type = "test type";


                                    if (detailProgram.getChan_id() == chan_id) {
                                        setDataToTable(prog_id, prog_title, prog_timestart, prog_timeend,prog_type, true, c,j);
                                        c++;
                                    }
                                }
                                Log.d("logrun2", group_list.size() + " ");

                                //ExpAdapter.notifyDataSetChanged();

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


    public void prepareListData() {
        aq.ajax(urlPath,
                JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {


                        if (object != null) {

                            try {

                                JSONArray items_array_cate = object.getJSONArray("allitems").getJSONObject(0).getJSONArray("category_list");
                                JSONArray items_array_chan = object.getJSONArray("allitems").getJSONObject(1).getJSONArray("channel_list");

                                String items_cate_title, items_cate_pic, items_chan_title, items_chan_pic;
                                int items_cate_id, items_chan_id, items_cate_id_in_chan;

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
                                        }
                                    }
                                    gru.setItems(channel_list);
                                    group_list.add(gru);

                                }


                                ExpAdapter = new ExpandableListAdapter_Left(MainActivity.this, group_list);
                                EXP_exp_left.setAdapter(ExpAdapter);


                                Log.d("logrun2", group_list.size() + " ");

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
