package com.example.yaneodoo.Info;

import java.io.Serializable;

public class Request implements Serializable {
    private String itemId;
    private Integer amount;
    private Menu item;

    public Request(String itemId, Integer amount) {
        this.itemId = itemId;
        this.amount = amount;
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
