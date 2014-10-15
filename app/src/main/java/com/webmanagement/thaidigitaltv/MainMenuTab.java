package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 8/10/2557.
 */
public class MainMenuTab {
    View rootView;
    Context context;
    TabHost mTabHost;
    ExpandableListView EXP_tab_2, EXP_tab_3;
    FrameLayout ContentFrame;
    View  ViewProgramDetail;
    LayoutInflater inflater;
    ListView LV_tab_1;
    Activity activity;

    AQuery aq;

    DetailProgram detailProgram;
    ListProgram listProgram;
    private int exp_left_group_pos, exp_left_child_pos;

    ListTab1Adapter listTab1Adapter;
    ArrayList<DataCustomListTab1> dataCustomListTab1 = new ArrayList<DataCustomListTab1>();

    ListTab2Adapter listTab2Adapter;
    ArrayList<ItemGroupTab2> group_list_tab2;
    ArrayList<ItemChildTab2> child_list_tab2;

    ListTab3Adapter listTab3Adapter;
    ArrayList<ItemGroupTab3> group_list_tab3;
    ArrayList<ItemChildTab3> child_list_tab3;

    ArrayList<DataStore_Category> arrDataStore_category = MainActivity.arrDataStore_category;
    ArrayList<DataStore_Channel> arrDataStore_channel = MainActivity.arrDataStore_channel;
    ArrayList<DataStore_Program> arrDataStore_program = MainActivity.arrDataStore_program;
    ArrayList<DataStore_Type> arrDataStore_type = MainActivity.arrDataStore_type;


    public MainMenuTab(View view) {

        this.rootView = view;
        this.context = rootView.getContext();

        detailProgram = new DetailProgram();

        mTabHost = (TabHost) rootView.findViewById(R.id.tabHost);
        mTabHost.setup();
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("ช่องทีวีดิจิตอล").setContent(R.id.ll_tab_1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("หมวดหมู่").setContent(R.id.ll_tab_2));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("ประเภทรายการ").setContent(R.id.ll_tab_3));
        mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_selector);
        mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_selector);
        mTabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tab_selector);
        //mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#455a64"));
        for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
        }

        mTabHost.setCurrentTab(0);

        LV_tab_1 = (ListView) rootView.findViewById(R.id.lv_tab_1);
        EXP_tab_2 = (ExpandableListView) rootView.findViewById(R.id.exp_tab_2);
        EXP_tab_3 = (ExpandableListView) rootView.findViewById(R.id.exp_tab_3);

        this.activity = (Activity)context;
        ContentFrame = (FrameLayout) activity.findViewById(R.id.content_frame);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );

        LoadMenuToTab();


        LV_tab_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String get_channel_name =  arrDataStore_channel.get(position).getChan_name();
                int get_channel_id = arrDataStore_channel.get(position).getChan_id();
                String get_channel_pic = arrDataStore_channel.get(position).getChan_pic();

                detailProgram.setChan_id(get_channel_id);
                detailProgram.setChan_name(get_channel_name);
                detailProgram.setChan_pic(get_channel_pic);

                openDetailProgram();
            }
        });

        EXP_tab_2.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String get_channel_name = group_list_tab2.get(groupPosition).getItems().get(childPosition).getName();
                int get_channel_id = group_list_tab2.get(groupPosition).getItems().get(childPosition).getId();
                String get_channel_pic = group_list_tab2.get(groupPosition).getItems().get(childPosition).getImage();
                //get position highlight
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);

                exp_left_group_pos = groupPosition;
                exp_left_child_pos = childPosition;

                detailProgram.setChan_id(get_channel_id);
                detailProgram.setChan_name(get_channel_name);
                detailProgram.setChan_pic(get_channel_pic);

                openDetailProgram();
                return false;
            }
        });




    } //End of Constructor


    private void LoadMenuToTab() {

        try {

            String item_cate_title, item_cate_pic;
            int item_cate_id;

            String item_chan_title, item_chan_pic;
            int item_chan_id;

            String item_type_name;
            int item_type_id;

           // int item_prog_id;
            String item_prog_name;

            int item_cate_id_in_chan, item_type_id_in_prog, item_chan_id_in_prog;

            ArrayList<String> temp_pro_name = new ArrayList<String>();
            temp_pro_name.clear();

            //==================//
            //Set Data To Tab 1//
            //==================//
            for (int j = 0; j < arrDataStore_channel.size(); j++) {

                item_chan_title = arrDataStore_channel.get(j).getChan_name();
                item_chan_pic = arrDataStore_channel.get(j).getChan_pic();
                item_chan_id = arrDataStore_channel.get(j).getChan_id();

                dataCustomListTab1.add(new DataCustomListTab1(item_chan_id, item_chan_title, item_chan_pic));

            }
           // Log.d("run","arrDataStore_channel : "+arrDataStore_channel.size());
            listTab1Adapter = new ListTab1Adapter(context, dataCustomListTab1);
            LV_tab_1.setAdapter(listTab1Adapter);
            listTab1Adapter.notifyDataSetChanged();


            //==================//
            //Set Data To Tab 2//
            //==================//
            group_list_tab2 = new ArrayList<ItemGroupTab2>();
            for (int i = 0; i < arrDataStore_category.size(); i++) {

                item_cate_title = arrDataStore_category.get(i).getCate_title();
                item_cate_pic = arrDataStore_category.get(i).getCate_pic();
                item_cate_id = arrDataStore_category.get(i).getCate_id();

                ItemGroupTab2 gru = new ItemGroupTab2();
                gru.setName(item_cate_title);
                gru.setImage(item_cate_pic);

                child_list_tab2 = new ArrayList<ItemChildTab2>();

                for (int j = 0; j < arrDataStore_channel.size(); j++) {

                    item_chan_title = arrDataStore_channel.get(j).getChan_name();
                    item_chan_pic = arrDataStore_channel.get(j).getChan_pic();
                    item_chan_id = arrDataStore_channel.get(j).getChan_id();
                    item_cate_id_in_chan = arrDataStore_channel.get(j).getFr_cate_id();

                    if (item_cate_id == item_cate_id_in_chan) {

                        ItemChildTab2 ch = new ItemChildTab2();
                        ch.setName(item_chan_title);
                        ch.setImage(item_chan_pic);
                        ch.setId(item_chan_id);
                        child_list_tab2.add(ch);
                    }
                }
                gru.setItems(child_list_tab2);
                group_list_tab2.add(gru);

            }
            listTab2Adapter = new ListTab2Adapter(context, group_list_tab2);
            EXP_tab_2.setAdapter(listTab2Adapter);
           // Log.d("run","arrDataStore_category : "+arrDataStore_channel.size());

            //==================//
            //Set Data To Tab 3//
            //==================//
            group_list_tab3 = new ArrayList<ItemGroupTab3>();
            for (int i = 0; i < arrDataStore_type.size(); i++) {

                item_type_id = arrDataStore_type.get(i).getType_id();
                item_type_name = arrDataStore_type.get(i).getType_name();

                ItemGroupTab3 gru = new ItemGroupTab3();
                gru.setTypeId(item_type_id);
                gru.setTypeName(item_type_name);

                child_list_tab3 = new ArrayList<ItemChildTab3>();

                for (int j = 0; j < arrDataStore_program.size(); j++) {
                    //item_prog_id = arrDataStore_program.get(j).getProg_id();
                    item_prog_name = arrDataStore_program.get(j).getProg_name();
                    item_type_id_in_prog = arrDataStore_program.get(j).getFr_type_id();
                    item_chan_id_in_prog = arrDataStore_program.get(j).getFr_channel_id();
                    item_chan_pic = null;
                    if (item_type_id == item_type_id_in_prog) {
                        if (!temp_pro_name.contains(item_prog_name)) {
                            for (int k = 0; k < arrDataStore_channel.size(); k++) {
                                item_chan_id = arrDataStore_channel.get(k).getChan_id();
                                if (item_chan_id == item_chan_id_in_prog) {
                                    item_chan_pic = arrDataStore_channel.get(k).getChan_pic();
                                    break;
                                }
                            }
                            ItemChildTab3 ch = new ItemChildTab3();
                            ch.setChanPic(item_chan_pic);
                            ch.setProgName(item_prog_name);
                            child_list_tab3.add(ch);
                            temp_pro_name.add(item_prog_name);
                        }

                    }
                }
                gru.setItems(child_list_tab3);
                group_list_tab3.add(gru);

            }
            listTab3Adapter = new ListTab3Adapter(context, group_list_tab3);
            EXP_tab_3.setAdapter(listTab3Adapter);
           // Log.d("run","arrDataStore_program : "+arrDataStore_channel.size());

        } catch (Exception e) {
            Log.d("run","Error LoadMenuToTab : "+e);
        }
    }


    private void openDetailProgram() {

        ViewProgramDetail = activity.getLayoutInflater().inflate(R.layout.activity_detail_list, ContentFrame, false);
        ContentFrame.removeAllViews();
        listProgram = new ListProgram(ViewProgramDetail);
        ContentFrame.addView(ViewProgramDetail);
    }
}

