package com.example.appanimals.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class InfoCustomer implements Serializable {
    private String username;
    private String phone;
    private String email;
    private ArrayList<Cart> carts;
    private String totalPrice;
    private String date;

    public InfoCustomer() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Cart> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<Cart> carts) {
        this.carts = carts;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public InfoCustomer(String username, String phone, String email, ArrayList<Cart> carts, String totalPrice, String date) {
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.carts = carts;
        this.totalPrice = totalPrice;
        this.date = date;
    }
}
