package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
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
import java.util.List;


public class FavoriteList extends Activity {
ImageView IV_ic_back_to_main;

    TextView TV_fav_list_title;
    ListView listView;

    ArrayList<DataCustomListView> arrayListData = new ArrayList<DataCustomListView>();

    ListFavoriteAdapter listFavoriteAdapter ;
    private DatabaseAction dbAction;
    private DetailProgram detailProgram;
    private int itemPosition;

    Typeface TF_font;
    String frontPath = "fonts/RSU_BOLD.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        TF_font = Typeface.createFromAsset(getAssets(), frontPath);
        TV_fav_list_title = (TextView) findViewById(R.id.tv_fav_list_title);
        TV_fav_list_title.setTypeface(TF_font);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);

        dbAction = new DatabaseAction(this);
        detailProgram = new DetailProgram();



        listFavoriteAdapter = new ListFavoriteAdapter(getApplicationContext(),arrayListData);

        listView = (ListView)findViewById(R.id.lv_fav_show);

        ImageView  IV_fav_delete_all = (ImageView)findViewById(R.id.iv_fav_delete_all);

        IV_ic_back_to_main = (ImageView)findViewById(R.id.iv_fav2_back);

        listView.setAdapter(listFavoriteAdapter);
        prepareDataToList();


        IV_ic_back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

        IV_fav_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuActionDeleteAll();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg)   {
                view.startAnimation(animAlpha);
                setItemPosition(position);
                //Toast.makeText(getApplicationContext(), "Prog id "+ list_id, Toast.LENGTH_SHORT).show();
                showActionMenuDialog();
            }

        });

    }

    private void userInputHandler(){

    }

private void prepareDataToList() {

    detailProgram.clearFavArray();
    arrayListData.clear();

    SQLiteCursor cur = (SQLiteCursor)dbAction.readAllFavoriteProgram();
    while (!cur.isAfterLast()) {
        int prog_id = Integer.parseInt(cur.getString(1));
        String prog_name = cur.getString(2);
        String chan_name = cur.getString(4);
        String time_start = cur.getString(5);
        String time_before = cur.getString(6);
        String time_sb = "แจ้งเตือน "+time_before+" นาที ก่อนออกอากาศเวลา "+time_start;

        arrayListData.add(new DataCustomListView(prog_id,prog_name,chan_name,time_sb));
        cur.moveToNext();
    }
    cur.close();

    listFavoriteAdapter.notifyDataSetChanged();
}

    public void setItemPosition(int i) {
        this.itemPosition = i;
    }


    public int getItemPosition() {
        return this.itemPosition;
    }

    private void showActionMenuDialog()
    {
        new AlertDialog.Builder(this)
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ยืนยันการลบ");
        builder.setMessage("คุณแน่ใจที่จะลบรายการ " + detailProgram.getFavProg_name(getItemPosition()));
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean chkDeleted = dbAction.deleteFavoriteProgram(detailProgram.getFavProg_id(getItemPosition()));
                        if (chkDeleted == true) {
                            Toast.makeText(FavoriteList.this, "Delete Complete", Toast.LENGTH_SHORT).show();
                            MainActivity.setStateOK(true);
                            prepareDataToList();
                        }else {
                            Toast.makeText(FavoriteList.this, "Can't Delete ", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ยืนยันการลบ");
        builder.setMessage("คุณแน่ใจที่จะลบรายการทั้งหมดหรือไม่");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean resAction = dbAction.deleteAllFavoriteProgram();
                        if (resAction == true) {
                            Toast.makeText(getApplicationContext(), "Delete Complete", Toast.LENGTH_LONG).show();
                            MainActivity.setStateOK(true);
                            prepareDataToList();
                        } else {
                            Toast.makeText(getApplicationContext(), "Can't Delete", Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_favorite_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
