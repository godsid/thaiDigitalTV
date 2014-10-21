package com.webmanagement.thaidigitaltv;

import android.util.Log;

import com.sec.android.allshare.Device;

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

    public static ArrayList<String> arrDelOrAdd = new ArrayList<String>();

    private static Device mDevice = null;

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

    public static void clearArrDelOrAdd() {
        arrDelOrAdd.clear();

    }


    public static void setCurrentDevice( Device device )
    {
        mDevice = device;
    }

    public static Device getCurrentDevice()
    {
        return mDevice;
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

    public static String getArrDelOrAdd(int i) {

        return arrDelOrAdd.get(i);
    }

    public static void addArrDelOrAdd(String j) {
        arrDelOrAdd.add(j);
    }

    public static void printArrDelOrAdd() {
        Log.d("run", "== Size ==" + arrDelOrAdd.size());
        for (int i = 0; i < arrDelOrAdd.size(); i++) {
            Log.d("run", i + " " + arrDelOrAdd.get(i));
        }
    }

  
  
  /*

  public static String getFavProg_name(int i) {
    return this.arrFav_Pro_name.get(i);
  }

  public static void setFavProg_name(String j) {
    this.arrFav_Pro_name.add(j);
  }

  public static int getDay_id() {
    return this.item_day_id;
  }

  public static void setDay_id(int j) {
    this.item_day_id = j;
  }

  public static int getFavProg_id(int i) {
    return this.arrFav_Pro_id.get(i);
  }

  public static void setFavProg_id(int j) {
    this.arrFav_Pro_id.add(j);
  }

  public static int getProg_id(int i) {
    try {
      // Log.d("run",i+" : "+this.arrPro_id.get(i)+" : "+this.arrPro_id.size()+" try ");
      return this.arrPro_id.get(i);
    } catch (Exception e) {
      //Log.d("run","catch getProg_id "+i+" : "+this.arrPro_id.get(i)+" : "+this.arrPro_id.size());
      return this.arrPro_id.get(i);
    }

  }


  public static void setProg_id(int j) {
    this.arrPro_id.add(j);
  }


  public static String getProg_name(int i) {
    return this.arrPro_name.get(i);
  }

  public static void setProg_name(String j) {
    this.arrPro_name.add(j);
  }



  public static String getProg_timestart(int i) {
    return this.arrProg_timestart.get(i);
  }

  public static void setProg_timestart(String j) {
    this.arrProg_timestart.add(j);
  }

  public static int getChan_id() {
    return chan_id;
  }

  public static void setChan_id(int chan_id) {
    this.chan_id = chan_id;
  }

  public static String getChan_name() {
    return chan_name;
  }

  public static void setChan_name(String chan_name) {
    this.chan_name = chan_name;
  }

  public static String getChan_pic() {
    return chan_pic;
  }

  public static void setChan_pic(String chan_pic) {
    this.chan_pic = chan_pic;
  }


  public static int getItem_selected() {
    return item_selected;
  }

  public static void seItem_selected(int item_selected) {
    this.item_selected = item_selected;
  }
*/

}

