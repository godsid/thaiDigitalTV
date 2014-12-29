package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidquery.AQuery;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by SystemDLL on 25/11/2557.
 */
public class ListTab1 extends SherlockFragment {
    View rootView;
    Context context;
    ListView LV_tab_1;
    Activity activity;
    Tracker t;
    Calendar calendar;
    AQuery aq;

    DatabaseAction dbAction;
    ListTab1Adapter listTab1Adapter;
    ArrayList<DataCustomListTab1> dataCustomListTab1;

    ArrayList<DataStore_Channel> arrDataStore_channel = MainActivity.arrDataStore_channel;
    int g_current_day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragmenttab1.xml
        View view = inflater.inflate(R.layout.inc_tab_1, container, false);
    //Log.d("run","TAB1");
        this.rootView = view;
        this.context = rootView.getContext();
        aq = new AQuery(context);
        this.activity = (Activity) context;
        calendar = Calendar.getInstance();
        dbAction = new DatabaseAction(context);

        t = ((MyApplication) ((Activity) context).getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
        g_current_day = calendar.get(Calendar.DAY_OF_WEEK);

      //  ArrayAdapter<String> adp = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,a);
        LV_tab_1 = (ListView) view.findViewById(R.id.lv_tab_1);
      //  LV_tab_1.setAdapter(adp);
        LoadMenuToTab();


        LV_tab_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String get_channel_name = arrDataStore_channel.get(position).getChan_name();

                int get_channel_id = arrDataStore_channel.get(position).getChan_id();

                GlobalVariable.setChan_id(get_channel_id);
                GlobalVariable.setChan_name(get_channel_name);

                t.setScreenName("ช่อง_" + get_channel_id);
                t.send(new HitBuilders.AppViewBuilder().build());


                openDetailProgram();
            }
        });

      return  view;
    }


    private void LoadMenuToTab() {
         // Log.d("run","LoadMenuToTab 1");
        try {
            String item_chan_title, item_chan_pic;
            int item_chan_id;
            dataCustomListTab1 = new ArrayList<DataCustomListTab1>();
            //==================//
            //Set Data To Tab 1//
            //==================//
            for (int j = 0; j < arrDataStore_channel.size(); j++) {

                item_chan_title = arrDataStore_channel.get(j).getChan_name();
                item_chan_pic = arrDataStore_channel.get(j).getChan_pic();
                item_chan_id = arrDataStore_channel.get(j).getChan_id();
                String gProgOnair = getProgOnair(item_chan_id,g_current_day);
                dataCustomListTab1.add(new DataCustomListTab1(item_chan_id, item_chan_title, item_chan_pic,gProgOnair));

            }
            // Log.d("run","arrDataStore_channel : "+arrDataStore_channel.size());
            listTab1Adapter = new ListTab1Adapter(context,dataCustomListTab1);
            LV_tab_1.setAdapter(listTab1Adapter);
            listTab1Adapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.d("run", "Error LoadMenuToTab 1 : " + e);
        }

    }


    private void openDetailProgram() {

        Intent intent = new Intent(activity.getApplicationContext(), ProgramDetail.class);
        activity.startActivity(intent);

    }


    public String getProgOnair(int gChanId,int gDayId) {
        SQLiteCursor cur = (SQLiteCursor) dbAction.readProgByChanDayId(gChanId, gDayId,"");
        while (!cur.isAfterLast()) {
            //Log.d("run","P "+cur.getString(1));
            String prog_title = cur.getString(1);
            String prog_timestart = cur.getString(2);
            String prog_timeend = cur.getString(3);


            try {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Date TimeStart = simpleDateFormat.parse(prog_timestart);
                Date TimeEnd = simpleDateFormat.parse(prog_timeend);
                String TimeNow = simpleDateFormat.format(date);
                String TS = simpleDateFormat.format(TimeStart);
                String TE = simpleDateFormat.format(TimeEnd);

                if (simpleDateFormat.parse(TimeNow).before((simpleDateFormat.parse(TE)))) {
                    if (simpleDateFormat.parse(TimeNow).before(simpleDateFormat.parse(TS))) {
                        //  Log.d("run", c + " : " + TimeNow + " Not yet " + TS + " - " + TE);
                    } else {
                        //  Log.d("run", c + " : " + TimeNow + " NOW " + TS + " - " + TE);
                        return prog_title;
                    }
                } else {
                    //   Log.d("run", c + " : " + TimeNow + " Over " + TS + " - " + TE);
                }
            } catch (Exception e) {
                Log.d("run", "Error Parse Date "+gChanId+" "+gDayId+" "+prog_title+" "+prog_timestart+" " +prog_timeend+" | "+ e);
            }
            cur.moveToNext();
        }
        cur.close();
        return "";
    }
}




