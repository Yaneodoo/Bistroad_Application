package com.example.yaneodoo.Info;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class MenuInfo {
    private Drawable represent_image;
    private String name, desc;
    private Integer price;
    private Double score;

    public MenuInfo(Drawable represent_image, String name, Integer price, String desc) {
        this.represent_image = represent_image;
        this.name = name;
        this.price = price;
        this.desc = desc;
    }

    public Drawable getRepresent_image() {
        return represent_image;
    }

    public void setRepresent_image(Drawable represent_image) {
        this.represent_image = represent_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public static ArrayList getmenus() {
        ArrayList menus = new ArrayList();
        //menus.add(new MenuInfo(, "San Diego"));
        return menus;
    }
}
