package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.androidquery.AQuery;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 25/11/2557.
 */
public class ListTab2 extends SherlockFragment {
    View rootView;
    Context context;
    ExpandableListView EXP_tab_2;
    Activity activity;
    Tracker t;

    AQuery aq;

    ListTab2Adapter listTab2Adapter;
    ArrayList<ItemGroupTab2> group_list_tab2;
    ArrayList<ItemChildTab2> child_list_tab2;


    ArrayList<DataStore_Category> arrDataStore_category = MainActivity.arrDataStore_category;
    ArrayList<DataStore_Channel> arrDataStore_channel = MainActivity.arrDataStore_channel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragmenttab1.xml
        View view = inflater.inflate(R.layout.inc_tab_2, container, false);
        //Log.d("run","TAB2");

        this.rootView = view;
        this.context = rootView.getContext();
        this.activity = (Activity) context;
        aq = new AQuery(context);

        t = ((MyApplication) ((Activity) context).getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        EXP_tab_2 = (ExpandableListView) view.findViewById(R.id.exp_tab_2);

        LoadMenuToTab();

        EXP_tab_2.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String get_channel_name = group_list_tab2.get(groupPosition).getItems().get(childPosition).getName();
                int get_channel_id = group_list_tab2.get(groupPosition).getItems().get(childPosition).getId();
                String get_channel_pic = group_list_tab2.get(groupPosition).getItems().get(childPosition).getImage();
                //get position highlight
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);
                t.setScreenName("หมวดหมู่_" + group_list_tab2.get(groupPosition).getName());
                t.send(new HitBuilders.AppViewBuilder().build());

                GlobalVariable.setChan_id(get_channel_id);
                GlobalVariable.setChan_name(get_channel_name);

                openDetailProgram();
                return false;
            }
        });

       return view;

    }


    private void LoadMenuToTab() {
        //Log.d("run","LoadMenuToTab 2");
        try {

            String item_cate_title, item_cate_pic;
            int item_cate_id;

            String item_chan_title, item_chan_pic;
            int item_chan_id;

            int item_cate_id_in_chan;

            ArrayList<String> temp_pro_name = new ArrayList<String>();
            temp_pro_name.clear();


            //==================//
            //Set Data To Tab 2//
            //==================//
            group_list_tab2 = new ArrayList<ItemGroupTab2>();
            for (int i = 0; i < arrDataStore_category.size(); i++) {

                item_cate_title = arrDataStore_category.get(i).getCate_name();
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

        } catch (Exception e) {
            Log.d("run", "Error LoadMenuToTab 2 :" + e);
        }
    }

    private void openDetailProgram() {

        Intent intent = new Intent(activity.getApplicationContext(), ProgramDetail.class);
        activity.startActivity(intent);

    }


    }

