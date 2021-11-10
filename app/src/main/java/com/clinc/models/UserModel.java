package com.clinc.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private int id;
    private String name;
    private String user_name;
    private String pass;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public UserModel(String name) {
        this.name = name;
    }
}
