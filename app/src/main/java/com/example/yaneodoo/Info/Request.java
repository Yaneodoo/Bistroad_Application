package com.example.yaneodoo.Info;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    String amount;
    private Menu item;

    public Menu getMenu() {
        return item;
    }

    public void setMenu(Menu item) {
        this.item = item;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "[item = " + item + ", amount = " + amount + "]";
    }

}
