package com.example.yaneodoo.ListView;

import android.graphics.drawable.Drawable;

public class ReviewListViewItem { // 리뷰 남기기와 동일한 부분임
    private String dateStr, writerStr, menuStr, scoreStr, reviewStr;
    private Drawable iconDrawable;

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public String getDateStr() {
        return dateStr;
    }

    public String getWriterStr() {
        return writerStr;
    }

    public String getMenuStr() {
        return menuStr;
    }

    public String getScoreStr() {
        return scoreStr;
    }

    public String getReviewStr() {
        return reviewStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public void setWriterStr(String writerStr) {
        this.writerStr = writerStr;
    }

    public void setMenuStr(String menuStr) {
        this.menuStr = menuStr;
    }

    public void setScoreStr(String scoreStr) {
        this.scoreStr = scoreStr;
    }

    public void setReviewStr(String reviewStr) {
        this.reviewStr = reviewStr;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }
}