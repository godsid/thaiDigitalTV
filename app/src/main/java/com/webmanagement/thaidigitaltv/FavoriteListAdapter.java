package com.webmanagement.thaidigitaltv;

/**
 * Created by SystemDLL on 25/9/2557.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Banpot.S on 9/4/14 AD.
 */
public class FavoriteListAdapter extends BaseAdapter {

    ArrayList<DataCustomFavoriteList> arrayList = new ArrayList<DataCustomFavoriteList>();
    private LayoutInflater mInflater;
    private GlobalVariable globalVariable;


    public FavoriteListAdapter(Context context, ArrayList<DataCustomFavoriteList> arrayList) {
        this.arrayList = arrayList;
        mInflater = LayoutInflater.from(context);
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
        globalVariable = new GlobalVariable();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_favorite_list, null);
        }


        TextView TV_fav_list_title = (TextView) convertView.findViewById(R.id.tv_fav_list_title);
        TextView TV_fav_time_show = (TextView) convertView.findViewById(R.id.tv_fav_time_show);
        TextView TV_fav_cha_title = (TextView) convertView.findViewById(R.id.tv_fav_cha_title);


        //  convertView.setTag(objectView);

        TV_fav_list_title.setText(arrayList.get(position).list_title);
        TV_fav_time_show.setText(arrayList.get(position).time_show);
        TV_fav_cha_title.setText(arrayList.get(position).cha_title);

        globalVariable.addArrFav_Prog_id(arrayList.get(position).list_id);
        globalVariable.addArrFav_Prog_name(arrayList.get(position).list_title);

        return convertView;
    }


}


class DataCustomFavoriteList {
    String list_title, cha_title, time_show;
    int list_id;

    public DataCustomFavoriteList(int list_id, String list_title, String cha_title, String time_show) {
        this.list_id = list_id;
        this.list_title = list_title;
        this.cha_title = cha_title;
        this.time_show = time_show;
    }


}




