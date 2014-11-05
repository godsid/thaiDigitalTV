package com.webmanagement.thaidigitaltv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.ServiceConnector;
import com.sec.android.allshare.ServiceProvider;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 17/9/2557.
 */
public class GlobalVariable {

    public static ArrayList<Integer> arrProg_id = new ArrayList<Integer>();
    public static ArrayList<String> arrProg_name = new ArrayList<String>();
    public static ArrayList<String> arrProg_timestart = new ArrayList<String>();
    public static ArrayList<String> arrProg_timeend = new ArrayList<String>();

    public static ArrayList<Integer> arrFav_Prog_id = new ArrayList<Integer>();
    public static ArrayList<String> arrFav_Prog_name = new ArrayList<String>();

    public static int Item_selected;
    public static int Day_id;

    public static int Chan_id;
    public static String Chan_name;
    public static String Chan_pic;

    public static int Prog_id;
    public static String Prog_name;
    public static String Prog_timestart;
    public static String Prog_timeend;


    private static Device mDevice = null;
    private static ServiceProvider mServiceProvider = null;
    public static ArrayList<Device> arrDeviceList = null;

    private static boolean confDialog;

    public static void clearAllArray() {
        arrProg_id.clear();
        arrProg_name.clear();
        arrProg_timestart.clear();
        arrProg_timeend.clear();

    }

    public static void clearFavArray() {
        arrFav_Prog_id.clear();
        arrFav_Prog_name.clear();

    }


    public static void setCurrentDevice( Device device )
    {
        mDevice = device;
    }

    public static Device getCurrentDevice()
    {
        return mDevice;
    }

    public static void setServiceProvider( ServiceProvider serviceProvider )
    {

        if (serviceProvider == null)
            ServiceConnector.deleteServiceProvider(mServiceProvider);
            mServiceProvider = serviceProvider;
       // Log.d("run","setServiceProvider "+mServiceProvider);

    }

    public static ServiceProvider getServiceProvider()
    {

        return mServiceProvider;
    }


    public static int getItem_selected() {
        return Item_selected;
    }

    public static void setItem_selected(int item_selected) {
        Item_selected = item_selected;
    }


    public static int getDay_id() {
        return Day_id;
    }

    public static void setDay_id(int day_id) {
        Day_id = day_id;
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

    public static String getChan_pic() {
        return Chan_pic;
    }

    public static void setChan_pic(String chan_pic) {
        Chan_pic = chan_pic;
    }

    public static int getProg_id() {
        return Prog_id;
    }

    public static void setProg_id(int prog_id) {
        Prog_id = prog_id;
    }

    public static String getProg_name() {
        return Prog_name;
    }

    public static void setProg_name(String prog_name) {
        Prog_name = prog_name;
    }

    public static String getProg_timestart() {
        return Prog_timestart;
    }

    public static void setProg_timestart(String prog_timestart) {
        Prog_timestart = prog_timestart;
    }

    public static String getProg_timeend() {
        return Prog_timeend;
    }

    public static void setProg_timeend(String prog_timeend) {
        Prog_timeend = prog_timeend;
    }


    public static int getArrProg_id(int i) {
        return arrProg_id.get(i);
    }

    public static void addArrProg_id(int j) {
        arrProg_id.add(j);
    }

    public static String getArrProg_name(int i) {
        return arrProg_name.get(i);
    }

    public static void addArrProg_name(String j) {
        arrProg_name.add(j);
    }

    public static String getArrProg_timestart(int i) {
        return arrProg_timestart.get(i);
    }

    public static void addArrProg_timestart(String j) {
        arrProg_timestart.add(j);
    }

    public static String getArrProg_timeend(int i) {
        return arrProg_timeend.get(i);
    }

    public static void addArrProg_timeend(String j) {
        arrProg_timeend.add(j);
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

    public static AlertDialog.Builder simpleDialogTemplate(Context c,String t,String b) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(null);
        View view = LayoutInflater.from(c).inflate(R.layout.dialog_template,null);
        TextView title = (TextView)view.findViewById(R.id.title);
        TextView body = (TextView)view.findViewById(R.id.body);
        title.setText(t);
        body.setText(b);
        builder.setView(view);
        return  builder;
    }


    public static boolean userChoosed(boolean b){

        if(b)
            //YOUR CODE FOR YES HERE
           return true;
        else
            return false;

    }



}

