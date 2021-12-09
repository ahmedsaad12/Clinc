package com.clinc.models;

import java.io.Serializable;

public class ReservisionModel implements Serializable {
    private String date;
    private String name;
    private String patient_id;
    private String time_name;
    private String visit_type;
    private String quarter_name;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getTime_name() {
        return time_name;
    }

    public void setTime_name(String time_name) {
        this.time_name = time_name;
    }

    public String getVisit_type() {
        return visit_type;
    }

    public void setVisit_type(String visit_type) {
        this.visit_type = visit_type;
    }

    public String getQuarter_name() {
        return quarter_name;
    }

    public void setQuarter_name(String quarter_name) {
        this.quarter_name = quarter_name;
    }
}