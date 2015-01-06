package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 26/9/2557.
 */
public class ProgramDetailAdapter extends BaseAdapter {

    ArrayList<DataCustomProgramDetail> arrayProgramDetail = new ArrayList<DataCustomProgramDetail>();

    private LayoutInflater mInflater;

    getObject getObject;

    Context context;
    Activity activity;
    private int position_for_delete;
    DatabaseAction dbAction;
    int p;
    private int prog_id;
    private String prog_name;


    public ProgramDetailAdapter(Context context2, ArrayList<DataCustomProgramDetail> arrayList) {
        context = context2;
        activity = (Activity)context2;
        this.arrayProgramDetail = arrayList;
        mInflater = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {

            p = position;

            convertView = mInflater.inflate(R.layout.item_program_detail, null);

            getObject = new getObject();
            getObject.TV_col_1 = (TextView) convertView.findViewById(R.id.tv_col_1);
            getObject.TV_col_2 = (TextView) convertView.findViewById(R.id.tv_col_2);
            ImageView IV_col_3 = (ImageView) convertView.findViewById(R.id.tv_col_3);
            getObject.IV_col_4 = (ImageView) convertView.findViewById(R.id.iv_col_4);
            convertView.setTag(getObject);

            getObject.TV_col_1.setText(arrayProgramDetail.get(position).pname);
            getObject.TV_col_2.setText(arrayProgramDetail.get(position).pstart + "\n" + arrayProgramDetail.get(position).pend);

            if (arrayProgramDetail.get(position).haveindb) {
                getObject.IV_col_4.setImageResource(R.drawable.ic_delete);
            } else {
                getObject.IV_col_4.setImageResource(R.drawable.ic_add);
            }

            getObject.IV_col_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_anim_pressed));
                    if (arrayProgramDetail.get(position).haveindb) {
                        actionDelete(position);

                    } else {

                        Intent intent = new Intent(context, SettingTime.class);
                        intent.putExtra("i_Prog_id", arrayProgramDetail.get(position).id);
                        intent.putExtra("i_Prog_name", arrayProgramDetail.get(position).pname);
                        intent.putExtra("i_Prog_timestart", arrayProgramDetail.get(position).pstart);
                        intent.putExtra("i_Day_id", arrayProgramDetail.get(position).did);
                        intent.putExtra("i_Chan_name", GlobalVariable.getChan_name());
                        intent.putExtra("i_Action_type", "add");
                       //activity.startActivity(intent);
                        activity.startActivityForResult(intent,123);
                        //context.(intent);
                    }
                }
            });

            if (arrayProgramDetail.get(position).st) {
                //convertView.setSelected(true);
                if (GlobalVariable.isHaveTVNetwork()) {
                    IV_col_3.setImageResource(R.drawable.ic_share);
                    IV_col_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_anim_pressed));
                            new DialogDeviceList(context);
                        }
                    });
                } else {
                    IV_col_3.setImageResource(R.drawable.ic_onair);
                }
            } else {
                IV_col_3.setImageDrawable(null);
            }


            Animation animation = null;
            animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);

            animation.setDuration(500);
            convertView.startAnimation(animation);
            animation = null;


        } catch (Exception e) {
            Log.d("run", "Exception : " + e);
        }

        return convertView;
    }


    public void actionDelete(int p) {
        position_for_delete = p;
        prog_id = arrayProgramDetail.get(position_for_delete).id;
        prog_name = arrayProgramDetail.get(position_for_delete).pname;
       // Log.d("run",position_for_delete+" "+GlobalVariable.getArrProg_id(position_for_delete)+" "+arrayProgramDetail.get(position_for_delete).id);
        String s = "คุณแน่ใจที่จะลบรายการ " + prog_name + " ออกจากรายการโปรดหรือไม่";
        AlertDialog.Builder builder = GlobalVariable.simpleDialogTemplate(context, "ยืนยัน", s);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean chkDeleted = dbAction.deleteFavoriteProgram(prog_id);
                        if (chkDeleted) {
                            Toast.makeText(context, "Delete Complete", Toast.LENGTH_SHORT).show();

                          //  FragmentProgramDetail.arrHoldProg_idDB.add(arrayProgramDetail.get(position_for_delete).id);
                            arrayProgramDetail.get(position_for_delete).haveindb = false;
                            getObject.IV_col_4.setImageResource(R.drawable.ic_add);
                            notifyDataSetChanged();

                        } else {
                            Toast.makeText(context, "Can't Delete ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();

    }


} // End Class


class DataCustomProgramDetail {
    int id;
    String pname, pstart, pend;
    boolean st, haveindb;
    int did;
    int count;

    public DataCustomProgramDetail(int id, String pname, String pstart, String pend, boolean st, int count, boolean haveindb,int did) {

        this.id = id;
        this.pname = pname;
        this.pstart = pstart;
        this.pend = pend;
        this.st = st;
        this.count = count;
        this.haveindb = haveindb;
        this.did = did;
    }
}

class getObject {
    TextView TV_col_1, TV_col_2;
    ImageView IV_col_4;
}






