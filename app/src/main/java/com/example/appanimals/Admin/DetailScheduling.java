package com.example.appanimals.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.appanimals.Model.Scheduling;
import com.example.appanimals.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailScheduling extends AppCompatActivity {
    private TextInputLayout editTypeOfProcedure,editProcedure,editKind,editSex,editAge,editDate,editTime,editAnimal,editNameCustomer;
    private MaterialToolbar toolbar;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_scheduling);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        Actionbar();
        GetData();
    }

    private void GetData() {
        Scheduling scheduling = (Scheduling) getIntent().getSerializableExtra("Scheduling");
        editTypeOfProcedure.getEditText().setText(scheduling.getTypeOfProcedure());
        editProcedure.getEditText().setText(scheduling.getProcedure());
        editKind.getEditText().setText(scheduling.getKind());
        editSex.getEditText().setText(scheduling.getSex());
        editDate.getEditText().setText(scheduling.getDate());
        editAge.getEditText().setText(scheduling.getAge());
        editTime.getEditText().setText(scheduling.getTime());
        editAnimal.getEditText().setText(scheduling.getName());
        editNameCustomer.getEditText().setText(scheduling.getNameCustomer());
    }

    private void Actionbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        editTypeOfProcedure = findViewById(R.id.type_of_procedure_admin);
        editProcedure = findViewById(R.id.procedure_admin);
        editKind = findViewById(R.id.category_animal);
        editSex = findViewById(R.id.sex_scheduling_admin);
        editAge = findViewById(R.id.age_scheduling_admin);
        editDate = findViewById(R.id.date_picker_scheduling_admin);
        editTime = findViewById(R.id.time_picker_scheduling_admin);
        editAnimal = findViewById(R.id.name_animal_scheduling);
        toolbar = findViewById(R.id.toolbar_detail_schedule_admin);
        editNameCustomer = findViewById(R.id.name_customer_detail_scheduling_admin);
    }
}