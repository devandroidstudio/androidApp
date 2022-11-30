package com.example.appanimals.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.Key;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appanimals.Activity.IntroActivity;
import com.example.appanimals.Activity.MainActivity;
import com.example.appanimals.Activity.MainActivity2;
import com.example.appanimals.Activity.VerificationActivity;
import com.example.appanimals.Adapter.ISendDataListener;
import com.example.appanimals.Adapter.ISendDataLogin;
import com.example.appanimals.Model.Account;
import com.example.appanimals.R;
import com.example.appanimals.transformer.ZoomOutPageTransformer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class SingUpFragment extends Fragment {


    public SingUpFragment() {
        // Required empty public constructor
    }
    private FloatingActionButton fabSingUp;
    private View view;
//    private ISendDataListener iSendDataListener;
    private IntroActivity mIntroActivity;
    private ISendDataLogin iSendDataLogin;
    private MainActivity2 mMainActivity2;
    private TextView tvSingIn;
    private EditText editEmail,editPhone,editPassword,editFullName;
    private DatabaseReference reference;
    private static String cryptoPass = "sup3rS3xy";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sing_up, container, false);
        initView();
        ActionView();
        return view;
    }

    public static String encryptIt(@NonNull String value) {
        try {
            DESKeySpec keySpec = new DESKeySpec(cryptoPass.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] clearText = value.getBytes("UTF8");
            // Cipher is not thread safe
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
            return encrypedValue;
        } catch (NoSuchPaddingException | InvalidKeySpecException | IllegalBlockSizeException | UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException e) {
            e.printStackTrace();
        }
        return value;
    }
    private void ActionView() {
        fabSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSingUp();
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                String strDate = simpleDateFormat.format(date);
                String role = "user";
                String status = "On";

                Account account = new Account(editFullName.getText().toString(),editPhone.getText().toString(),editEmail.getText().toString(),encryptIt(editPassword.getText().toString()),strDate,role,status);
                Account account2 = new Account(editFullName.getText().toString(),editPhone.getText().toString(),editEmail.getText().toString(),editPassword.getText().toString(),strDate,role,status);
                reference.child(editFullName.getText().toString()).setValue(account);
                iSendDataLogin.SignUp(account2,editPhone.getText().toString());
                Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
            }
        });

        tvSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntroActivity.viewPager2.setCurrentItem(0);
                mIntroActivity.viewPager2.setPageTransformer(new ZoomOutPageTransformer());
            }
        });
    }

    private void onClickSingUp() {

    }
    private void initView() {
//        mMainActivity2 = (MainActivity2) getActivity();
        mIntroActivity = (IntroActivity) getActivity();
        iSendDataLogin = mIntroActivity;
        fabSingUp = view.findViewById(R.id.fab_sing_up);
        tvSingIn = view.findViewById(R.id.tv_sing_in);
        editFullName = view.findViewById(R.id.edit_name_customer_sing_up);
        editEmail = view.findViewById(R.id.edit_email_customer_sing_up);
        editPhone = view.findViewById(R.id.edit_phone_sing_up);
        editPassword = view.findViewById(R.id.edit_password_customer_sing_up);
        reference = FirebaseDatabase.getInstance().getReference("Account");
    }
}