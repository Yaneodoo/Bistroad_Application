package com.example.yaneodoo.Info;

import java.io.Serializable;

public class Review4Leave implements Serializable {
    private String contents, itemId, orderId, storeId, timestamp, writerId;
    private Integer stars;

    public Review4Leave(String reviewContent, String itemId, String orderId, Integer stars, String storeId, String timestamp, String writerId) {
        contents = reviewContent;
        this.itemId = itemId;
        this.orderId = orderId;
        this.stars = stars;
        this.storeId = storeId;
        this.timestamp = timestamp;
        this.writerId = writerId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    @Override
    public String toString() {
        return "[contents = " + contents + ", itemId = " + itemId + ", orderId = " + orderId + ", stars = " + stars + ", storeId = " + storeId + ", timestamp = " + timestamp + ", writerId = " + writerId  + "]";
    }
}