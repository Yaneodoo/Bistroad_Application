package com.example.yaneodoo.Info;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    private String id;
    private String storeId;
    private String userId;
    private List<Request> requestList;
    private Date date;
    private Integer tableNum;
    private String progress;

    public Order() {
    }

    public Order(String storeId, String userId, List<Request> requestList, Date date, Integer tableNum, String progress) {
        this.storeId = storeId;
        this.userId = userId;
        this.requestList = requestList;
        this.date = date;
        this.tableNum = tableNum;
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Request> getRequests() {
        return requestList;
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "[id = " + id + ", storeId = " + storeId + ", userId = " + userId + ", requestList = " + requestList + ", date = " + date + ", tableNum = " + tableNum + ", progress = " + progress + "]";
    }
}
