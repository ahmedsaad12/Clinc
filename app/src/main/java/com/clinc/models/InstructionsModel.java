package com.clinc.models;

import java.io.Serializable;

public class InstructionsModel implements Serializable {
   private int id;
   private String title;
   private String content;
private String imageURL;
    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }
}