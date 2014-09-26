package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FavoriteList extends Activity {
ImageView IV_ic_back_to_main;

    TextView TV_fav_delete_all;

    ArrayList<DataCustomListView> arrayListData = new ArrayList<DataCustomListView>();

    ListFavoriteAdapter listFavoriteAdapter ;
    private DatabaseAction dbAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        dbAction = new DatabaseAction(this);

        SQLiteCursor cur = (SQLiteCursor)dbAction.readAllFavoriteProgram();

        listFavoriteAdapter = new ListFavoriteAdapter(getApplicationContext(),arrayListData);

        ListView listView = (ListView)findViewById(R.id.lv_fav_show);

        IV_ic_back_to_main = (ImageView)findViewById(R.id.ic_back_to_main);

        TextView  TV_fav_delete_all = (TextView)findViewById(R.id.tv_fav_delete_all);

        listView.setAdapter(listFavoriteAdapter);

        while (cur.isAfterLast() == false) {
            int list_id = Integer.parseInt(cur.getString(0));
            String prog_name = cur.getString(2);
            String chan_name = cur.getString(4);
            String time_start = cur.getString(5);
            String time_before = cur.getString(6);
            String time_sb = "แจ้งเตือน "+time_before+" นาที ก่อนออกอากาศ "+time_start;

            arrayListData.add(new DataCustomListView(list_id,prog_name,chan_name,time_sb));
            cur.moveToNext();
        }
        cur.close();

        listFavoriteAdapter.notifyDataSetChanged();

        IV_ic_back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

        TV_fav_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean resAction = dbAction.deleteAllFavoriteProgram();
                if (resAction == true)
                    Toast.makeText(getApplicationContext(),"Delete Complete",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Can't Delete",Toast.LENGTH_SHORT).show();

            }
        });
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
