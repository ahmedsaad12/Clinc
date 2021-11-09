package com.clinc.models;

import java.io.Serializable;

public class TimeModel implements Serializable {
    private String time_name;
    private String quarter_name;
    private int state;
    private String pray;

    public String getTime_name() {
        return time_name;
    }

    public String getQuarter_name() {
        return quarter_name;
    }

    public int getState() {
        return state;
    }

    public String getPray() {
        return pray;
    }
}