package com.webmanagement.thaidigitaltv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sec.android.allshare.control.TVController;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 3/11/2557.
 */
public class DialogDeviceList {
    ArrayList<DataCustomDeviceListAdapter> dataCustomDeviceListAdapter = new ArrayList<DataCustomDeviceListAdapter>();
    DeviceListAdapter deviceListAdapter;
    private int itemSelectTV;
    private TVController mTVController = null;
    Context context;

    public DialogDeviceList(Context context2) {
        this.context = context2;
        showDialogDeviceList();
    }

    private void showDialogDeviceList() {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialog_device_list);
        ListView LV_device_list = (ListView) dialog1.findViewById(R.id.lv_device_list);

        prepareDataToDeviceList(LV_device_list);
        dialog1.show();
        LV_device_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    itemSelectTV = position;
                    mTVController = (TVController) GlobalVariable.arrDeviceList.get(itemSelectTV);
                    GlobalVariable.setCurrentDevice(GlobalVariable.arrDeviceList.get(itemSelectTV));
                    if (mTVController == null) {
                        Log.d("run", "TV Null");
                        return;
                    } else {

                        mTVController.connect();
                        String s = "เปิดช่อง " + GlobalVariable.getChan_name() + "\nไปยัง " + GlobalVariable.arrDeviceList.get(itemSelectTV).getName();
                        AlertDialog.Builder builder = GlobalVariable.simpleDialogTemplate(context, "ยืนยัน", s);
                        builder.setPositiveButton("ตกลง",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sendKeyToTV(GlobalVariable.getChan_id());
                                        Toast.makeText(context, "เปิดช่องเรียบร้อย", Toast.LENGTH_SHORT).show();
                                        dialog1.dismiss();
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

                } catch (Exception e) {
                    Log.d("run", "Exception LV_device_list.setOnItemClickListener : " + e);
                }
            }
        });


    }


    private void prepareDataToDeviceList(ListView lv) {
        deviceListAdapter = new DeviceListAdapter(context, dataCustomDeviceListAdapter);
        deviceListAdapter.arrayList.clear();
        dataCustomDeviceListAdapter.clear();

        if (GlobalVariable.arrDeviceList.size() != 0) {
            for (int i = 0; i < GlobalVariable.arrDeviceList.size(); i++) {

                Uri uri = GlobalVariable.arrDeviceList.get(i).getIcon();

                String deviceName = GlobalVariable.arrDeviceList.get(i).getName();
                String deviceModel = GlobalVariable.arrDeviceList.get(i).getModelName();
                String deviceIp = GlobalVariable.arrDeviceList.get(i).getIPAddress();
                dataCustomDeviceListAdapter.add(new DataCustomDeviceListAdapter(uri, deviceName, deviceModel, deviceIp));

            }
            lv.setAdapter(deviceListAdapter);

        }
    }


    private void sendKeyToTV(int i) {

        switch (i) {
            case 1:
                mTVController.sendRemoteKey(TVController.RemoteKey.KEY_1);
                break;
            case 2:
                mTVController.sendRemoteKey(TVController.RemoteKey.KEY_2);
                break;
            case 3:
                mTVController.sendRemoteKey(TVController.RemoteKey.KEY_3);
                break;
            case 4:
                mTVController.sendRemoteKey(TVController.RemoteKey.KEY_4);
                break;
            case 13:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_1, TVController.RemoteKey.KEY_3)).start();
                break;
            case 14:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_1, TVController.RemoteKey.KEY_4)).start();
                break;
            case 15:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_1, TVController.RemoteKey.KEY_5)).start();
                break;
            case 16:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_1, TVController.RemoteKey.KEY_6)).start();
                break;
            case 17:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_1, TVController.RemoteKey.KEY_7)).start();
                break;
            case 18:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_1, TVController.RemoteKey.KEY_8)).start();
                break;
            case 19:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_1, TVController.RemoteKey.KEY_9)).start();
                break;
            case 20:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_2, TVController.RemoteKey.KEY_0)).start();
                break;
            case 21:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_2, TVController.RemoteKey.KEY_1)).start();
                break;
            case 22:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_2, TVController.RemoteKey.KEY_2)).start();
                break;
            case 23:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_2, TVController.RemoteKey.KEY_3)).start();
                break;
            case 24:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_2, TVController.RemoteKey.KEY_4)).start();
                break;
            case 25:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_2, TVController.RemoteKey.KEY_5)).start();
                break;
            case 26:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_2, TVController.RemoteKey.KEY_6)).start();
                break;
            case 27:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_2, TVController.RemoteKey.KEY_7)).start();
                break;
            case 28:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_2, TVController.RemoteKey.KEY_8)).start();
                break;
            case 29:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_2, TVController.RemoteKey.KEY_9)).start();
                break;
            case 30:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_3, TVController.RemoteKey.KEY_0)).start();
                break;
            case 31:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_3, TVController.RemoteKey.KEY_1)).start();
                break;
            case 32:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_3, TVController.RemoteKey.KEY_2)).start();
                break;
            case 33:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_3, TVController.RemoteKey.KEY_3)).start();
                break;
            case 34:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_3, TVController.RemoteKey.KEY_4)).start();
                break;
            case 35:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_3, TVController.RemoteKey.KEY_5)).start();
                break;
            case 36:
                new Thread(new ThreadRemoteKey(TVController.RemoteKey.KEY_3, TVController.RemoteKey.KEY_6)).start();
                break;
            default:
                mTVController.sendRemoteKey(TVController.RemoteKey.KEY_1);
                break;
        }

    }

}
