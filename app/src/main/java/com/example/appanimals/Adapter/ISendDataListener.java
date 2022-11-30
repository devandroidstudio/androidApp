package com.example.appanimals.Adapter;

import com.example.appanimals.Model.Account;
import com.example.appanimals.Model.Product;

public interface ISendDataListener {
    void sendData(String str);
    void sendDataAccount(Account account,Integer integer);
}
