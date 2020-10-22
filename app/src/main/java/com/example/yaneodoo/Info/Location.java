package com.example.yaneodoo.Info;

import java.io.Serializable;

public class Location implements Serializable {
    private String lng;
    private String lat;

    public Location(String lat, String lng) {
        this.lng = lng;
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "[lng = " + lng + ", lat = " + lat + "]";
    }
}