package com.example.appanimals.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.appanimals.Model.InfoCustomer;
import com.example.appanimals.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

public class DetailNotificationActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private TextInputLayout editCustomer,editPhone,editEmail,editAnimal,editCount,editTotalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.activity_detail_notification);
        initView();
        ActionBar();
        getData();
    }
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getData() {
        InfoCustomer infoCustomer = (InfoCustomer) getIntent().getSerializableExtra("notification");
        editCustomer.getEditText().setText(infoCustomer.getUsername());
        editPhone.getEditText().setText(infoCustomer.getPhone());
        editEmail.getEditText().setText(infoCustomer.getEmail());
        editAnimal.getEditText().setText("Animal:");
        for (int i = 0; i < infoCustomer.getCarts().size(); i++) {
            editAnimal.getEditText().append(" "+infoCustomer.getCarts().get(i).getName());
            editCount.getEditText().setText("Count: "+infoCustomer.getCarts().size());
        }
        editTotalPrice.getEditText().setText("$"+infoCustomer.getTotalPrice());

    }
    private void initView() {
        editCustomer = findViewById(R.id.tv_name_customer_notification);
        editPhone = findViewById(R.id.tv_phone_notification);
        editEmail = findViewById(R.id.tv_email_nofication);
        editAnimal = findViewById(R.id.tv_animal_notification);
        editCount = findViewById(R.id.tv_count_notification);
        editTotalPrice = findViewById(R.id.tv_price_notification);
        toolbar = findViewById(R.id.toolbar_detail_notification);
    }
}