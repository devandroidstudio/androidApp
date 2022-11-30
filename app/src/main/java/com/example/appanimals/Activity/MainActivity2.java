package com.example.appanimals.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.appanimals.Adapter.ISendDataLogin;
import com.example.appanimals.Adapter.ViewpagerIntro2Adapter;
import com.example.appanimals.Adapter.ViewpagerIntroAdapter;
import com.example.appanimals.Model.Account;
import com.example.appanimals.R;
import com.example.appanimals.transformer.DepthPageTransformer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity2 extends AppCompatActivity implements ISendDataLogin {
    private ViewPager2 viewPager2;
    private ViewpagerIntro2Adapter adapter;
    public int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        //ActionView();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user == null){
//            startActivity(getIntent());
//        }
//        else {
//            startActivity(new Intent(MainActivity2.this,MainActivity.class));
//            finish();
//        }
    }

    private void ActionView() {
        if (position == 0)
        {
            viewPager2.setCurrentItem(0);
        }
        else
        {
            viewPager2.setCurrentItem(1);
        }
    }

    private void initView() {

        viewPager2 = findViewById(R.id.pages_2);
        viewPager2.setPageTransformer(new DepthPageTransformer());
        viewPager2.setAdapter(adapter);
        adapter = new ViewpagerIntro2Adapter(MainActivity2.this);
    }

    @Override
    public void SignUp(Account account, String nPhone) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(account.getEmail(),account.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null)
                    {
                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity2.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MainActivity2.this,VerificationActivity.class);
                                            intent.putExtra("code_verify_login",account.getPassword());
                                            startActivity(intent);
                                            MainActivity2.this.finishAffinity();
                                        }
                                    }
                                });
                    }

                }
                else {
                    Toast.makeText(MainActivity2.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void Login(Account account) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(account.getEmail().trim(),account.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(MainActivity2.this,MainActivity.class));
                    finishAffinity();
                }
                else {
                    Toast.makeText(MainActivity2.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
}