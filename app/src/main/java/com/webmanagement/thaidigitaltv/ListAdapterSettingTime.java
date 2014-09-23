package com.webmanagement.thaidigitaltv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 23/9/2557.
 */

    public class ListAdapterSettingTime extends BaseAdapter {

        ArrayList<DataCustomListView> arrayList = new ArrayList<DataCustomListView>();
        private LayoutInflater mInflater;
        private Context context;



        public ListAdapterSettingTime(Context context, ArrayList<DataCustomListView> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
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
            DataCustomListView dataCustomListView = null;

            if (convertView == null) {
                //mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.list_item_setting_time, null);

            }

           TextView TV_list_settime = (TextView)convertView.findViewById(R.id.tv_list_settime);

            TV_list_settime.setText(arrayList.get(position).title);

            return convertView;
        }

    }


    class DataCustomListView {
        String title;

        public DataCustomListView(String title) {
            this.title = title;
        }


        public String getTitle() {
            return title;
        }



    }
