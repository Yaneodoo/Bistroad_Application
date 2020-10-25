package com.example.yaneodoo.Info;

import java.io.Serializable;

public class Request implements Serializable {
    private Integer amount;
    private Menu item;
    private Integer price;
    private String itemId;

    public Request(Menu item, Integer amount, Integer price) {
        this.amount = amount;
        this.item = item;
        this.price = price;
        this.itemId = item.getId();
    }

    public Integer getAmount(){return amount;}
    public void setAmount(Integer amount){this.amount = amount;}

    public Integer getPrice(){return price;}
    public void setPrice(Integer price){this.price = price;}

    public Menu getMenu() {
        return item;
    }
    public void setMenu(Menu item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "[amount = " + amount + ", itemId = " + item.getId() + ", price = " + price + "]";
    }
}
