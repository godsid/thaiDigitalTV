package com.webmanagement.thaidigitaltv;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.ServiceConnector;
import com.sec.android.allshare.ServiceProvider;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 17/9/2557.
 */
public class GlobalVariable {


    private static ArrayList<Integer> arrFav_Prog_id = new ArrayList<Integer>();
    private static ArrayList<String> arrFav_Prog_name = new ArrayList<String>();

    private static int Chan_id;
    private static String Chan_name;

    private static int Prog_id;

    private static Device mDevice = null;
    private static ServiceProvider mServiceProvider = null;
    public static ArrayList<Device> arrDeviceList = null;

    private static boolean isOnlineMode;

    private static int MAX_DAY_OF_MONTH;

    private static boolean HaveTVNetwork = false;





    public static boolean isHaveTVNetwork() {
        return HaveTVNetwork;
    }

    public static void setHaveTVNetwork(boolean haveTVNetwork) {
        HaveTVNetwork = haveTVNetwork;
    }

    public static int getMAX_DAY_OF_MONTH() {
        return MAX_DAY_OF_MONTH;
    }

    public static void setMAX_DAY_OF_MONTH(int MAX_DAY_OF_MONTH) {
        GlobalVariable.MAX_DAY_OF_MONTH = MAX_DAY_OF_MONTH;
    }


    public static void clearFavArray() {
        arrFav_Prog_id.clear();
        arrFav_Prog_name.clear();

    }


    public static void setCurrentDevice(Device device) {
        mDevice = device;
    }

    public static Device getCurrentDevice() {
        return mDevice;
    }

    public static void setServiceProvider(ServiceProvider serviceProvider) {

        if (serviceProvider == null)
            ServiceConnector.deleteServiceProvider(mServiceProvider);
        mServiceProvider = serviceProvider;
        // Log.d("run","setServiceProvider "+mServiceProvider);

    }

    public static ServiceProvider getServiceProvider() {

        return mServiceProvider;
    }


    public static boolean isOnlineMode() {
        return isOnlineMode;
    }

    public static void setOnlineMode(boolean isOnlineMode) {
        GlobalVariable.isOnlineMode = isOnlineMode;
    }


    public static int getChan_id() {
        return Chan_id;
    }

    public static void setChan_id(int chan_id) {
        Chan_id = chan_id;
    }

    public static String getChan_name() {
        return Chan_name;
    }

    public static void setChan_name(String chan_name) {
        Chan_name = chan_name;
    }

    public static int getProg_id() {
        return Prog_id;
    }

    public static void setProg_id(int prog_id) {
        Prog_id = prog_id;
    }

    public static int getArrFav_Prog_id(int i) {
        return arrFav_Prog_id.get(i);
    }

    public static void addArrFav_Prog_id(int j) {
        arrFav_Prog_id.add(j);
    }

    public static String getArrFav_Prog_name(int i) {
        return arrFav_Prog_name.get(i);
    }

    public static void addArrFav_Prog_name(String j) {
        arrFav_Prog_name.add(j);
    }

    public static AlertDialog.Builder simpleDialogTemplate(Context c, String t, String b) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c,R.style.Run_ButtonDialog);
        builder.setTitle(null);
        View view = LayoutInflater.from(c).inflate(R.layout.dialog_template, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView body = (TextView) view.findViewById(R.id.body);
        title.setText(t);
        body.setText(b);
        builder.setView(view);
        return builder;
    }



}

