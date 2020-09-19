package com.example.yaneodoo.Info;

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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
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
