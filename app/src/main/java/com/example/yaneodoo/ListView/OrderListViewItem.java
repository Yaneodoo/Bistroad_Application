package com.example.yaneodoo.ListView;

import android.graphics.drawable.Drawable;

import com.example.yaneodoo.Info.Order;
import com.example.yaneodoo.Info.Request;
import com.example.yaneodoo.Info.Store;

import java.util.ArrayList;
import java.util.List;

public class OrderListViewItem {
    private Drawable progress;
    private String dateStr, nameStr, orderStr, stateStr, role, orderNum;
    private Integer tableNum;
    private Order order;

    public Integer getTableNum() {
        return tableNum;
    }

    public void setTableNum(Integer tableNum) {
        this.tableNum = tableNum;
    }

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

    public String getStateStr() {
        return stateStr;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public String getRole() {
        return role;
    }

    public Order getOrder() {
        return order;
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

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
