package com.clinc.models;

import java.io.Serializable;

public class ProfileModel implements Serializable {
    private String name;
    private int id;
    private String date;
    private int waiting2;
    private int age;
    private String reservation;
    private String reservation_time;
    private String enter_time;
    private String time;
    private double wieght;
    private double height;
    private double head_cm;
    private String birth_date;
    private String treatment;
    private String visit_type;
    private String dosage;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getWaiting2() {
        return waiting2;
    }

    public int getAge() {
        return age;
    }

    public String getReservation() {
        return reservation;
    }

    public String getReservation_time() {
        return reservation_time;
    }

    public String getEnter_time() {
        return enter_time;
    }

    public String getTime() {
        return time;
    }

    public double getWieght() {
        return wieght;
    }

    public double getHeight() {
        return height;
    }

    public double getHead_cm() {
        return head_cm;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getVisit_type() {
        return visit_type;
    }

    public String getDosage() {
        return dosage;
    }
}