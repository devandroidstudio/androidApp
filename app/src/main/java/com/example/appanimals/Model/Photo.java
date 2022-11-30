package com.example.appanimals.Model;

import java.io.Serializable;

public class Photo implements Serializable {
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Photo(String image) {
        this.image = image;
    }
}
