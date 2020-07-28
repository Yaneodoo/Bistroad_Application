package com.example.yaneodoo.ListView;

import android.graphics.drawable.Drawable;

public class OrderListViewItem {
    private Drawable progress;
    private String dateStr, nameStr, orderStr;
    private String orderNum;

    public Drawable getProgress() {
        return progress;
    }

    public String getDateStr() {
        return dateStr;
    }

    public String getNameStr() {
        return nameStr;
    }

    public String getOrderStr() {
        return orderStr;
    }

    public void setProgress(Drawable progress) {
        this.progress = progress;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public void setNameStr(String nameStr) {
        this.nameStr = nameStr;
    }

    public void setOrderStr(String orderStr) {
        this.orderStr = orderStr;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
