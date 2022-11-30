package com.example.appanimals.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appanimals.Activity.IntroActivity;
import com.example.appanimals.Activity.MainActivity2;
import com.example.appanimals.Adapter.ISendDataLogin;
import com.example.appanimals.Model.Account;
import com.example.appanimals.Model.ISendDataListener;
import com.example.appanimals.R;
import com.example.appanimals.transformer.ZoomOutPageTransformer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SingInFragment extends Fragment {




    public SingInFragment() {
        // Required empty public constructor
    }

    private FloatingActionButton fabSingIn;
    private View view;
    private IntroActivity mIntroActivity;
    private ISendDataListener iSendDataListener;
    private MainActivity2 mMainActivity2;
    private ISendDataLogin iSendDataLogin;
    private TextView tvSingIn;
    private EditText editEmail,editPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sing_in, container, false);
        initView();
        ActionView();
        return view;
    }

    private void ActionView() {

        tvSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntroActivity.viewPager2.setCurrentItem(1);
                mIntroActivity.viewPager2.setPageTransformer(new ZoomOutPageTransformer());
            }
        });
        fabSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account account = new Account(editEmail.getText().toString(),editPassword.getText().toString());
                iSendDataLogin.Login(account);
            }
        });
    }

    private void initView() {
        mIntroActivity = (IntroActivity) getActivity();
//        iSendDataLogin = mIntroActivity;

//        mMainActivity2 = (MainActivity2) getActivity();
        iSendDataLogin = mIntroActivity;
        fabSingIn = view.findViewById(R.id.fab_sing_in);
        tvSingIn = view.findViewById(R.id.tv_sing_up);
        editEmail = view.findViewById(R.id.edit_email_customer_sing_in);
        editPassword = view.findViewById(R.id.edit_password_sing_in);

    }
}