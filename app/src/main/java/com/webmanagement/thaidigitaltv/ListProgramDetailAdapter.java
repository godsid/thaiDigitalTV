package com.webmanagement.thaidigitaltv;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 26/9/2557.
 */
public class ListProgramDetailAdapter extends BaseAdapter {

    ArrayList<DataCustomProgramDetail> arrayProgramDetail = new ArrayList<DataCustomProgramDetail>();
    private LayoutInflater mInflater;


    private int bg_color_rl;
    private int selectedPosition = 0;
    DatabaseAction dbAction;
    private ArrayList<Integer> arrHoldProg_idDB = new ArrayList<Integer>();
    private ArrayList<String> arrDelorAdd = new ArrayList<String>();
    TextView TV_col_1,TV_col_2,TV_col_3,TV_col_4;



    public ListProgramDetailAdapter(Context context, ArrayList<DataCustomProgramDetail> arrayList){
        this.arrayProgramDetail = arrayList;
        mInflater = LayoutInflater.from(context);
        dbAction = new DatabaseAction(context);
    }

    public String getArrDelorAdd(int s) {
        return arrDelorAdd.get(s);
    }

    public void clearHoldArrProg_IdFromDB() {
        arrHoldProg_idDB.clear();
    }

    public void clearArrDelOrAdd() {
        arrDelorAdd.clear();
    }
    public void  setHoldArrProg_idFromDB() {

            SQLiteCursor cur = (SQLiteCursor) dbAction.readAllFavoriteProgram();

            while (!cur.isAfterLast()) {

                arrHoldProg_idDB.add(Integer.parseInt(cur.getString(1)));
                cur.moveToNext();

            }

            cur.close();

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

        if(convertView==null){
            convertView = mInflater.inflate(R.layout.custom_program_detail,null);
        }

        TV_col_1 = (TextView) convertView.findViewById(R.id.tv_col_1);
        TV_col_2 = (TextView) convertView.findViewById(R.id.tv_col_2);
        TV_col_3 = (TextView) convertView.findViewById(R.id.tv_col_3);
        TV_col_4 = (TextView) convertView.findViewById(R.id.tv_col_4);




        //  convertView.setTag(objectView);

        TV_col_1.setText(arrayProgramDetail.get(position).col_1);
        TV_col_2.setText(arrayProgramDetail.get(position).col_2);

        TV_col_4.setText("");
        if (arrHoldProg_idDB.contains(arrayProgramDetail.get(position).id)) {
            //Log.d("run","if "+c+" : "+arrHoldProg_idDB.contains(id)+","+id);
            TV_col_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete_3, 0, 0, 0);
            arrDelorAdd.add("delete");
        } else {
           // Log.d("run","else "+c+" : "+arrHoldProg_idDB.contains(id)+","+id);
            TV_col_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_3, 0, 0, 0);
            arrDelorAdd.add("add");
        }

        TV_col_3.setText("");
        if (arrayProgramDetail.get(position).st) {
            //Log.d("run","if "+c+" : "+arrHoldProg_idDB.contains(id)+","+id);
            TV_col_3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_onair_2, 0, 0, 0);
        } else {
            // Log.d("run","else "+c+" : "+arrHoldProg_idDB.contains(id)+","+id);
            //TV_col_4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_3, 0, 0, 0);
        }
/*
        TV_col_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detailProgram.seItem_selected(TV_col_4.getId());

                if (arrDelorAdd.get(TV_col_4.getId()).equals("add")) {

                    //Log.d("run","if add "+arrDelorAdd.get(tv_col_4.getId())+" , "+tv_col_4.getId());
                   // Log.d("run","EXP IF");

                    extendsMain.gotoSetTimeList();
                } else if(arrDelorAdd.get(TV_col_4.getId()).equals("delete")){

                    extendsMain.gotoMenuActionDelete(TV_col_4.getId());

                    //setEexpLeftChildSelected(2);
                    Log.d("run","EXP EL");
                }

                //Toast.makeText(getApplicationContext(), "Click row at :" + tb_row.getId(), Toast.LENGTH_SHORT).show();

            }
        });



/*
        TV_col_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedPosition((Integer) v.getTag());
                notifyDataSetInvalidated();
                Log.d("run", "CLICK : " + getSelectedPosition());
            }
        });

*/
        return convertView;
    }



    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }


}


class DataCustomProgramDetail{
    int id;
    String col_1,col_2,col_3;
    boolean st;

    int count;
    public DataCustomProgramDetail(int dis,String col_1,String col_2,boolean st,int count){

        this.id = dis;
        this.col_1 = col_1;
        this.col_2 = col_2;
        this.col_3 = col_3;
        this.st = st;
        this.count = count;
    }


}






