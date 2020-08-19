package com.example.yaneodoo.Info;

import java.util.List;

public class Request {
    String amount;
    private List<Menu> menuList;

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "[menuList = " + menuList + ", amount = " + amount + "]";
    }

}
