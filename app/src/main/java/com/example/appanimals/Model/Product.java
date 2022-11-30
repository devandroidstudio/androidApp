package com.example.appanimals.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Product implements Serializable {
    private String name;
    private ArrayList<String> img;
    private long price;
    private String description;
    private String category;
    private String sex;
    private String origin;
    private String age;
    private String weight;
    private String status;
    private String dateUpdate;

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", img=" + img +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", sex='" + sex + '\'' +
                ", origin='" + origin + '\'' +
                ", age='" + age + '\'' +
                ", weight='" + weight + '\'' +
                ", status='" + status + '\'' +
                ", dateUpdate='" + dateUpdate + '\'' +
                '}';
    }

    public Map<String,Object> toMap(){
        Map<String,Object> result = new HashMap<>();
        result.put("status",status);
        return  result;
    }
    public Map<String,Object> NotUriToMap(){
        Map<String,Object> result = new HashMap<>();
        result.put("name",name);
        result.put("img",img);
        result.put("price",price);
        result.put("description",description);
        result.put("category",category);
        result.put("sex",sex);
        result.put("origin",origin);
        result.put("age",age);
        result.put("weight",weight);
        result.put("status",status);
        result.put("dateUpdate",dateUpdate);

        return  result;
    }



    public Product() {
    }
    public Product(String name, ArrayList<String> img, long price, String description, String category, String sex, String origin, String age, String weight, String status, String dateUpdate) {
        this.name = name;
        this.img = img;
        this.price = price;
        this.description = description;
        this.category = category;
        this.sex = sex;
        this.origin = origin;
        this.age = age;
        this.weight = weight;
        this.status = status;
        this.dateUpdate = dateUpdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getImg() {
        return img;
    }

    public void setImg(ArrayList<String> img) {
        this.img = img;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
