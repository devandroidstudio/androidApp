package com.example.appanimals.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.appanimals.Adapter.ISendDataListener;
import com.example.appanimals.Adapter.ISendDataLogin;
import com.example.appanimals.Adapter.ViewpagerIntroAdapter;
import com.example.appanimals.Fragment.SingInFragment;
import com.example.appanimals.Fragment.SingUpFragment;
import com.example.appanimals.Model.Account;
import com.example.appanimals.R;
import com.example.appanimals.transformer.DepthPageTransformer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IntroActivity extends AppCompatActivity implements ISendDataLogin {
    private ImageView mLogo,mBackground;
    private ConstraintLayout constraintLayout;
    private LottieAnimationView lottieAnimationView;
    public ViewPager2 viewPager2;
    private ViewpagerIntroAdapter adapter;
    public int position = 0;
    private ProgressDialog progressDialog;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        nextActivity();
        getData();

    }

    private void getData() {

    }

    private void initView() {
        mLogo = findViewById(R.id.app_name);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        mBackground = findViewById(R.id.img);
        viewPager2 = findViewById(R.id.pages);
        adapter = new ViewpagerIntroAdapter(this);
        viewPager2.setAdapter(adapter);
        viewPager2.setPageTransformer(new DepthPageTransformer());
        mBackground.animate().translationY(-22000).setDuration(1500).setStartDelay(4000);
        mLogo.animate().translationY(14000).setDuration(1500).setStartDelay(4000);
        lottieAnimationView.animate().translationY(14000).setDuration(1000).setStartDelay(4000);
        progressDialog = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference("Account");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            startActivity(getIntent());
        }
        else {
            startActivity(new Intent(IntroActivity.this,MainActivity.class));
            finish();
        }
    }

    private void nextActivity(){

    }


//    @Override
//    public void sendData(String str) {
//        if (str.equals("1")){
//            viewPager2.setCurrentItem(1);
//        }
//        else if (str.equals("0")){
//            viewPager2.setCurrentItem(0);
//        }
//    }

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
                                            Toast.makeText(IntroActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(IntroActivity.this,VerificationActivity.class);
                                            intent.putExtra("code_verify_login",account.getPassword());
                                            startActivity(intent);
                                            finishAffinity();
                                        }
                                    }
                                });

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(account.getFullName())
//                    .setPhotoUri(uri)
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(IntroActivity.this, "Update profile successful", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(IntroActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
                else {
                    Toast.makeText(IntroActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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
                    startActivity(new Intent(IntroActivity.this,MainActivity.class));
                    finishAffinity();
                }
                else {
                    Toast.makeText(IntroActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

//    @Override
//    public void sendDataAccount(Account account, Integer integer) {
//        if (integer.equals(0)){
//            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            mAuth.signInWithEmailAndPassword(account.getEmail().trim(),account.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()){
//                        startActivity(new Intent(IntroActivity.this,MainActivity.class));
//                        finishAffinity();
//                    }
//                    else {
//                        Toast.makeText(IntroActivity.this, "Authentication failed.",
//                                Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//            });
//        }
//
//    }
}