package com.example.yaneodoo.Info;

import android.graphics.drawable.Drawable;

public class BistroInfo {
    Drawable represent_photo;
    String name;
    String location;
    String tel;

    public BistroInfo(Drawable represent_photo, String name, String location, String tel) {
        this.represent_photo = represent_photo;
        this.name = name;
        this.location = location;
        this.tel = tel;
    }
}
