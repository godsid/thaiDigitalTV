package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.DeviceFinder;
import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.ServiceConnector;
import com.sec.android.allshare.ServiceProvider;
import com.sec.android.allshare.control.TVController;
import com.sec.android.allshare.media.Provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DeviceList extends ListActivity {

    ImageView IC_nav_top_left;
    ListView lv;


    static final String KEY_DEVICE_ID = "KEY_DEVICE_ID";
    final static String KEY_DEVICE_INTERFACE = "KEY_DEVICE_INTERFACE";
    private final static int SELECT_ITEM = 1001;

    private static final int WAITING_PROGRESS = 102;
    private static final int FRAMEWORK_NOT_INSTALLED = 103;

    private String mActionString = "";
    private boolean mIsShowing = false;

    private ComponentName mCallerComponent = null;

    private final LinkedHashMap<String, Intent> mMenuDeviceTypeMap = new LinkedHashMap<String, Intent>();
    private static Device device = null;
    private TVController mTVController = null;
    private String mControlName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        updateDeviceList();
        IC_nav_top_left = (ImageView) findViewById(R.id.ic_nav_top_left);
        lv = (ListView)findViewById(R.id.lv_device_list);

        IC_nav_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mActionString = "allshare.intent.action.REQUEST_CONTROLLER";
        Device.DeviceType targetDeviceType = Device.DeviceType.DEVICE_TV_CONTROLLER;

        setListAdapter(new DeviceListAdapter(this));
        getListView().setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {

                // device is clicked.
                device = (Device) getListView().getItemAtPosition(arg2);

                if (device != null)
                {
                    Config.setDevice(device);

                    if (mActionString.equals("allshare.intent.action.REQUEST_CONTROLLER"))

                    {
                        autoConnect();
                    }
                    else
                    //Return device ID of DMR/Controller
                    {
                        //Return DMR if the caller is not DeviceBrowser Activity

                            Intent intent = getIntent();
                            intent.putExtra(KEY_DEVICE_ID, device.getID());
                            intent.putExtra(KEY_DEVICE_INTERFACE, device.getNIC());
                            setResult(Activity.RESULT_OK, intent);
                            finish();

                    }
                }

            }
        });





        ERROR error = ServiceConnector.createServiceProvider(this, new ServiceConnector.IServiceConnectEventListener() {

            @Override
            public void onCreated(ServiceProvider sprovider, ServiceConnector.ServiceState state) {


                Config.setServiceProvider(sprovider);
            }

            @Override
            public void onDeleted(ServiceProvider sprovider) {

                // fail to connect allshare service.
                if (!DeviceList.this.isFinishing())
                    Toast.makeText(DeviceList.this, getString(R.string.fail_to_connect_allshare_service), Toast.LENGTH_SHORT)
                            .show();
            }
        });
    }

    private void autoConnect()
    {

        if (device != null)
        {
            GlobalVariable.setCurrentDevice(device);
            mTVController = (TVController) GlobalVariable.getCurrentDevice();
            if (mTVController != null)
            {
                mTVController.setEventListener(mEventListener);
                mTVController.setResponseListener(mResponseListener); // this line to fix bugzilla 1921, more info read in bellow comments
                mTVController.connect();
                showDialog(WAITING_PROGRESS);
            }
        }
    }

    TVController.IResponseListener mResponseListener = new TVController.IResponseListener() {

        @Override
        public void onConnectResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onDisconnectResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onOpenWebPageResponseReceived(TVController tvController, String s, ERROR error) {

        }

        @Override
        public void onCloseWebPageResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onGetBrowserURLResponseReceived(TVController tvController, String s, ERROR error) {

        }

        @Override
        public void onGetBrowserModeResponseReceived(TVController tvController, TVController.BrowserMode browserMode, ERROR error) {

        }

        @Override
        public void onGetTVInformationResponseReceived(TVController tvController, TVController.TVInformation tvInformation, ERROR error) {

        }

        @Override
        public void onGoHomePageResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onRefreshWebPageResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onStopWebPageResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onGoNextPageResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onGoPreviousPageResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onBrowserZoomInResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onBrowserZoomOutResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onBrowserZoomDefaultResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onBrowserScrollUpResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onBrowserScrollDownResponseReceived(TVController tvController, ERROR error) {

        }

        @Override
        public void onSetBrowserModeResponseReceived(TVController tvController, TVController.BrowserMode browserMode, ERROR error) {

        }
    };

    private TVController.IEventListener mEventListener = new TVController.IEventListener()
    {
        @Override
        public void onStringChanged(TVController tv, String text, ERROR result)
        {

        }

        @Override
        public void onDisconnected(TVController tv, ERROR result)
        {

            removeDialog(WAITING_PROGRESS);
            Toast.makeText(getApplicationContext(), getString(R.string.tv_disconnected), Toast.LENGTH_SHORT)
                    .show();
        }



    };


    @Override
    protected void onDestroy()
    {

        if (Config.mServiceProvider != null && isFinishing() == true)
            ServiceConnector.deleteServiceProvider(Config.mServiceProvider);

        BitmapCache.getBitmapCache().clear();
        super.onDestroy();
    }

    @Override
    protected void onStart()
    {

        super.onStart();
        Config.reset();
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {

        switch (id)
        {
            case WAITING_PROGRESS:
            {
                ProgressDialog pd = new ProgressDialog(this);
                pd.setMessage(getString(R.string.connecting_to_allsahre_service));
                pd.setCancelable(false);
                pd.setButton(Dialog.BUTTON_NEUTRAL, getString(R.string.cancel), new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        Toast.makeText(getApplicationContext(), getString(R.string.fail_to_connect_allshare_service), Toast.LENGTH_SHORT)
                                .show();
                        removeDialog(WAITING_PROGRESS);
                        finish();
                    }
                });
                return pd;
            }
            case FRAMEWORK_NOT_INSTALLED:
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

                alertDialog.setTitle(R.string.framework_not_installed_title);
                alertDialog.setMessage(R.string.framework_not_installed_message);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton(R.string.framework_not_installed_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                return alertDialog.create();
            }
            default:
                return super.onCreateDialog(id);
        }
    }


    private void updateDeviceList()
    {

        // update device list only if this activity is shown to a user.

            ArrayList<Device> deviceList;

            DeviceFinder deviceFinder = Config.getServiceProvider().getDeviceFinder();

             deviceList = deviceFinder.getDevices(Device.DeviceType.DEVICE_TV_CONTROLLER);


            for (Device device : deviceList)
            {
                Log.d("run",device.getName());
            }




    }

    private boolean isAcceptableDevice(Device device)
    {

        if (mActionString.equals("allshare.intent.action.REQUEST_RECEIVER"))
        {
            if (device instanceof Provider)
            {
                return (((Provider) device).getReceiver() != null);
            }

            return false;
        }



        return true;
    }


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
}
