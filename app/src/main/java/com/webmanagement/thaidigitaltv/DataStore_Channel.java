package com.webmanagement.thaidigitaltv;

/**
 * Created by SystemDLL on 8/10/2557.
 */
public class DataStore_Channel {
    private String chan_name, chan_pic;
    private int chan_id;

    public int getFr_cate_id() {
        return fr_cate_id;
    }

    public void setFr_cate_id(int fr_cate_id) {
        this.fr_cate_id = fr_cate_id;
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

    public int getChan_id() {
        return chan_id;
    }

    public void setChan_id(int chan_id) {
        this.chan_id = chan_id;
    }

    int fr_cate_id;
}
