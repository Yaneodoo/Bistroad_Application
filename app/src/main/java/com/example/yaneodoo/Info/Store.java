package com.example.yaneodoo.Info;

public class Store {
    private String phone;
    private String name;
    private String description;
    private Location location;
    private String id;
    private String ownerId;
    private String photoUri;

    public Store() {
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public Store(String description, Location location, String name, String ownerId, String phone) {
        this.phone = phone;
        this.name = name;
        this.description = description;
        this.location = location;
        this.ownerId = ownerId;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
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

    @Override
    public String toString() {
        return "[phone = " + phone + ", name = " + name + ", description = " + description + ", location = " + location + ", id = " + id + ", ownerId = " + ownerId + "]";
    }
}