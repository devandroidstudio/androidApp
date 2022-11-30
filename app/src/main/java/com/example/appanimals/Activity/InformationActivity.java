package com.example.appanimals.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appanimals.Adapter.NotificationApplication;
import com.example.appanimals.Model.Cart;
import com.example.appanimals.Model.InfoCustomer;
import com.example.appanimals.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InformationActivity extends AppCompatActivity {
    private EditText editTextName, editTextPhone, editTextEmail;
    private Button btnConfirm, btnBack;
    private DatabaseReference reference;
    private ArrayList<InfoCustomer> arrayList;
    private ArrayList<Cart> cartArrayList;
    private String name;
    private String phone;
    private String email;
    private String totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        ActioView();
        GetData();
        reference = FirebaseDatabase.getInstance().getReference("InformationCustomer");
//        Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
    }

    private void GetData() {
        long price = 0;
        for (int i = 0; i < MainActivity.cartArrayList.size(); i++) {
            price += MainActivity.cartArrayList.get(i).getPrice();
        }
        totalPrice = String.valueOf(price);

    }


    private void ActioView() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this, MainActivity.class));
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                Date date = new Date();
                date.getTime();
                SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy HH:mm a");
                String newDateStr = postFormater.format(date);
                InfoCustomer infoCustomer = new InfoCustomer(name, phone, email, MainActivity.cartArrayList, totalPrice, newDateStr);
                reference.push().setValue(infoCustomer, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(InformationActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(InformationActivity.this,DetailNotificationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("notification",infoCustomer);
                        PendingIntent pendingIntent = PendingIntent.getActivity(InformationActivity.this,getNotification(),intent,0);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(InformationActivity.this, NotificationApplication.CHANNEL_ID)
                                .setSmallIcon(R.drawable.foot)
                                .setContentTitle("Message")
                                .setContentText("You have a order")
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);
                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(InformationActivity.this);
                        notificationManagerCompat.notify(getNotification(), builder.build());
                    }
                });
                startActivity(new Intent(InformationActivity.this, MainActivity.class));
//                Query checkUser = reference.orderByChild("username").equalTo(name);
//                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
////                                Cart cart =  snapshot.child(name).child("carts").getValue(Cart.class);
////                                MainActivity.cartArrayList.add(cart);
//                        } else {
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }


    private int getNotification() {
        return (int) new Date().getTime();
    }

    private void initView() {
        editTextName = findViewById(R.id.edit_name_customer);
        editTextEmail = findViewById(R.id.edit_email_customer);
        editTextPhone = findViewById(R.id.edit_nphone_customer);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnBack = findViewById(R.id.btn_back);
        arrayList = new ArrayList<>();
        name = editTextName.getText().toString().trim();
        phone = editTextPhone.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        cartArrayList = new ArrayList<>();
    }
}