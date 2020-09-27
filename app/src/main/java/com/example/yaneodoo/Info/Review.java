package com.example.yaneodoo.Info;

import java.io.Serializable;

public class Review implements Serializable {
    private String orderId;
    private String contents;
    private String id;
    private String itemId;
    private String storeId;
    private int stars;
    private User writer;
    private String writerId;

    public Review(){

    }
    public Review(String contents, String itemId, String orderId, int stars, String storeId, String writerId){
        this.contents = contents;
        this.itemId = itemId;
        this.orderId = orderId;
        this.stars = stars;
        this.storeId = storeId;
        this.writerId = writerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public User getUser() {
        return writer;
    }

    public void setUser(User writer) {
        this.writer = writer;
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    @Override
    public String toString() {
        return "[orderId = " + orderId + ", contents = " + contents + ", id = " + id + ", stars = " + String.valueOf(stars) + ", writerId = " + writer.toString() + "]";
    }
}