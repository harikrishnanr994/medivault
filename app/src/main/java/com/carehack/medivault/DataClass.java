package com.carehack.medivault;

import java.io.Serializable;

/**
 * Created by sachin on 26/11/17.
 */

public class DataClass implements Serializable{
    private String title,subtitle,date;

    public DataClass() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
