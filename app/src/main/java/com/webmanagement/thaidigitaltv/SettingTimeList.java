package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class SettingTimeList extends Activity {
String[] time_list = new String[] {"5 นาที","10 นาที","20 นาที","30 นาที","40 นาที","50 นาที","60 นาที"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_time_list);

        ArrayAdapter ad=new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,time_list);
        ListView list=(ListView)findViewById(R.id.lv_time);
        list.setAdapter(ad);

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
