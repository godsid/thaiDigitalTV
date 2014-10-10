package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FavoriteList {

    View rootView;
    Context context;

    TextView TV_fav_list_title;
    ListView listView;

    ArrayList<DataCustomFavoriteList> arrayListData = new ArrayList<DataCustomFavoriteList>();

    FavoriteListAdapter favoriteListAdapter;
    private DatabaseAction dbAction;
    private DetailProgram detailProgram;
    private int itemPosition;

    String[] arr_day = new String[]{"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};
    public FavoriteList(View rootView) {
        this.rootView = rootView;
        this.context = rootView.getContext();

        TV_fav_list_title = (TextView) rootView.findViewById(R.id.tv_fav_list_title);

        final Animation animAlpha = AnimationUtils.loadAnimation(context, R.anim.anim_alpha);

        dbAction = new DatabaseAction(context);
        detailProgram = new DetailProgram();


        favoriteListAdapter = new FavoriteListAdapter(context, arrayListData);

        listView = (ListView) rootView.findViewById(R.id.lv_fav_show);

        ImageView IV_fav_delete_all = (ImageView) rootView.findViewById(R.id.iv_fav_delete_all);

        listView.setAdapter(favoriteListAdapter);
        prepareDataToList();


       
        IV_fav_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuActionDeleteAll();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                view.startAnimation(animAlpha);
                setItemPosition(position);
                //Toast.makeText(getApplicationContext(), "Prog id "+ list_id, Toast.LENGTH_SHORT).show();
                showActionMenuDialog();
            }

        });

    }


    private void prepareDataToList() {

        detailProgram.clearFavArray();
        arrayListData.clear();

        SQLiteCursor cur = (SQLiteCursor) dbAction.readAllFavoriteProgram();
        while (!cur.isAfterLast()) {
            int prog_id = Integer.parseInt(cur.getString(1));
            String prog_name = cur.getString(2);
            String chan_name = cur.getString(3);
            String time_start = cur.getString(4);
            String time_before = cur.getString(5);
            int day_id = cur.getInt(6);
            int repeat_id = cur.getInt(7);
            String st_repeat;
            if (repeat_id == 0)
                st_repeat = "เตือนครั้งเดียว";
            else
                st_repeat = "เตือนซ้ำทุกสัปดาห์";
            String time_sb = "แจ้งเตือน " + time_before + " นาที ก่อนออกอากาศเวลา " + time_start;
            String ln3 = "วัน"+arr_day[day_id]+"  "+chan_name+"  "+st_repeat;
            arrayListData.add(new DataCustomFavoriteList(prog_id, prog_name, ln3, time_sb));
            cur.moveToNext();
        }
        cur.close();

        favoriteListAdapter.notifyDataSetChanged();
    }

    public void setItemPosition(int i) {
        this.itemPosition = i;
    }


    public int getItemPosition() {
        return this.itemPosition;
    }

    private void showActionMenuDialog() {
        new AlertDialog.Builder(context)
                .setTitle("เมนู")
                .setItems(R.array.dialog_menu_fav,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                if (i == 0)
                                    menuActionEdit();
                                else if (i == 1)
                                    menuActionDelete();

                            }
                        })
                .show();
    }


    private void menuActionEdit() {

    }

    private void menuActionDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ยืนยันการลบ");
        builder.setMessage("คุณแน่ใจที่จะลบรายการ " + detailProgram.getFavProg_name(getItemPosition()));
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean chkDeleted = dbAction.deleteFavoriteProgram(detailProgram.getFavProg_id(getItemPosition()));
                        if (chkDeleted == true) {
                            Toast.makeText(context, "Delete Complete", Toast.LENGTH_SHORT).show();

                            prepareDataToList();
                        } else {
                            Toast.makeText(context, "Can't Delete ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(ShowDialog.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });

        builder.show();

    }


    private void menuActionDeleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ยืนยันการลบ");
        builder.setMessage("คุณแน่ใจที่จะลบรายการทั้งหมดหรือไม่");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean resAction = dbAction.deleteAllFavoriteProgram();
                        if (resAction == true) {
                            Toast.makeText(context, "Delete Complete", Toast.LENGTH_LONG).show();

                            prepareDataToList();
                        } else {
                            Toast.makeText(context, "Can't Delete", Toast.LENGTH_LONG).show();
                        }

                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(ShowDialog.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });

        builder.show();

    }

<<<<<<< HEAD
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_favorite_list, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
=======

>>>>>>> 94ca065c8035c58b5f54d3c240db0b713e50f0ab
}
