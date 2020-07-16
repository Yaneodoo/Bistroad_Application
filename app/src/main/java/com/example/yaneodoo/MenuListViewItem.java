package com.example.yaneodoo;

import android.graphics.drawable.Drawable;

public class MenuListViewItem {
    private Drawable iconDrawable;
    private String menuStr, priceStr, descStr, scoreStr, orderCountStr;

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public String getMenuStr() {
        return menuStr;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public String getDescStr() {
        return descStr;
    }

    public String getScoreStr() {
        return scoreStr;
    }

    public String getOrderCountStr() {
        return orderCountStr;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public void setMenuStr(String menuStr) {
        this.menuStr = menuStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    public void setDescStr(String descStr) {
        this.descStr = descStr;
    }

    public void setScore(String scoreStr) {
        this.scoreStr = scoreStr;
    }

    public void setOrderCountStr(String orderCountStr) {
        this.orderCountStr = orderCountStr;
    }
}