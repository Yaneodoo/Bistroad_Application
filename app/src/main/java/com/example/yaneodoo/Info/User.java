package com.example.yaneodoo.Info;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String role;
    private String id;

    public User() {
    }

    public User(String username, String password, String fullName, String phone, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "[role = " + role + ", phone = " + phone + ", fullName = " + fullName + ", id = " + id + ", username = " + username + "]";
    }
}