package com.example.appanimals.Adapter;

import com.example.appanimals.Model.Account;

public interface ISendDataLogin {
    void SignUp(Account account, String nPhone);
    void Login(Account account);
}
