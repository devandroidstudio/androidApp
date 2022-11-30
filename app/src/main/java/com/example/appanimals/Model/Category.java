package com.example.appanimals.Model;

import java.io.Serializable;

public class Category implements Serializable {
    private String name;
    private String image;
    private String status;


    public void setStatus(String status) {
        this.status = status;
    }

    public Category(String name, String image, String status) {
        this.name = name;
        this.image = image;
        this.status = status;
    }

    public Category() {
    }
    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category(String name, String image) {
        this.name = name;
        this.image = image;
    }
}
