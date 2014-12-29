package com.webmanagement.thaidigitaltv;

/**
 * Created by SystemDLL on 25/9/2557.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by Banpot.S on 9/4/14 AD.
 */
public class ListTab1Adapter extends BaseAdapter {

    ArrayList<DataCustomListTab1> arrayList = new ArrayList<DataCustomListTab1>();
    private LayoutInflater mInflater;
    AQuery aq;
    Context context2;


    public ListTab1Adapter(Context context, ArrayList<DataCustomListTab1> arrayList) {
        this.arrayList = arrayList;
        this.context2 = context;
        mInflater = LayoutInflater.from(context);
        aq = new AQuery(context);
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
            convertView = mInflater.inflate(R.layout.item_lv_tab_1, null);
        }
        TextView tv_title_lv_tab_1 = (TextView) convertView.findViewById(R.id.tv_title_lv_tab_1);
        TextView tv_prog_onair = (TextView) convertView.findViewById(R.id.tv_prog_onair);
        ImageView iv_title_lv_tab_1 = (ImageView) convertView.findViewById(R.id.iv_channel_lv_tab_1);
        tv_title_lv_tab_1.setText(arrayList.get(position).ch_name);
        tv_prog_onair.setText(arrayList.get(position).prog_onair);
        aq.id(iv_title_lv_tab_1).image(arrayList.get(position).ch_pic);
        Animation animation = null;
        animation = AnimationUtils.loadAnimation(context2, R.anim.fade_in);
        animation.setDuration(500);
        convertView.startAnimation(animation);
        animation = null;

        return convertView;
    }


}


class DataCustomListTab1 {
    String ch_name, ch_pic, prog_onair;
    int ch_id;

    public DataCustomListTab1(int ch_id, String ch_name, String ch_pic,String prog_onair) {
        this.ch_name = ch_name;
        this.ch_pic = ch_pic;
        this.ch_id = ch_id;
        this.prog_onair = prog_onair;
    }


}




