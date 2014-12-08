package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidquery.AQuery;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by SystemDLL on 25/11/2557.
 */
public class ListTab3  extends SherlockFragment {
    View rootView;
    Context context;
    ExpandableListView  EXP_tab_3;

    Activity activity;
    Tracker t;

    AQuery aq;

    ArrayList<DataCustomDialogProgramSchedule> dataCustomDialogProgramSchedule = new ArrayList<DataCustomDialogProgramSchedule>();
    DialogProgramScheduleAdapter dialogProgramScheduleAdapter;

    String[] arr_day = new String[]{"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};

    ListTab3Adapter listTab3Adapter;
    ArrayList<ItemGroupTab3> group_list_tab3;
    ArrayList<ItemChildTab3> child_list_tab3;

    ArrayList<DataStore_Channel> arrDataStore_channel = MainActivity.arrDataStore_channel;
    ArrayList<DataStore_Program> arrDataStore_program = MainActivity.arrDataStore_program;
    ArrayList<DataStore_Type> arrDataStore_type = MainActivity.arrDataStore_type;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragmenttab1.xml
        View view = inflater.inflate(R.layout.inc_tab_3, container, false);
        //Log.d("run","TAB3");

        this.rootView = view;
        this.context = rootView.getContext();
        aq = new AQuery(context);
        this.activity = (Activity) context;

        t = ((MyApplication) ((Activity) context).getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        EXP_tab_3 = (ExpandableListView) rootView.findViewById(R.id.exp_tab_3);


        LoadMenuToTab();

        EXP_tab_3.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String get_prog_name = group_list_tab3.get(groupPosition).getItems().get(childPosition).getProgName();

                String get_channel_pic = group_list_tab3.get(groupPosition).getItems().get(childPosition).getChanPic();
                //get position highlight
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);
                //Toast.makeText(context,get_prog_name,Toast.LENGTH_SHORT).show();
                t.setScreenName("ประเภทรายการ_" + group_list_tab3.get(groupPosition).getTypeName());
                t.send(new HitBuilders.AppViewBuilder().build());

                showDialogDetailProgram(get_prog_name, get_channel_pic);

                return false;
            }
        });

return view;
    }


    private void LoadMenuToTab() {
        //Log.d("run","LoadMenuToTab 3");
        try {


            String  item_chan_pic;
            int item_chan_id;

            String item_type_name;
            int item_type_id;

            String item_prog_name;

            int  item_type_id_in_prog, item_chan_id_in_prog;

            ArrayList<String> temp_pro_name = new ArrayList<String>();
            temp_pro_name.clear();


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
            Log.d("run", "Error LoadMenuToTab 3 : " + e);
        }
    }


    private void showDialogDetailProgram(String pn, String cp) {

        t.setScreenName("รายการ_" + pn);
        t.send(new HitBuilders.AppViewBuilder().build());

        AlertDialog.Builder dlb = new AlertDialog.Builder(context,R.style.Run_ButtonDialog);
        dlb.setCancelable(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_program_schedule, null);
        ListView LV_prog_schedule = (ListView) view.findViewById(R.id.lv_prog_schedule);
        TextView TV_prog_name = (TextView) view.findViewById(R.id.tv_prog_name);
        TextView TV_chan_id = (TextView) view.findViewById(R.id.tv_chan_id);
        //ImageView IV_chan_pic = (ImageView) view.findViewById(R.id.iv_chan_pic);

        //aq.id(IV_chan_pic).image(cp);
        TV_prog_name.setText(pn);
        prepareDataToList(LV_prog_schedule, pn, TV_chan_id);

        dlb.setView(view);
        //Toast.makeText(context, cp, Toast.LENGTH_SHORT).show();
        dlb.setNegativeButton("ปิดหน้าต่าง", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        dlb.show();
    }


    private void prepareDataToList(ListView lv, String pn, TextView tvchid) {
        String item_time_start, item_prog_name, item_time_end;
        int item_day_id, item_chan_id;
        dataCustomDialogProgramSchedule.clear();
        Collections.sort(arrDataStore_program, new Comparator<DataStore_Program>() {
            @Override
            public int compare(DataStore_Program lhs, DataStore_Program rhs) {
                if (lhs.getFr_day_id() > rhs.getFr_day_id())
                    return 1;
                if (lhs.getFr_day_id() < rhs.getFr_day_id())
                    return -1;
                return 0;
            }
        });

        for (int j = 0; j < arrDataStore_program.size(); j++) {
            item_prog_name = arrDataStore_program.get(j).getProg_name();
            item_time_start = arrDataStore_program.get(j).getProg_timestart();
            item_time_end = arrDataStore_program.get(j).getProg_timeend();
            item_day_id = arrDataStore_program.get(j).getFr_day_id();
            item_chan_id = arrDataStore_program.get(j).getFr_channel_id();

            if (item_prog_name.equals(pn)) {
                dataCustomDialogProgramSchedule.add(new DataCustomDialogProgramSchedule(arr_day[item_day_id], item_time_start, item_time_end));
                tvchid.setText("ช่อง " + Integer.toString(item_chan_id));
            }

        }

        dialogProgramScheduleAdapter = new DialogProgramScheduleAdapter(context, dataCustomDialogProgramSchedule);
        lv.setAdapter(dialogProgramScheduleAdapter);
    }


    }

