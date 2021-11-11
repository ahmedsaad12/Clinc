package com.clinc.models;

import java.io.Serializable;

public class InstructionsModel implements Serializable {
   private int id;
   private String title;
   private String name;
private String image_url;
    public int getId() {
        return id;
    }

    public String getContent() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return image_url;
    }
}