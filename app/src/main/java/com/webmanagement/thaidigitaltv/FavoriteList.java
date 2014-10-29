package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FavoriteList extends Activity {

    View rootView;
    Context context;

    TextView TV_fav_list_title;
    ListView listView;

    ArrayList<DataCustomFavoriteList> arrayListData = new ArrayList<DataCustomFavoriteList>();

    FavoriteListAdapter favoriteListAdapter;
    private DatabaseAction dbAction;
    private int itemPosition;

    String[] arr_day = new String[]{"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};

    public FavoriteList(View rootView) {
        this.rootView = rootView;
        this.context = rootView.getContext();

        TV_fav_list_title = (TextView) rootView.findViewById(R.id.tv_fav_list_title);

        final Animation animAlpha = AnimationUtils.loadAnimation(context, R.anim.anim_alpha);

        dbAction = new DatabaseAction(context);



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

        GlobalVariable.clearFavArray();
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
            //String time_sb = "แจ้งเตือน " + time_before + " นาที ก่อนออกอากาศเวลา " + time_start;
            String time_sb = "ออกอากาศวัน"+ arr_day[day_id] +" เวลา " + time_start + " น.";
            String ln3 = chan_name + "  " + st_repeat;
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
                .show()
                .setCanceledOnTouchOutside(true);

    }


    private void menuActionEdit() {
        //Intent intent3 = new Intent(getApplicationContext(), SettingAlert.class);
        //startActivity(intent3);
    }

    private void cancelAlarm(int prog_id) {
        Intent intent2 = new Intent(context, ReceiverAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, prog_id, intent2, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

    }

    private void cancelAllAlarm() {
        dbAction = new DatabaseAction(context);
        SQLiteCursor cur = (SQLiteCursor) dbAction.readAllFavoriteProgram();

        while (!cur.isAfterLast()) {
            int prog_id = Integer.parseInt(cur.getString(1));
            Intent intent2 = new Intent(context, ReceiverAlarm.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, prog_id, intent2, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);

            cur.moveToNext();
        }
        cur.close();

    }

    private void menuActionDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ยืนยันการลบ");
        builder.setMessage("คุณแน่ใจที่จะลบรายการ " + GlobalVariable.getArrFav_Prog_name(getItemPosition()));
        builder.setPositiveButton("ใช่",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean chkDeleted = dbAction.deleteFavoriteProgram(GlobalVariable.getArrFav_Prog_id(getItemPosition()));
                        if (chkDeleted) {
                            cancelAlarm(GlobalVariable.getArrFav_Prog_id(getItemPosition()));
                            Toast.makeText(FavoriteList.this,"สำเร็จ : ลบรายการเรียบร้อย", Toast.LENGTH_SHORT).show();

                            prepareDataToList();
                        } else {
                            Toast.makeText(FavoriteList.this,"ผิดพลาด : ไม่สามารถรายการได้",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setNegativeButton("ไม่",
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
        builder.setPositiveButton("ใช่",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int rowCount = dbAction.countFavoriteProgram();
                        if (rowCount > 0) {
                            boolean resAction = dbAction.deleteAllFavoriteProgram();
                            if (resAction == true) {
                                Toast.makeText(context, "สำเร็จ : ลบรายการเรียบร้อย", Toast.LENGTH_LONG).show();
                                cancelAllAlarm();
                                prepareDataToList();
                            } else {
                                Toast.makeText(context, "ผิดพลาด : ไม่สามารถรายการได้", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "ไม่มีรายการที่ต้องการลบ", Toast.LENGTH_LONG).show();
                        }

                    }
                });
        builder.setNegativeButton("ไม่",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(ShowDialog.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });

        builder.show();

    }


}
