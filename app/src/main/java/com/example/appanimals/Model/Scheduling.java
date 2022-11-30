package com.example.appanimals.Model;

import java.io.Serializable;

public class Scheduling implements Serializable {
    private String nameCustomer;
    private String typeOfProcedure;
    private String procedure;
    private String name;
    private String sex;
    private String age;
    private String kind;
    private String date;
    private String time;

    public Scheduling() {
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getTypeOfProcedure() {
        return typeOfProcedure;
    }

    public void setTypeOfProcedure(String typeOfProcedure) {
        this.typeOfProcedure = typeOfProcedure;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Scheduling(String nameCustomer, String typeOfProcedure, String procedure, String name, String sex, String age, String kind, String date, String time) {
        this.nameCustomer = nameCustomer;
        this.typeOfProcedure = typeOfProcedure;
        this.procedure = procedure;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.kind = kind;
        this.date = date;
        this.time = time;
    }
}
