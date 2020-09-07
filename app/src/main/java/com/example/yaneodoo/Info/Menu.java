package com.example.yaneodoo.Info;

import java.io.Serializable;

public class Menu implements Serializable {
    private String price;
    private String photoUri;
    private String name;
    private String description;
    private String id;
    private String stars;
    private String storeId;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "[price = " + price + ", photoUri = " + photoUri + ", name = " + name + ", description = " + description + ", id = " + id + ", stars = " + stars + ", storeId = " + storeId + "]";
    }
}