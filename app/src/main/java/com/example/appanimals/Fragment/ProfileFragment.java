package com.example.appanimals.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appanimals.Activity.IntroActivity;
import com.example.appanimals.Activity.MainActivity;
import com.example.appanimals.Activity.MainActivity2;
import com.example.appanimals.Admin.AdminActivity;
import com.example.appanimals.Model.Account;
import com.example.appanimals.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    private View view;
    private TextInputLayout editFullName,editEmail,editPhone,editPassword;
    private Button btnUpdate,btnLogOut,btnManager;
    private TextView tvRole,tvName,tvDateJoined;
    private DatabaseReference reference;
    private MainActivity mMainActivity;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private static final String name = "";
    private static String cryptoPass = "sup3rS3xy";
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);


        initView();
        ActionView();
        GetData();
        mAuth = FirebaseAuth.getInstance();
        return view;

    }

    


    public static String decryptIt(String value) {
        try {
            DESKeySpec keySpec = new DESKeySpec(cryptoPass.getBytes("UTF8"));

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
            // cipher is not thread safe
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

            String decrypedValue = new String(decrypedValueBytes);
            return decrypedValue;

        } catch (InvalidKeyException | UnsupportedEncodingException | InvalidKeySpecException | NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(getContext(), IntroActivity.class));
        }
    }

    private void GetData() {
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                Objects.requireNonNull(editFullName.getEditText()).setText(profile.getDisplayName());
                Objects.requireNonNull(editFullName.getEditText()).setText(MainActivity.account.getFullName());
                Objects.requireNonNull(editEmail.getEditText()).setText(profile.getEmail());
                tvName.setText(profile.getDisplayName());
                tvName.setText(MainActivity.account.getFullName());
                tvDateJoined.setText(MainActivity.account.getDateJoined());
                Objects.requireNonNull(editPhone.getEditText()).setText(MainActivity.account.getPhone());
                Objects.requireNonNull(editPassword.getEditText()).setText(decryptIt(MainActivity.account.getPassword()));
                tvRole.setText(MainActivity.account.getRole());

            }
        }

    }

    private void ActionView() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FirebaseAuth.getInstance().signOut();
               startActivity(new Intent(mMainActivity, IntroActivity.class));
            }
        });
        btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mMainActivity, AdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                mMainActivity.overridePendingTransition(R.anim.sile_in_right,R.anim.sile_out_right);
            }
        });

    }

    private void onClickUpdateProfile() {
        progressDialog.show();
        if (user == null){
            return;
        }
        if (isEmailChange()|| isFullNameChange() || isPasswordChange()){
            Account account = new Account(Objects.requireNonNull(editFullName.getEditText()).getText().toString(), Objects.requireNonNull(editPhone.getEditText()).getText().toString(), Objects.requireNonNull(editEmail.getEditText()).getText().toString(), SingUpFragment.encryptIt(Objects.requireNonNull(editPassword.getEditText()).getText().toString()).toString(),tvDateJoined.getText().toString(),MainActivity.account.getRole(),MainActivity.account.getStatus());
            reference = FirebaseDatabase.getInstance().getReference("Account");
            reference.child(editFullName.getEditText().getText().toString()).updateChildren(account.UpdateAccount()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getActivity(), "Data has been update", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        editFullName.getEditText().setFocusable(false);
                        editEmail.getEditText().setFocusable(false);
                        editPassword.getEditText().setFocusable(false);
                        editPhone.getEditText().setFocusable(false);

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mMainActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(getActivity(), "Data is same can not be update", Toast.LENGTH_SHORT).show();
        }
    }

    private void reAuthenticate(String strEmail, String strPassword){
        AuthCredential credential = EmailAuthProvider
                .getCredential(strEmail, strPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            onClickUpdateProfile();
                        }
                    }
                });
    }

    private boolean isEmailChange() {
        assert user != null;
        if (validateEmail()){
            progressDialog.dismiss();
            return false;
        }
        else
        {
            user.updateEmail(Objects.requireNonNull(editEmail.getEditText()).getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Update email successful", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                dialogUpdateProfile();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            return true;
        }
    }

    private void dialogUpdateProfile(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Please enter information login");
        dialog.setContentView(R.layout.custom_dialog_profile);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        EditText editEmail = dialog.findViewById(R.id.edit_email_dialog);
        EditText editPassword = dialog.findViewById(R.id.edit_password_dialog);
        Button btnUpdateDialog = dialog.findViewById(R.id.btn_continues_dialog);
        dialog.show();
        btnUpdateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reAuthenticate(editEmail.getText().toString(),editPassword.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private boolean isFullNameChange(){
        if (!user.getDisplayName().equals(editFullName.getEditText().getText().toString())){
            String strFullName = editFullName.getEditText().getText().toString();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(strFullName)
//                    .setPhotoUri(uri)
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Update profile successful", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                tvName.setText(user.getDisplayName());
                            }
                            else {
                                dialogUpdateProfile();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mMainActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            return true;
        }
        else {
            progressDialog.dismiss();
            return false;
        }
    }

    private boolean isPasswordChange(){
        if (editPassword != null){
            user.updatePassword(editPassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Update password successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return true;
        }
        else {
            progressDialog.dismiss();
            return false;
        }
    }

    private void initView() {
        mMainActivity = (MainActivity) getActivity();
        reference = FirebaseDatabase.getInstance().getReference("Account");
        tvName = view.findViewById(R.id.tv_Name_profile);
        tvRole = view.findViewById(R.id.tv_role_profile);
        btnUpdate = view.findViewById(R.id.btn_update);
        editEmail = view.findViewById(R.id.email_profile);
        editFullName = view.findViewById(R.id.full_name_profile);
        editPhone = view.findViewById(R.id.phone_profile);
        editPassword = view.findViewById(R.id.password_profile);
        tvDateJoined = view.findViewById(R.id.tv_date_joined);
        btnManager = view.findViewById(R.id.Manager);
        mMainActivity = (MainActivity) getActivity();
        btnLogOut = view.findViewById(R.id.LogOut);
        if (MainActivity.account.getRole().equals("admin")){
            btnManager.setVisibility(View.VISIBLE);
        }
        else {
            btnManager.setVisibility(View.INVISIBLE);
        }

        progressDialog = new ProgressDialog(mMainActivity);
        boolean check = user.isEmailVerified();
        Toast.makeText(mMainActivity, check+"", Toast.LENGTH_SHORT).show();
    }

    private Boolean validateName(){
        String val = editFullName.getEditText().getText().toString();

        if (val.isEmpty())
        {
            editFullName.setError("Field cannot be empty");
            return false;
        }
        else
        {
            editFullName.setError(null);
            editFullName.setErrorEnabled(false);
            return true;
        }
    }

    @NonNull
    private Boolean validateEmail(){
        String val = Objects.requireNonNull(editEmail.getEditText()).getText().toString();
        if (val.isEmpty())
        {
            editEmail.setError("Field cannot be empty");

            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches())
        {
            editEmail.setError("Invalid email address");
            return false;
        }
        else
        {
            editEmail.setError(null);
            editEmail.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePhone(){
        String val = editPhone.getEditText().getText().toString();

        if (val.isEmpty())
        {
            editPhone.setError("Field cannot be empty");
            return false;
        }
        else
        {
            editPhone.setError(null);
            editPhone.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword(){
        String val = editPassword.getEditText().getText().toString();
        if (val.isEmpty())
        {
            editPassword.setError("Field cannot be empty");
            return false;
        }
        else if (val.length()<5)
        {
            editPassword.setError("Password is too weak");
            return false;
        }
        else
        {
            editPassword.setError(null);
            editPassword.setErrorEnabled(false);
            return true;
        }
    }

}