package com.example.yaneodoo.Info;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    private String id;
    private String storeId;
    private String userId;
    private List<Request> requests;
    private Date date;
    private int tableNum;
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
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
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
        return "[id = " + id + ", storeId = " + storeId + ", userId = " + userId + ", requests = " + requests + ", date = " + date + ", tableNum = " + tableNum + ", progress = " + progress + "]";
    }
}
