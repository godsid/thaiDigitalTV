package com.webmanagement.thaidigitaltv;

/**
 * Created by SystemDLL on 8/10/2557.
 */
public class DataStore_Program {
    private int prog_id,fr_channel_id,fr_day_id,fr_type_id;
    String prog_name,prog_timestart,prog_timeend;

    public int getProg_id() {
        return prog_id;
    }

    public void setProg_id(int prog_id) {
        this.prog_id = prog_id;
    }

    public int getFr_channel_id() {
        return fr_channel_id;
    }

    public void setFr_channel_id(int fr_channel_id) {
        this.fr_channel_id = fr_channel_id;
    }

    public int getFr_day_id() {
        return fr_day_id;
    }

    public void setFr_day_id(int fr_day_id) {
        this.fr_day_id = fr_day_id;
    }

    public int getFr_type_id() {
        return fr_type_id;
    }

    public void setFr_type_id(int fr_type_id) {
        this.fr_type_id = fr_type_id;
    }

    public String getProg_name() {
        return prog_name;
    }

    public void setProg_name(String prog_name) {
        this.prog_name = prog_name;
    }

    public String getProg_timestart() {
        return prog_timestart;
    }

    public void setProg_timestart(String prog_timestart) {
        this.prog_timestart = prog_timestart;
    }

    public String getProg_timeend() {
        return prog_timeend;
    }

    public void setProg_timeend(String prog_timeend) {
        this.prog_timeend = prog_timeend;
    }
}
