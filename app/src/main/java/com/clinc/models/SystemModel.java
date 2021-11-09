package com.clinc.models;

import java.io.Serializable;

public class SystemModel implements Serializable {
   private int id;
   private String content;

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}