package com.webmanagement.thaidigitaltv;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 26/9/2557.
 */
public class ListProgramDetailAdapter extends BaseAdapter {

    ArrayList<DataCustomProgramDetail> arrayProgramDetail = new ArrayList<DataCustomProgramDetail>();

    private LayoutInflater mInflater;

    getObject getObject;


    private int bg_color_rl;
    private int selectedPosition = 0;
    Store_Variable storeVariable;
    ArrayList<Integer> arrHoldProg_idDB = ProgramDetail.arrHoldProg_idDB;




    int p;


    public ListProgramDetailAdapter(Context context, ArrayList<DataCustomProgramDetail> arrayList) {
        storeVariable = new Store_Variable();
        this.arrayProgramDetail = arrayList;
        mInflater = LayoutInflater.from(context);

       //  dbAction = new DatabaseAction(context);
    }










    @Override
    public int getCount() {
        return arrayProgramDetail.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayProgramDetail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {


            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_program_detail, null);

                getObject = new getObject();
                getObject.TV_col_1 = (TextView) convertView.findViewById(R.id.tv_col_1);
                getObject.TV_col_2 = (TextView) convertView.findViewById(R.id.tv_col_2);
                getObject.TV_col_3 = (TextView) convertView.findViewById(R.id.tv_col_3);
                getObject.TV_col_4 = (TextView) convertView.findViewById(R.id.tv_col_4);
                convertView.setTag(getObject);
            } else {
                getObject = (getObject) convertView.getTag();
            }


            //  convertView.setTag(objectView);

            getObject.TV_col_1.setText(arrayProgramDetail.get(position).col_1);
            getObject.TV_col_2.setText(arrayProgramDetail.get(position).col_2);

            getObject.TV_col_4.setText("");
            if (arrHoldProg_idDB.contains(arrayProgramDetail.get(position).id)) {
                //Log.d("run","if "+c+" : "+arrHoldProg_idDB.contains(id)+","+id);
                getObject.TV_col_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete_3, 0, 0, 0);
                storeVariable.arrDelOrAdd.add("delete");
            } else {
                // Log.d("run","else "+c+" : "+arrHoldProg_idDB.contains(id)+","+id);
                getObject.TV_col_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_3, 0, 0, 0);
                storeVariable.arrDelOrAdd.add("add");
            }

            getObject.TV_col_3.setText("");
            if (arrayProgramDetail.get(position).st) {
               // Log.d("run", "st i : " + arrayProgramDetail.get(position).st);
                getObject.TV_col_3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_onair, 0, 0, 0);
            } else {
               // Log.d("run", "st e : " + arrayProgramDetail.get(position).st);
                getObject.TV_col_3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_offline, 0, 0, 0);
            }
/*
        getObject.TV_col_4.setId(arrayProgramDetail.get(position).count);

        getObject.TV_col_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("run", "Clicked " + getItem(p).toString());
            }
        });
        */

        } catch (Exception e) {
            Log.d("run", "Exception : " + e);
        }
        return convertView;
    }


    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }


}


class DataCustomProgramDetail {
    int id;
    String col_1, col_2, col_3;
    boolean st;

    int count;

    public DataCustomProgramDetail(int dis, String col_1, String col_2, boolean st, int count) {

        this.id = dis;
        this.col_1 = col_1;
        this.col_2 = col_2;
        this.col_3 = col_3;
        this.st = st;
        this.count = count;
    }
}

class getObject {
    TextView TV_col_1, TV_col_2, TV_col_3, TV_col_4;
}






