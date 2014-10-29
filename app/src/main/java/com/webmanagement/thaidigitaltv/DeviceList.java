package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.androidquery.AQuery;
import com.sec.android.allshare.Device;
import com.sec.android.allshare.DeviceFinder;
import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.ServiceConnector;
import com.sec.android.allshare.ServiceProvider;
import com.sec.android.allshare.control.TVController;
import com.sec.android.allshare.control.TVController.RemoteKey;

import org.json.JSONObject;

import java.util.ArrayList;

public class DeviceList extends Activity {

    AQuery aq;

    ListView LV_device_list;

    ArrayList<DataCustomDeviceListAdapter> dataCustomDeviceListAdapter;
    DeviceListAdapter deviceListAdapter;

    private  int itemSelect;

    private TVController mTVController = null;
    ArrayList<Device> mDeviceList;
    ProgressDialog progressDialog;

Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        context = DeviceList.this;
        aq = new AQuery(this);

        dataCustomDeviceListAdapter = new ArrayList<DataCustomDeviceListAdapter>();

        LV_device_list = (ListView)findViewById(R.id.lv_device_list);
        ImageView ic_nav_top_left = (ImageView)findViewById(R.id.ic_nav_top_left);

        showDeviceList();



        ic_nav_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LV_device_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    itemSelect = position;
                    mTVController = (TVController)mDeviceList.get(itemSelect);
                    GlobalVariable.setCurrentDevice(mDeviceList.get(itemSelect));
                    if(mTVController == null) {
                        Log.d("run","TV Null");
                        return;
                    } else {
                        mTVController.setEventListener(mEventListener);

                        mTVController.connect();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("ยืนยัน");
                        builder.setMessage("ส่งช่อง " + GlobalVariable.getChan_name()+"\nแสดงไปยัง "+mDeviceList.get(itemSelect).getName());
                        builder.setPositiveButton("ตกลง",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sendKeyToTV( GlobalVariable.getChan_id());
                                    }
                                });
                        builder.setNegativeButton("ไม่",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Toast.makeText(ShowDialog.this, "Fail", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        builder.show();

                        Log.d("run", mDeviceList.get(itemSelect).getIPAddress() + "");
                    }

                } catch (Exception e) {
                    Log.d("run","Exception LV_device_list.setOnItemClickListener : "+e);
                }
            }
        });




    }




    private void showDeviceList() {

        deviceListAdapter = new DeviceListAdapter(this,dataCustomDeviceListAdapter);
        deviceListAdapter.arrayList.clear();
        dataCustomDeviceListAdapter.clear();

        DeviceFinder deviceFinder = GlobalVariable.getServiceProvider().getDeviceFinder();

        deviceFinder.setDeviceFinderEventListener(Device.DeviceType.DEVICE_TV_CONTROLLER, iDeviceFinderEventListener);
        mDeviceList = deviceFinder.getDevices(Device.DeviceDomain.LOCAL_NETWORK, Device.DeviceType.DEVICE_TV_CONTROLLER);
        deviceFinder.refresh();

        if (mDeviceList != null) {
            for (int i = 0; i < mDeviceList.size(); i++) {

                Uri uri = mDeviceList.get(i).getIcon();

                String deviceName =  mDeviceList.get(i).getName();
                String deviceModel =  mDeviceList.get(i).getModelName();
                String deviceIp =  mDeviceList.get(i).getIPAddress();
                dataCustomDeviceListAdapter.add(new DataCustomDeviceListAdapter(uri, deviceName, deviceModel, deviceIp));

            }
           LV_device_list.setAdapter(deviceListAdapter);


        }

    }



    private ProgressDialog showDialogProgress() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("กำลังโหลดข้อมูล TV...");
        progressDialog.setTitle("กรุณารอสักครู่");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setButton(Dialog.BUTTON_NEUTRAL, "ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
            }
        });
        progressDialog.show();
        return progressDialog;
    }



    private void sendKeyToTV(int i) {

        switch(i) {
            case 1:
                mTVController.sendRemoteKey(RemoteKey.KEY_1);
                break;
            case 2:
                mTVController.sendRemoteKey(RemoteKey.KEY_2);
                break;
            case 3:
                mTVController.sendRemoteKey(RemoteKey.KEY_3);
                break;
            case 4:
                mTVController.sendRemoteKey(RemoteKey.KEY_4);
                break;
            case 13:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1,RemoteKey.KEY_3)).start();
                break;
            case 14:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1,RemoteKey.KEY_4)).start();
                break;
            case 15:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1,RemoteKey.KEY_5)).start();
                break;
            case 16:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1,RemoteKey.KEY_6)).start();
                break;
            case 17:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1,RemoteKey.KEY_7)).start();
                break;
            case 18:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1,RemoteKey.KEY_8)).start();
                break;
            case 19:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1,RemoteKey.KEY_9)).start();
                break;
            case 20:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2,RemoteKey.KEY_0)).start();
                break;
            case 21:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2,RemoteKey.KEY_1)).start();
                break;
            case 22:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2,RemoteKey.KEY_2)).start();
                break;
            case 23:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2,RemoteKey.KEY_3)).start();
                break;
            case 24:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2,RemoteKey.KEY_4)).start();
                break;
            case 25:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2,RemoteKey.KEY_5)).start();
                break;
            case 26:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2,RemoteKey.KEY_6)).start();
                break;
            case 27:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2,RemoteKey.KEY_7)).start();
                break;
            case 28:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2,RemoteKey.KEY_8)).start();
                break;
            case 29:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2,RemoteKey.KEY_9)).start();
                break;
            case 30:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3,RemoteKey.KEY_0)).start();
                break;
            case 31:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3,RemoteKey.KEY_1)).start();
                break;
            case 32:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3,RemoteKey.KEY_2)).start();
                break;
            case 33:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3,RemoteKey.KEY_3)).start();
                break;
            case 34:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3,RemoteKey.KEY_4)).start();
                break;
            case 35:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3,RemoteKey.KEY_5)).start();
                break;
            case 36:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3,RemoteKey.KEY_6)).start();
                break;
            default: mTVController.sendRemoteKey(RemoteKey.KEY_1);
                break;
        }

    }


    private final DeviceFinder.IDeviceFinderEventListener iDeviceFinderEventListener = new DeviceFinder.IDeviceFinderEventListener() {


        @Override
        public void onDeviceAdded(Device.DeviceType deviceType, Device device, ERROR error) {
            Log.d("run","onDeviceAdded");
            showDeviceList();
        }

        @Override
        public void onDeviceRemoved(Device.DeviceType deviceType,Device device, ERROR error) {
            Log.d("run","onDeviceRemoved");
            showDeviceList();
            if (mDeviceList.size() <= 0)
                finish();
        }
    };


    private TVController.IEventListener mEventListener = new TVController.IEventListener()
    {
        @Override
        public void onStringChanged(TVController tv, String text, ERROR result)
        {
            Log.d("run","IEventListener");
        }

        @Override
        public void onDisconnected(TVController tv, ERROR result)
        {
            showDeviceList();
            if (mDeviceList.size() <= 0)
                finish();
            Log.d("run","onDisconnected");
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_device_list, menu);
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

    @Override
    protected void onDestroy() {
       // if (mServiceProvider != null)
            //ServiceConnector.deleteServiceProvider(mServiceProvider);
        super.onDestroy();
    }


}

