package com.carehack.medivault;

import java.io.Serializable;

/**
 * Created by sachin on 26/11/17.
 */

public class DataClass implements Serializable{
    private String title;
    private String subtitle;
    private String date;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public DataClass() {
    }

    public DataClass(String title,String subtitle,String date) {
        this.title=title;
        this.subtitle = subtitle;
        this.date=date;
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
