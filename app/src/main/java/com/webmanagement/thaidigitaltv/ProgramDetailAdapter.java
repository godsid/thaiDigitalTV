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
public class ProgramDetailAdapter extends BaseAdapter {

    ArrayList<DataCustomProgramDetail> arrayProgramDetail = new ArrayList<DataCustomProgramDetail>();

    private LayoutInflater mInflater;

    getObject getObject;

    TextView TV_col_4;
    private int bg_color_rl;
    private int selectedPosition = 0;
    GlobalVariable globalVariable;
    ArrayList<Integer> arrHoldProg_idDB = ProgramDetail.arrHoldProg_idDB;
    Context context2;
    private int position_for_delete;

    DatabaseAction dbAction;
    int p;


    public ProgramDetailAdapter(Context context, ArrayList<DataCustomProgramDetail> arrayList) {
        globalVariable = new GlobalVariable();
        this.arrayProgramDetail = arrayList;
        mInflater = LayoutInflater.from(context);
        context2 = context;
        dbAction = new DatabaseAction(context);

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

            p = position;

            //   if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_program_detail, null);

            getObject = new getObject();
            getObject.TV_col_1 = (TextView) convertView.findViewById(R.id.tv_col_1);
            getObject.TV_col_2 = (TextView) convertView.findViewById(R.id.tv_col_2);
            getObject.TV_col_3 = (TextView) convertView.findViewById(R.id.tv_col_3);
            final TextView TV_col_4 = (TextView) convertView.findViewById(R.id.tv_col_4);
            convertView.setTag(getObject);
            // } else {
            //      getObject = (getObject) convertView.getTag();
            // }


            //  convertView.setTag(objectView);

            getObject.TV_col_1.setText(arrayProgramDetail.get(position).pname);
            getObject.TV_col_2.setText(arrayProgramDetail.get(position).pstart+"\n"+arrayProgramDetail.get(position).pend);

            TV_col_4.setText("");
            if (arrHoldProg_idDB.contains(arrayProgramDetail.get(position).id)) {
                //Log.d("run","if "+c+" : "+arrHoldProg_idDB.contains(id)+","+id);
                TV_col_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete_3, 0, 0, 0);
                globalVariable.addArrDelOrAdd("delete");
            } else {
                // Log.d("run","else "+c+" : "+arrHoldProg_idDB.contains(id)+","+id);
                TV_col_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_3, 0, 0, 0);
                globalVariable.addArrDelOrAdd("add");
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
            TV_col_4.setId(arrayProgramDetail.get(position).count);

            TV_col_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goWay(TV_col_4.getId());
                    Log.d("run", "Clicked " + TV_col_4.getId());

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
    String pname, pstart, pend;
    boolean st;

    int count;

    public DataCustomProgramDetail(int id, String pname, String pstart, String pend, boolean st, int count) {

        this.id = id;
        this.pname = pname;
        this.pstart = pstart;
        this.pend = pend;
        this.st = st;
        this.count = count;
    }
}

class getObject {
    TextView TV_col_1, TV_col_2, TV_col_3, TV_col_4;
}






