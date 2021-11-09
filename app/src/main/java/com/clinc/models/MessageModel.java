package com.clinc.models;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private int id;
    private String senddate;
    private String message;
    private String check_sender;
    private int p_id;
    private String name;

    public int getId() {
        return id;
    }

    public String getSenddate() {
        return senddate;
    }

    public String getMessage() {
        return message;
    }

    public String getCheck_sender() {
        return check_sender;
    }

    public int getP_id() {
        return p_id;
    }

    public String getName() {
        return name;
    }
}