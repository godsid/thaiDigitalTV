package com.webmanagement.thaidigitaltv;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 17/9/2557.
 */
public class Store_Variable {
    public static int chan_id;
    public static String chan_name, chan_pic;

    public static ArrayList<Integer> arr_Pro_id = new ArrayList<Integer>();
    public static ArrayList<String> arr_Pro_name = new ArrayList<String>();
    public static ArrayList<String> arr_Time_start = new ArrayList<String>();
    public static ArrayList<String> arr_Time_end = new ArrayList<String>();

    public static ArrayList<Integer> arrFav_Pro_id = new ArrayList<Integer>();
    public static ArrayList<String> arrFav_Pro_name = new ArrayList<String>();

    public static int item_selected;
    public static int item_day_id;

    public static ArrayList<String> arrDelOrAdd = new ArrayList<String>();

    public void clearAllArray() {
        arr_Pro_id.clear();
        arr_Pro_name.clear();
        arr_Time_start.clear();
        arr_Time_end.clear();

    }

    public void clearFavArray() {
        arrFav_Pro_id.clear();
        arrFav_Pro_name.clear();

    }


    public String getFavProg_name(int i) {
        return this.arrFav_Pro_name.get(i);
    }

    public void setFavProg_name(String j) {
        this.arrFav_Pro_name.add(j);
    }

    public int getDay_id() {
        return this.item_day_id;
    }

    public void setDay_id(int j) {
        this.item_day_id = j;
    }

    public int getFavProg_id(int i) {
        return this.arrFav_Pro_id.get(i);
    }

    public void setFavProg_id(int j) {
        this.arrFav_Pro_id.add(j);
    }

    public int getProg_id(int i) {
        try {
            // Log.d("run",i+" : "+this.arr_Pro_id.get(i)+" : "+this.arr_Pro_id.size()+" try ");
            return this.arr_Pro_id.get(i);
        } catch (Exception e) {
            //Log.d("run","catch getProg_id "+i+" : "+this.arr_Pro_id.get(i)+" : "+this.arr_Pro_id.size());
            return this.arr_Pro_id.get(i);
        }

    }


    public void setProg_id(int j) {
        this.arr_Pro_id.add(j);
    }


    public String getProg_name(int i) {
        return this.arr_Pro_name.get(i);
    }

    public void setProg_name(String j) {
        this.arr_Pro_name.add(j);
    }



    public String getTime_start(int i) {
        return this.arr_Time_start.get(i);
    }

    public void setTime_start(String j) {
        this.arr_Time_start.add(j);
    }

    public int getChan_id() {
        return chan_id;
    }

    public void setChan_id(int chan_id) {
        this.chan_id = chan_id;
    }

    public String getChan_name() {
        return chan_name;
    }

    public void setChan_name(String chan_name) {
        this.chan_name = chan_name;
    }

    public String getChan_pic() {
        return chan_pic;
    }

    public void setChan_pic(String chan_pic) {
        this.chan_pic = chan_pic;
    }


    public int getItem_selected() {
        return item_selected;
    }

    public void seItem_selected(int item_selected) {
        this.item_selected = item_selected;
    }


}

