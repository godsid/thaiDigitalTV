package com.webmanagement.thaidigitaltv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 29/10/2557.
 */

public class DialogProgramScheduleAdapter extends BaseAdapter {

    ArrayList<DataCustomDialogProgramSchedule> arrayList = new ArrayList<DataCustomDialogProgramSchedule>();
    private LayoutInflater mInflater;

    Context context2;


    public DialogProgramScheduleAdapter(Context context, ArrayList<DataCustomDialogProgramSchedule> arrayList) {
        this.arrayList = arrayList;
        mInflater = LayoutInflater.from(context);
        this.context2 = context;


    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dialog_program, null);
        }

        TextView TV_day = (TextView) convertView.findViewById(R.id.tv_day);
        TextView TV_timestart = (TextView) convertView.findViewById(R.id.tv_timestart);
        TextView TV_timeend = (TextView) convertView.findViewById(R.id.tv_timeend);


        TV_day.setText(arrayList.get(position).day);
        TV_timestart.setText(arrayList.get(position).timestart + " - ");
        TV_timeend.setText(arrayList.get(position).timeend);

        Animation animation = null;
        animation = AnimationUtils.loadAnimation(context2, R.anim.fade_in);
        animation.setDuration(500);
        convertView.startAnimation(animation);
        animation = null;

        return convertView;
    }


}


class DataCustomDialogProgramSchedule {
    String day, timestart, timeend;

    public DataCustomDialogProgramSchedule(String day, String timestart, String timeend) {
        this.day = day;
        this.timestart = timestart;
        this.timeend = timeend;
    }


}





