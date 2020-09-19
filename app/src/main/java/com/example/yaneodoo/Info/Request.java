package com.example.yaneodoo.Info;

import java.io.Serializable;

public class Request implements Serializable {
    private Integer quantity;
    private Menu menu;
    private Review review;
  
    private String amount;
    private Menu item;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getAmount(){return amount;}
    public void setAmount(String amount){this.amount = amount;}

    public Menu getMenu() {
        return item;
    }

    public void setMenu(Menu item) {
        this.item = item;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "[quantity = " + quantity + ", menu = " + menu + ", review = " + review + "]";
    }
}
