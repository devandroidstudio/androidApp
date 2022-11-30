package com.example.appanimals.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Account implements Serializable {
    private String fullName;
    private String phone;
    private String email;
    private String password;
    private String dateJoined;
    private String role;
    private String status;



    public Account(String fullName, String phone, String email, String password, String dateJoined, String role, String status) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.dateJoined = dateJoined;
        this.role = role;
        this.status = status;
    }

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }



    public Map<String,Object> UpdateAccount(){
        Map<String,Object> result = new HashMap<>();
        result.put("fullName",fullName);
        result.put("phone",phone);
        result.put("email",email);
        result.put("password",password);
        result.put("dateJoined",dateJoined);
        result.put("role",role);
        result.put("status",status);
        return  result;
    }
    public Map<String,Object> UpdateAccountStatus(){
        Map<String,Object> result = new HashMap<>();
        result.put("status",status);
        return  result;
    }


    public Account() {
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
