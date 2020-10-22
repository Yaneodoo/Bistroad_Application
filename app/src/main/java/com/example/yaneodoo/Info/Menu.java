package com.example.yaneodoo.Info;

import java.io.File;
import java.io.Serializable;

public class Menu implements Serializable {
    private String price;
    private String name;
    private String description;
    private String id;
    private String stars;
    private String storeId;
    private Integer quantity;
    private Photo photo;
    private Integer orderCount;
    private File file;

    public Menu() {
    }

    public Menu(String id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Menu(String price, String name, String id, String storeId, Integer quantity) {
        this.price = price;
        this.name = name;
        this.id = id;
        this.storeId = storeId;
        this.quantity = quantity;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
        return "[price = " + price + ", name = " + name + ", description = " + description + ", id = " + id + ", stars = " + stars + ", storeId = " + storeId +
                ", quantity = " +quantity+", photo = "+photo.getThumbnailUrl().toString()+", orderCount = "+orderCount+"]";
    }
}