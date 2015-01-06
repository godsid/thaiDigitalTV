package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidquery.AQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SystemDLL on 23/12/2557.
 */
public class FragmentProgramDetail extends SherlockFragment {

    ListView LV_program_detail;
    DatabaseAction dbAction;
    View rootView;
    Context context;
    Activity activity;
    AQuery aq;
    int scoreFirstVisible = 0;
    ProgramDetailAdapter programDetailAdapter;
    ArrayList<DataCustomProgramDetail> dataCustomProgramDetail = new ArrayList<DataCustomProgramDetail>();
    Calendar calendar;
    public static ArrayList<Integer> arrHoldProg_idDB = new ArrayList<Integer>();

    int current_date;
    int user_select_day,user_select_date;
    boolean isToDay;
    int select_date;


    public FragmentProgramDetail(int select_date) {
        this.select_date = select_date + 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program_detail, container, false);
        this.rootView = view;
        this.context = rootView.getContext();
        aq = new AQuery(context);
        this.activity = (Activity) context;
        dbAction = new DatabaseAction(context);
        calendar = Calendar.getInstance();
        LV_program_detail = (ListView) view.findViewById(R.id.lv_program_detail);

        current_date = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DATE, select_date);
        user_select_date = calendar.get(Calendar.DAY_OF_MONTH);
        user_select_day = calendar.get(Calendar.DAY_OF_WEEK);

        programDetailAdapter = new ProgramDetailAdapter(context, dataCustomProgramDetail);
        LV_program_detail.setAdapter(programDetailAdapter);

            if (current_date == user_select_date)
                isToDay = true;
            else
                isToDay = false;

         //   Log.d("run", "completeThree " + completeThree);
        //    Log.d("run", current_date + " " + selected_date + " " + selected_day + " " + isToDay);
            setDataToLV();
            return view;
        }



    public void setHoldArrProg_idFromDB() {
        arrHoldProg_idDB.clear();
        SQLiteCursor cur = (SQLiteCursor) dbAction.readAllFavoriteProgram();

        while (!cur.isAfterLast()) {

            arrHoldProg_idDB.add(Integer.parseInt(cur.getString(1)));
            cur.moveToNext();

        }
        cur.close();
    }


    public void setDataToLV() {
        Log.d("run", "========== ProgramDetail setDataToLV =========");
        //programDetailAdapter = new ProgramDetailAdapter(this, dataCustomProgramDetail);
        dataCustomProgramDetail.clear();
        setHoldArrProg_idFromDB();

        int c = 0;
        int chan = GlobalVariable.getChan_id();
        String shortby = "time_start ASC";

        SQLiteCursor cur = (SQLiteCursor) dbAction.readProgByChanDayId(chan,user_select_day,shortby);
        while (!cur.isAfterLast()) {
            int prog_id = cur.getInt(0);
            String prog_name = cur.getString(1);
            String prog_timestart = cur.getString(2);
            String prog_timeend = cur.getString(3);
            int day_id = cur.getInt(4);
            boolean status_onair = false;
            boolean have_in_db;

                try {
                    Date date = new Date();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                    Date TimeStart = simpleDateFormat.parse(prog_timestart);
                    Date TimeEnd = simpleDateFormat.parse(prog_timeend);
                    String TimeNow = simpleDateFormat.format(date);
                    String TS = simpleDateFormat.format(TimeStart);
                    String TE = simpleDateFormat.format(TimeEnd);

                    if (isToDay) {
                        if (simpleDateFormat.parse(TimeNow).before((simpleDateFormat.parse(TE)))) {
                            if (simpleDateFormat.parse(TimeNow).before(simpleDateFormat.parse(TS))) {
                                //  Log.d("run", c + " : " + TimeNow + " Not yet " + TS + " - " + TE);
                                status_onair = false;
                            } else {
                                //  Log.d("run", c + " : " + TimeNow + " NOW " + TS + " - " + TE);
                                status_onair = true;
                                scoreFirstVisible = c;
                                GlobalVariable.setFirstVisible(scoreFirstVisible);

                            }
                        } else {
                            //   Log.d("run", c + " : " + TimeNow + " Over " + TS + " - " + TE);
                            status_onair = false;
                        }
                    }
                } catch (Exception e) {
                    Log.d("run", "Error Parse Date " + e);
                }

                if (arrHoldProg_idDB.contains(prog_id)) {
                    have_in_db = true;
                } else {
                    have_in_db = false;
                }
                dataCustomProgramDetail.add(new DataCustomProgramDetail(prog_id, prog_name, prog_timestart, prog_timeend, status_onair, c, have_in_db,day_id));
                c++;
            cur.moveToNext();
        }
        cur.close();
      //  Log.d("run",selected_day+" | "+day_id2);
        //programDetailAdapter.notifyDataSetChanged();

        if (isToDay) {
            LV_program_detail.setSelection(scoreFirstVisible);
        } else {
            LV_program_detail.setSelection(0);
        }


        programDetailAdapter.notifyDataSetChanged();

    }



    @Override
    public void onResume() {
        super.onResume();
        setDataToLV();
        Log.d("run","onResume Fr");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }




}
