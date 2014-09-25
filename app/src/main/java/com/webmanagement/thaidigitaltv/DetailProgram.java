package com.webmanagement.thaidigitaltv;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 17/9/2557.
 */
public class DetailProgram{
    private static int chan_id,cate_id;
    private static String chan_name,chan_pic;

    private static ArrayList<Integer> arr_Pro_id = new ArrayList<Integer>();
    private static ArrayList<String> arr_Pro_name = new ArrayList<String>();
    private static ArrayList<String> arr_Type_name = new ArrayList<String>();
    private static ArrayList<String> arr_Time_start = new ArrayList<String>();
    private static ArrayList<String> arr_Time_end = new ArrayList<String>();

    private static int item_selected;


    public int getProg_id(int i) {
        return this.arr_Pro_id.get(i);
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

    public String getType_name(int i) {
        return this.arr_Type_name.get(i);
    }
    public void setType_name(String j) {
        this.arr_Type_name.add(j);
    }


    public String getTime_start(int i) {
        return this.arr_Time_start.get(i);
    }
    public void setTime_start(String j) {
        this.arr_Time_start.add(j);
    }

    public String getTime_end(int i) {
        return this.arr_Time_end.get(i);
    }
    public void setTime_end(String j) {
        this.arr_Time_end.add(j);
    }
    public int getChan_id() {
        return chan_id;
    }
    public void setChan_id(int chan_id) {
        this.chan_id = chan_id;
    }

    public int getCate_id() {
        return cate_id;
    }
    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
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

