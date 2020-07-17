package com.example.yaneodoo.Info;

import android.graphics.drawable.Drawable;

public class MenuInfo {
    Drawable represent_image;
    String name;
    Integer price;
    String desc;
    Double score;

    public MenuInfo(Drawable represent_image, String name, Integer price, String desc) {
        this.represent_image = represent_image;
        this.name = name;
        this.price = price;
        this.desc = desc;
    }
}
