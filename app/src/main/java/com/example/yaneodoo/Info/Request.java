package com.example.yaneodoo.Info;

import java.io.Serializable;

public class Request implements Serializable {
    private Menu menu;
    private Review review;
    private String itemId;

    private Integer amount;
    private Menu item;

    public Request(String itemId, Integer amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    public Integer getAmount(){return amount;}
    public void setAmount(Integer amount){this.amount = amount;}

    public Menu getMenu() {
        return item;
    }

    public void setMenu(Menu item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "[amount = " + amount + ", itemId = " + itemId + "]";
    }
}
