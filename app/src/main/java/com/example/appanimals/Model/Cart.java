package com.example.appanimals.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {
    private String name;
    private Long price;
    private ArrayList<String> img;
    private int count;
    private String category;

    public Cart() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public ArrayList<String> getImg() {
        return img;
    }

    public void setImg(ArrayList<String> img) {
        this.img = img;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Cart(String name, Long price, ArrayList<String> img, int count, String category) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.count = count;
        this.category = category;
    }
}
