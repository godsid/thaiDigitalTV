package com.webmanagement.thaidigitaltv;

/**
 * Created by SystemDLL on 17/9/2557.
 */
public class DetailProgram{
    private int chan_id,cate_id,prog_id;
    private String chan_name,chan_pic;


    public int getProg_id() {
        return prog_id;
    }

    public void setProg_id(int prog_id) {
        this.prog_id = prog_id;
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
}
