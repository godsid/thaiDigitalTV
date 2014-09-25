package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


public class SettingTimeList extends Activity {

    private DatabaseAction dbAction;

    String[] time_list = new String[] {"5 นาที","10 นาที","20 นาที","30 นาที","40 นาที","50 นาที","60 นาที"};
    DetailProgram detailProgram;

    String program_name,type_name,channel_name,time_before,time_start;
    int program_id;
    int select_item = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_time_list);

        ArrayAdapter ad=new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,time_list);
        ListView list=(ListView)findViewById(R.id.lv_time);

        list.setAdapter(ad);

        dbAction = new DatabaseAction(this);
        detailProgram = new DetailProgram();

        select_item = detailProgram.getItem_selected();

        program_id = detailProgram.getProg_id(select_item);
        program_name = detailProgram.getProg_name(select_item);
        type_name = detailProgram.getType_name(select_item);
        channel_name = detailProgram.getChan_name();
        time_start = detailProgram.getTime_start(select_item);



        Button bt_ok = (Button)findViewById(R.id.bt_setttime_ok);
        Button bt_cancel = (Button)findViewById(R.id.bt_settime_cancel);
        ImageView iv_back = (ImageView)findViewById(R.id.iv_fav_back);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long idf = dbAction.addFavoriteProgram(program_id,program_name,type_name,channel_name,time_start,20);

                Log.d("run2", "program_id,program_name,type_name,channel_name,time_start,20" + idf);
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdata();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(SettingTimeList.this,MainActivity.class);
                Toast.makeText(getApplicationContext(), "back to main", Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

    }


    public void showdata(){ // ดึงข้อมูลมาแสดง

        SQLiteCursor cur = (SQLiteCursor)dbAction.readAllFavoriteProgram();

        while (cur.isAfterLast() == false) {
            Log.d("run","\nprogram_id: "+cur.getString(0)+"\n"+
                    "program_name: "+cur.getString(1)+"\n"+
                    "type_name: "+cur.getString(2)+"\n"+
                    "channel_name: "+cur.getString(3)+"\n"+
                    "time_start: "+cur.getString(4)+"\n"+
                    "time_before: "+cur.getString(5)+"\n");
            cur.moveToNext();
        }
        cur.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_setting_time_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // return false;
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
