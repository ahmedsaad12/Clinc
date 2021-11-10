package com.clinc.models;

import java.io.Serializable;

public class SystemModel implements Serializable {
    private int id;
    private String content;
    private String title;
    private String text;

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}