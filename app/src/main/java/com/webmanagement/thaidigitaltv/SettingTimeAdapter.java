package com.webmanagement.thaidigitaltv;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 26/9/2557.
 */
public class SettingTimeAdapter extends BaseAdapter {

    ArrayList<DataCustomSettingTime> arraySettingTime = new ArrayList<DataCustomSettingTime>();
    private LayoutInflater mInflater;


private int bg_color_rl;
    private int selectedPosition = 0;


    public SettingTimeAdapter(Context context, ArrayList<DataCustomSettingTime> arrayList){
        this.arraySettingTime = arrayList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arraySettingTime.size();
    }

    @Override
    public Object getItem(int position) {
        return arraySettingTime.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = mInflater.inflate(R.layout.custom_setting_list,null);
        }

        TextView TV_settime_timeval = (TextView) convertView.findViewById(R.id.tv_settime_timeval);
        TextView TV_settime_min = (TextView) convertView.findViewById(R.id.tv_settime_min);
        RadioButton RB_settime_line = (RadioButton) convertView.findViewById(R.id.rb_settime_line);
        RelativeLayout RL_custom_settime = (RelativeLayout) convertView.findViewById(R.id.rl_custom_settime);

        if ((arraySettingTime.get(position).chk_color_val % 2) != 0)
            bg_color_rl = Color.rgb(252, 236, 232);
        else
            bg_color_rl = Color.rgb(228, 216, 205);

        //  convertView.setTag(objectView);

        TV_settime_timeval.setText(Integer.toString(arraySettingTime.get(position).time_val));
        TV_settime_min.setText("นาที");

        RB_settime_line.setChecked(position == getSelectedPosition());
        RL_custom_settime.setBackgroundColor(bg_color_rl);
        RL_custom_settime.setTag(position);

        RL_custom_settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedPosition((Integer) v.getTag());
                notifyDataSetInvalidated();
                Log.d("run", "CLICK : " + getSelectedPosition());
            }
        });


        return convertView;
    }



    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }


}


class DataCustomSettingTime{
    int time_val;
    int chk_color_val;

    public DataCustomSettingTime(int time_val,int chk_color_val){
        this.time_val = time_val;
        this.chk_color_val = chk_color_val;
    }


}





