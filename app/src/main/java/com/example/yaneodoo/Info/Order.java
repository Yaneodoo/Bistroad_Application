package com.example.yaneodoo.Info;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Order implements Serializable {
    private String id;
    private Store store;
    private String userId;
    private List<Request> orderLines;
    private Integer tableNum;
    private String progress;
    private Boolean hasReview=false;
    private String storeId;
    private String timestamp;

    public Order() {
    }

    public Order(Store store, String userId, List<Request> requests, String date, Integer tableNum, String progress) {
        this.store = store;
        this.userId = userId;
        this.orderLines = requests;
        this.timestamp = date;
        this.tableNum = tableNum;
        this.progress = progress;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTableNum(Integer tableNum) {
        this.tableNum = tableNum;
    }

    public Boolean getHasReview() {
        return hasReview;
    }

    public void setHasReview(Boolean hasReview) {
        this.hasReview = hasReview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getUserId() {
        return userId;
    } 

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Request> getRequests() {
        return orderLines;
    }

    public void setRequest(List<Request> requests) {
        this.orderLines = requests;
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
        return "[id = " + id + ", store = " + store.toString() + ", userId = " + userId + ", requestList = " + orderLines + ", date = " + timestamp + ", tableNum = " + tableNum + ", progress = " + progress + "]";
    }
}
