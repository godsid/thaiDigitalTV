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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

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

    private int itemSelect;

    private TVController mTVController = null;
    ArrayList<Device> mDeviceList;
    private ServiceProvider servicePv;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_device_list);
        context = DeviceList.this;
        aq = new AQuery(this);


        ERROR err = ServiceConnector.createServiceProvider(this, new ServiceConnector.IServiceConnectEventListener() {

            @Override
            public void onCreated(ServiceProvider serviceProvider, ServiceConnector.ServiceState serviceState) {
                if (serviceProvider == null)
                    return;
                servicePv = serviceProvider;
                Log.d("run", "Service provider created! " + GlobalVariable.getServiceProvider());
            }

            @Override
            public void onDeleted(ServiceProvider serviceProvider) {
                GlobalVariable.setServiceProvider(null);
                Log.d("run", "Service provider Deleted! " + GlobalVariable.getServiceProvider());
            }
        });


        if (err == ERROR.FRAMEWORK_NOT_INSTALLED) {
            // AllShare Framework Service is not installed.
            Log.d("run", "AllShare Framework Service is not installed.");
        } else if (err == ERROR.INVALID_ARGUMENT) {
            // Input argument is invalid. Check and try again
            Log.d("run", "Input argument is invalid. Check and try again.");
        } else if (err == ERROR.FAIL) {
            Log.d("run", "AllShare Framework Service ERROR.FAIL");
        } else if (err == ERROR.SERVICE_NOT_CONNECTED) {
            // Input argument is invalid. Check and try again
            Log.d("run", "AllShare Framework Service ERROR.SERVICE_NOT_CONNECTED");
        } else {
            // Success on calling the function.
            Log.d("run", "Success on calling the function. : " + err.toString());
        }


        dataCustomDeviceListAdapter = new ArrayList<DataCustomDeviceListAdapter>();
        LV_device_list = (ListView) findViewById(R.id.lv_device_list);
        ImageView IV_exit = (ImageView) findViewById(R.id.iv_exit);

        showDeviceList();

        IV_exit.setOnClickListener(new View.OnClickListener() {
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
                    mTVController = (TVController) mDeviceList.get(itemSelect);
                    GlobalVariable.setCurrentDevice(mDeviceList.get(itemSelect));
                    if (mTVController == null) {
                        Log.d("run", "TV Null");
                        return;
                    } else {
                        mTVController.setEventListener(mEventListener);
                        mTVController.connect();

                        String s = "ส่งช่อง " + GlobalVariable.getChan_name() + "\nแสดงไปยัง " + mDeviceList.get(itemSelect).getName();
                        AlertDialog.Builder builder = GlobalVariable.simpleDialogTemplate(context, "ยืนยัน", s);
                        builder.setPositiveButton("ตกลง",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sendKeyToTV(GlobalVariable.getChan_id());
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
                    Log.d("run", "Exception LV_device_list.setOnItemClickListener : " + e);
                }
            }
        });
    }


    private void showDeviceList() {

        deviceListAdapter = new DeviceListAdapter(this, dataCustomDeviceListAdapter);
        deviceListAdapter.arrayList.clear();
        dataCustomDeviceListAdapter.clear();

        DeviceFinder deviceFinder = servicePv.getDeviceFinder();

        deviceFinder.setDeviceFinderEventListener(Device.DeviceType.DEVICE_TV_CONTROLLER, iDeviceFinderEventListener);
        mDeviceList = deviceFinder.getDevices(Device.DeviceDomain.LOCAL_NETWORK, Device.DeviceType.DEVICE_TV_CONTROLLER);
        deviceFinder.refresh();

        if (mDeviceList != null) {
            for (int i = 0; i < mDeviceList.size(); i++) {

                Uri uri = mDeviceList.get(i).getIcon();

                String deviceName = mDeviceList.get(i).getName();
                String deviceModel = mDeviceList.get(i).getModelName();
                String deviceIp = mDeviceList.get(i).getIPAddress();
                dataCustomDeviceListAdapter.add(new DataCustomDeviceListAdapter(uri, deviceName, deviceModel, deviceIp));

            }
            LV_device_list.setAdapter(deviceListAdapter);


        } else {
            Toast.makeText(context, "ตรวจสอบ: ไม่พบ TV ของคุณในเครือข่าย", Toast.LENGTH_LONG).show();
            finish();
        }

    }


    private void sendKeyToTV(int i) {

        switch (i) {
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
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1, RemoteKey.KEY_3)).start();
                break;
            case 14:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1, RemoteKey.KEY_4)).start();
                break;
            case 15:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1, RemoteKey.KEY_5)).start();
                break;
            case 16:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1, RemoteKey.KEY_6)).start();
                break;
            case 17:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1, RemoteKey.KEY_7)).start();
                break;
            case 18:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1, RemoteKey.KEY_8)).start();
                break;
            case 19:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_1, RemoteKey.KEY_9)).start();
                break;
            case 20:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2, RemoteKey.KEY_0)).start();
                break;
            case 21:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2, RemoteKey.KEY_1)).start();
                break;
            case 22:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2, RemoteKey.KEY_2)).start();
                break;
            case 23:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2, RemoteKey.KEY_3)).start();
                break;
            case 24:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2, RemoteKey.KEY_4)).start();
                break;
            case 25:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2, RemoteKey.KEY_5)).start();
                break;
            case 26:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2, RemoteKey.KEY_6)).start();
                break;
            case 27:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2, RemoteKey.KEY_7)).start();
                break;
            case 28:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2, RemoteKey.KEY_8)).start();
                break;
            case 29:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_2, RemoteKey.KEY_9)).start();
                break;
            case 30:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3, RemoteKey.KEY_0)).start();
                break;
            case 31:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3, RemoteKey.KEY_1)).start();
                break;
            case 32:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3, RemoteKey.KEY_2)).start();
                break;
            case 33:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3, RemoteKey.KEY_3)).start();
                break;
            case 34:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3, RemoteKey.KEY_4)).start();
                break;
            case 35:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3, RemoteKey.KEY_5)).start();
                break;
            case 36:
                new Thread(new ThreadRemoteKey(RemoteKey.KEY_3, RemoteKey.KEY_6)).start();
                break;
            default:
                mTVController.sendRemoteKey(RemoteKey.KEY_1);
                break;
        }

    }


    private final DeviceFinder.IDeviceFinderEventListener iDeviceFinderEventListener = new DeviceFinder.IDeviceFinderEventListener() {


        @Override
        public void onDeviceAdded(Device.DeviceType deviceType, Device device, ERROR error) {
            Log.d("run", "onDeviceAdded");
            showDeviceList();
        }

        @Override
        public void onDeviceRemoved(Device.DeviceType deviceType, Device device, ERROR error) {
            Log.d("run", "onDeviceRemoved");
            showDeviceList();
            if (mDeviceList.size() <= 0)
                finish();
        }
    };


    private TVController.IEventListener mEventListener = new TVController.IEventListener() {
        @Override
        public void onStringChanged(TVController tv, String text, ERROR result) {
            Log.d("run", "IEventListener");
        }

        @Override
        public void onDisconnected(TVController tv, ERROR result) {
            showDeviceList();
            if (mDeviceList.size() <= 0)
                finish();
            Log.d("run", "onDisconnected");
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_device_list, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        if (GlobalVariable.getServiceProvider() != null && isFinishing() == true)
            GlobalVariable.setServiceProvider(null);

        super.onDestroy();
    }


}

