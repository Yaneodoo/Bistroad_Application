package com.example.yaneodoo.Info;

import java.io.Serializable;

public class Review implements Serializable {
    private String orderId;
    private String contents;
    private String id;
    private String itemId;
    private String storeId;
    private String stars;
    private User writer;

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

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public User getUser() {
        return writer;
    }

    public void setUser(User writer) {
        this.writer = writer;
    }

    @Override
    public String toString() {
        return "[orderId = " + orderId + ", contents = " + contents + ", id = " + id + ", stars = " + stars + ", writerId = " + writer.toString() + "]";
    }
}