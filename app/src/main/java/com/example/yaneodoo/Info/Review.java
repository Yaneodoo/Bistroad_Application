package com.example.yaneodoo.Info;

import java.io.Serializable;

public class Review implements Serializable {
    private String orderId;
    private String contents;
    private String id;
    private Menu item;
    private String storeId;
    private Integer stars;
    private String itemId;
    private String writerId;
    private User writer;
    private Photo photo;
    private String timestamp;

    public Review(String reviewContent, String itemId, String orderId, Integer stars, String storeId, String timestamp, String writerId) {
        contents = reviewContent;
        this.itemId = itemId;
        this.orderId = orderId;
        this.stars = stars;
        this.storeId = storeId;
        this.timestamp = timestamp;
        this.writerId = writerId;
    }

    public Review(){

    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public Menu getItem() {
        return item;
    }

    public void setItem(Menu item) {
        this.item = item;
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