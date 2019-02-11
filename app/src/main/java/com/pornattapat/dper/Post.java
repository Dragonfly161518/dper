package com.pornattapat.dper;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post {
    private String text;
    private Date date;
    private String user;

    public Post() {

    }

    public Post(String text, Date date, String user) {
        this.text = text;
        this.date = date;
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @ServerTimestamp
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
