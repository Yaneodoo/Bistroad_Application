package com.example.yaneodoo.Info;

import java.io.Serializable;

public class Store implements Serializable {
    private String phone;
    private String name;
    private String description;
    private Location location;
    private String id;
    private String ownerId;
    private Photo photo;
    private String address;

    public Store() {
    }

    public Store(String description, Location location, String name, String ownerId, String phone) {
        this.phone = phone;
        this.name = name;
        this.description = description;
        this.location = location;
        this.ownerId = ownerId;
        //address
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "[phone = " + phone + ", name = " + name + ", description = " + description + ", location = " + location + ", id = " + id + ", ownerId = " + ownerId + "]";
    }
}