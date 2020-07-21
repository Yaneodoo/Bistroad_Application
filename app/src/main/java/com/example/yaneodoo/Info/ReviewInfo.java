package com.example.yaneodoo.Info;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class ReviewInfo {
    private Drawable image;
    private Date written_date;
    private String name, menu;
    private Integer score;

    public ReviewInfo(Drawable image, Date written_date, String name, String menu, Integer score) {
        this.image = image;
        this.written_date = written_date;
        this.name = name;
        this.menu = menu;
        this.score = score;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Date getWritten_date() {
        return written_date;
    }

    public void setWritten_date(Date written_date) {
        this.written_date = written_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
