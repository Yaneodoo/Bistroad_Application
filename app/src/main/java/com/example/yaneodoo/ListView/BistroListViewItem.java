package com.example.yaneodoo.ListView;

import android.graphics.drawable.Drawable;

public class BistroListViewItem {
    private Drawable iconDrawable;
    private String titleStr, locationStr, descStr;

    public void setIcon(Drawable icon) {
        iconDrawable = icon;
    }

    public void setTitle(String title) {
        titleStr = title;
    }

    public void setDesc(String desc) {
        descStr = desc;
    }

    public void setLocationStr(String locationStr) { this.locationStr = locationStr; }

    public Drawable getIcon() {
        return this.iconDrawable;
    }

    public String getTitle() {
        return this.titleStr;
    }

    public String getLocationStr() { return locationStr; }

    public String getDesc() {
        return this.descStr;
    }

}