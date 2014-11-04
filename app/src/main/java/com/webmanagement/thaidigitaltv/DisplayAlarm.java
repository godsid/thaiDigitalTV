package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.sec.android.allshare.Device;
import com.sec.android.allshare.DeviceFinder;
import com.sec.android.allshare.ERROR;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DisplayAlarm extends Activity {
    Ringtone r;
    Vibrator v;
    AQuery aq;
    private DatabaseAction dbAction;
    int prog_id = 0, day_id,repeat_id;
    String time_before;
    String[] arr_day = new String[]{"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};
    int progress1;

    boolean haveTVinNetwork = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_alert_time);
        aq = new AQuery(this);
        dbAction = new DatabaseAction(this);
        //stopVibrator();
        v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);

        v.cancel();
        r.stop();

        TextView tv_title = (TextView) findViewById(R.id.tv_disp_title);
        //Button bt_accept = (Button) findViewById(R.id.bt_disp_accept);
        //Button buttonKS = (Button) findViewById(R.id.bt_kill_service);
        //Button bt_accept_open_app = (Button) findViewById(R.id.bt_disp_accept_and_open);
        //TextView TV_time_before = (TextView) findViewById(R.id.tv_time_before);
        //TextView TV_time_onair = (TextView) findViewById(R.id.tv_time_onair);
        //ImageView iv_channel = (ImageView) findViewById(R.id.iv_disp_chan);

        final ImageView iv0 = (ImageView) findViewById(R.id.iv_center_0);
        final ImageView iv_L1 = (ImageView) findViewById(R.id.iv_slide_left1);
        final ImageView iv_L2 = (ImageView) findViewById(R.id.iv_slide_left2);
        final ImageView iv_L3 = (ImageView) findViewById(R.id.iv_slide_left3);
        final ImageView iv_L4 = (ImageView) findViewById(R.id.iv_slide_left4);
        final ImageView iv_L5 = (ImageView) findViewById(R.id.iv_slide_left5);
        final ImageView iv_L6 = (ImageView) findViewById(R.id.iv_slide_left6);
        final ImageView iv_R1 = (ImageView) findViewById(R.id.iv_slide_right1);
        final ImageView iv_R2 = (ImageView) findViewById(R.id.iv_slide_right2);
        final ImageView iv_R3 = (ImageView) findViewById(R.id.iv_slide_right3);
        final ImageView iv_R4 = (ImageView) findViewById(R.id.iv_slide_right4);
        final ImageView iv_R5 = (ImageView) findViewById(R.id.iv_slide_right5);
        final ImageView iv_R6 = (ImageView) findViewById(R.id.iv_slide_right6);
        final Button bt_acc = (Button) findViewById(R.id.bt_accept);
        final Button bt_device = (Button) findViewById(R.id.bt_device_sh);
        final ImageView iv_center = (ImageView) findViewById(R.id.iv_center_0);
        SeekBar sk1 = (SeekBar) findViewById(R.id.seek_1);
        sk1.setProgress(50);
        //Drawable ii = getResources().getDrawable(R.drawable.scrubber_control_time);
        //sk1.setThumb(ii);

        sk1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress1 = progress;
                //slide left
                if(progress <= 50){
                    iv_R1.setImageResource(R.drawable.arrow_right_1);
                    iv_R2.setImageResource(R.drawable.arrow_right_2);
                    iv_R3.setImageResource(R.drawable.arrow_right_3);
                    iv_R4.setImageResource(R.drawable.arrow_right_4);
                    iv_R5.setImageResource(R.drawable.arrow_right_5);
                    iv_R6.setImageResource(R.drawable.arrow_right_6);
                }
                if(progress <= 45){
                    iv_L1.setImageResource(R.drawable.arrow_left_1);
                }
                if(progress <= 40){
                    iv_L1.setImageResource(R.drawable.arrow_left_1_2);
                    iv_L2.setImageResource(R.drawable.arrow_left_2_2);
                }
                if(progress <= 35){
                    iv_L1.setImageResource(R.drawable.arrow_left_1_2);
                    iv_L2.setImageResource(R.drawable.arrow_left_2_2);
                    iv_L3.setImageResource(R.drawable.arrow_left_3_2);
                }
                if(progress <= 30){
                    iv_L1.setImageResource(R.drawable.arrow_left_1_2);
                    iv_L2.setImageResource(R.drawable.arrow_left_2_2);
                    iv_L3.setImageResource(R.drawable.arrow_left_3_2);
                    iv_L4.setImageResource(R.drawable.arrow_left_4_2);
                }
                if(progress <= 25){
                    iv_L1.setImageResource(R.drawable.arrow_left_1_2);
                    iv_L2.setImageResource(R.drawable.arrow_left_2_2);
                    iv_L3.setImageResource(R.drawable.arrow_left_3_2);
                    iv_L4.setImageResource(R.drawable.arrow_left_4_2);
                    iv_L5.setImageResource(R.drawable.arrow_left_5_2);
                }
                if(progress <= 20){
                    iv_L1.setImageResource(R.drawable.arrow_left_1_2);
                    iv_L2.setImageResource(R.drawable.arrow_left_2_2);
                    iv_L3.setImageResource(R.drawable.arrow_left_3_2);
                    iv_L4.setImageResource(R.drawable.arrow_left_4_2);
                    iv_L5.setImageResource(R.drawable.arrow_left_5_2);
                    iv_L6.setImageResource(R.drawable.arrow_left_6_2);
                }
                if(progress >= 50){
                    iv_L1.setImageResource(R.drawable.arrow_left_1);
                    iv_L2.setImageResource(R.drawable.arrow_left_2);
                    iv_L3.setImageResource(R.drawable.arrow_left_3);
                    iv_L4.setImageResource(R.drawable.arrow_left_4);
                    iv_L5.setImageResource(R.drawable.arrow_left_5);
                    iv_L6.setImageResource(R.drawable.arrow_left_6);
                }
                if(progress >= 58){
                    iv_R1.setImageResource(R.drawable.arrow_right_1_2);
                }
                if(progress >= 62){
                    iv_R1.setImageResource(R.drawable.arrow_right_1_2);
                    iv_R2.setImageResource(R.drawable.arrow_right_2_2);
                }
                if(progress >= 67){
                    iv_R1.setImageResource(R.drawable.arrow_right_1_2);
                    iv_R2.setImageResource(R.drawable.arrow_right_2_2);
                    iv_R3.setImageResource(R.drawable.arrow_right_3_2);
                }
                if(progress >= 72){
                    iv_R1.setImageResource(R.drawable.arrow_right_1_2);
                    iv_R2.setImageResource(R.drawable.arrow_right_2_2);
                    iv_R3.setImageResource(R.drawable.arrow_right_3_2);
                    iv_R4.setImageResource(R.drawable.arrow_right_4_2);
                }
                if(progress >= 77){
                    iv_R1.setImageResource(R.drawable.arrow_right_1_2);
                    iv_R2.setImageResource(R.drawable.arrow_right_2_2);
                    iv_R3.setImageResource(R.drawable.arrow_right_3_2);
                    iv_R4.setImageResource(R.drawable.arrow_right_4_2);
                    iv_R5.setImageResource(R.drawable.arrow_right_5_2);
                }
                if(progress >= 82){
                    iv_R1.setImageResource(R.drawable.arrow_right_1_2);
                    iv_R2.setImageResource(R.drawable.arrow_right_2_2);
                    iv_R3.setImageResource(R.drawable.arrow_right_3_2);
                    iv_R4.setImageResource(R.drawable.arrow_right_4_2);
                    iv_R5.setImageResource(R.drawable.arrow_right_5_2);
                    iv_R6.setImageResource(R.drawable.arrow_right_6_2);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (progress1 <= 10){
                    if (repeat_id != 0)
                        Toast.makeText(getApplicationContext(),alertNextTime(time_before),Toast.LENGTH_LONG).show();
                        r.stop();
                        stopVibrator();
                        finish();
                }
                else if (progress1 >= 90){
                    chkTVinNetwork();
                    if (repeat_id != 0 )
                        Toast.makeText(getApplicationContext(),alertNextTime(time_before),Toast.LENGTH_LONG).show();
                        r.stop();
                        stopVibrator();
                        finish();
                    if (haveTVinNetwork) {
                        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(DisplayAlarm.this,"ตรวจสอบ: ไม่พบ TV ของคุณในเครือข่าย",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    seekBar.setProgress(50);
                }
            }
        });
        /*
        bt_accept_open_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat_id != 0)
                    Toast.makeText(getApplicationContext(),alertNextTime(time_before),Toast.LENGTH_LONG).show();
                r.stop();
                stopVibrator();
                finish();
                PackageManager manager = getPackageManager();
                try {
                    Intent i;
                    i = manager.getLaunchIntentForPackage("com.webmanagement.thaidigitaltv");
                    if (i == null)
                        throw new PackageManager.NameNotFoundException();
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {}
            }
        });
        */

    try {

            Intent intent = getIntent();
            prog_id = Integer.parseInt(intent.getStringExtra("prog_id"));
            String prog_name = intent.getStringExtra("prog_name");
            String chan_name = intent.getStringExtra("chan_name");
            time_before = intent.getStringExtra("time_before");
            String time_start = intent.getStringExtra("time_start");
            day_id = Integer.parseInt(intent.getStringExtra("day_id"));
            repeat_id = Integer.parseInt(intent.getStringExtra("repeat_id"));

            if (repeat_id == 0) {
                dbAction.deleteFavoriteProgram(prog_id);
                Intent intent2 = new Intent(getBaseContext(), ReceiverAlarm.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), prog_id, intent2, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                alarmManager.cancel(pendingIntent);
                Log.d("run", "repeat_id " + repeat_id);


            }

            //tv_title.setText(prog_id + " : " + prog_name + "  " + chan_name);
            tv_title.setText("รายการ" + prog_name + "  " + chan_name);
            //tv_time.setText("แจ้งเตือน " + time_before + " ก่อนถึงเวลา " + time_start);
            //TV_time_before.setText("แจ้งเตือนก่อนเวลาออกอากาศ " + time_before + " นาที");
            //TV_time_onair.setText("เวลาออกอากาศ " + time_start + " นาที");
            //aq.id(iv_channel).image(getIntent().getStringExtra("pic"));

            long[] pattern = {1000, 0, 0, 100, 100, 0, 100, 2000, 0, 0,};
            v.vibrate(pattern, 0);
            r.play();

        } catch (Exception e) {
            Log.d("run", "File DisplayAlarm : " + e);
        }
    }

    private void chkTVinNetwork() {
        ArrayList<Device> mDeviceList;
        DeviceFinder deviceFinder = GlobalVariable.getServiceProvider().getDeviceFinder();
        deviceFinder.setDeviceFinderEventListener(Device.DeviceType.DEVICE_TV_CONTROLLER, iDeviceFinderEventListener);
        deviceFinder.refresh();
        mDeviceList = deviceFinder.getDevices(Device.DeviceDomain.LOCAL_NETWORK, Device.DeviceType.DEVICE_TV_CONTROLLER);

        if (mDeviceList.size() > 0) {
            haveTVinNetwork = true;
        } else {
            haveTVinNetwork = false;
        }
        Log.d("run","mDeviceList "+mDeviceList.size());
    }



    private final DeviceFinder.IDeviceFinderEventListener iDeviceFinderEventListener = new DeviceFinder.IDeviceFinderEventListener() {


        @Override
        public void onDeviceAdded(Device.DeviceType deviceType, Device device, ERROR error) {
            Log.d("run","onDeviceAdded");
            chkTVinNetwork();
        }

        @Override
        public void onDeviceRemoved(Device.DeviceType deviceType,Device device, ERROR error) {
            Log.d("run","onDeviceRemoved");
            chkTVinNetwork();
        }
    };




    private String alertNextTime(String time_before) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, Integer.parseInt(time_before));

        //int date = calendar.get(Calendar.DATE);
        String hm = sdf.format(calendar.getTime());

        Calendar calendarNext = Calendar.getInstance();
        calendarNext.setTimeInMillis(calendar.getTimeInMillis());
        calendarNext.add(Calendar.DAY_OF_WEEK, 7);


        return "แจ้งเตือนอีกครั้งวัน"+arr_day[day_id]+" เวลา"+hm;

    }

    private void stopVibrator() {
        v.cancel();
    }


    @Override
    public void onBackPressed() {
       return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_alert_time, menu);
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
