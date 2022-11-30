package com.example.appanimals.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.appanimals.Model.Account;
import com.example.appanimals.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class VerificationActivity extends AppCompatActivity {
    private TextView tvSendItAgain;
    private ProgressDialog progressDialog;
    private AppCompatButton btnVerify;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.MATCH_PARENT);
        tvSendItAgain = findViewById(R.id.tv_send_it_again);
        progressDialog = new ProgressDialog(this);
        btnVerify = findViewById(R.id.btn_verify_email);
        progressDialog.setMessage("Loading");
        ActionView();
    }



//    @Override
//    protected void onResume() {
//        super.onResume();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null)
//        {
////            if (!user.isEmailVerified()){
////
////            }
////            else if(user.isEmailVerified()) {
////                progressDialog.dismiss();
////                Toast.makeText(VerificationActivity.this, "Please check your email again!", Toast.LENGTH_SHORT).show();
////            }
//            ActionView(user);
//        }
//    }

    private void ActionView() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressDialog.show();
//
//                    }
//                },1500);

                startActivity(new Intent(VerificationActivity.this,MainActivity.class));
//                overridePendingTransition(R.anim.sile_in_right,R.anim.sile_out_right);

//                finishAffinity();
            }
        });

        if (user != null)
        {
            tvSendItAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Verification email has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }


    }
}