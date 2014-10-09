package com.webmanagement.thaidigitaltv;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 7/10/2557.
 */
public class MenuLeftAdapter  extends BaseAdapter {

    ArrayList<DataCustomMenuLeft> arraySettingTime = new ArrayList<DataCustomMenuLeft>();
    private LayoutInflater mInflater;
AQuery aq;



    public MenuLeftAdapter(Context context, ArrayList<DataCustomMenuLeft> arrayList) {
        this.arraySettingTime = arrayList;
        mInflater = LayoutInflater.from(context);
        aq = new AQuery(context);
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

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_menu_left, null);
        }

        TextView TV_menu_left = (TextView) convertView.findViewById(R.id.tv_menu_left);
        ImageView IV_menu_left = (ImageView) convertView.findViewById(R.id.iv_menu_left);

        TV_menu_left.setText(arraySettingTime.get(position).title);
        aq.id(IV_menu_left).image(arraySettingTime.get(position).pic);


        return convertView;
    }


}


class DataCustomMenuLeft {
    String title;
    int pic;

    public DataCustomMenuLeft(String title, int pic) {
        this.title = title;
        this.pic = pic;
    }


}





