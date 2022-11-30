package com.example.appanimals.Fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appanimals.Activity.CartActivity;
import com.example.appanimals.Activity.MainActivity;
import com.example.appanimals.Model.Scheduling;
import com.example.appanimals.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }
    private View view;
    private AutoCompleteTextView autoCompleteTextViewTypeOfProcedure, autoCompleteTextViewSex,autoCompleteTextViewAge,autoCompleteTextViewProcedure;
    private Button btnSingUpScheduling;
    private ImageButton imgDatePicker,imgTimePicker;
    private TextInputLayout editDate,editTime,editName,editKind;
    private DatabaseReference reference;
    @Override
    public void onResume() {
        super.onResume();
        autoCompleteTextViewTypeOfProcedure.setText("");
        autoCompleteTextViewProcedure.setText("");
        autoCompleteTextViewSex.setText("");
        autoCompleteTextViewAge.setText("");
        editTime.getEditText().setText("");
        editDate.getEditText().setText("");
        editName.getEditText().setText("");
        editKind.getEditText().setText("");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initView();
        ActionView();
        AutoView();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(0,"Hi");
        arrayList.add("Hello");
        arrayList.add("Hey");
        arrayList.add("What up");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextViewTypeOfProcedure.setAdapter(arrayAdapter);
        return view;
    }

    private void AutoView() {
        ArrayList<String> arrayListSex = new ArrayList<>();
        arrayListSex.add("Male");
        arrayListSex.add("Female");
        ArrayAdapter<String> arrayAdapterSex = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,arrayListSex);
        arrayAdapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextViewSex.setAdapter(arrayAdapterSex);

        Integer[] Age = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> arrayAdapterAge = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,Age);
        arrayAdapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextViewAge.setAdapter(arrayAdapterAge);

    }

    private void ActionView() {
        reference = FirebaseDatabase.getInstance().getReference("Scheduling");
        btnSingUpScheduling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeOfProcedure = autoCompleteTextViewTypeOfProcedure.getText().toString();
                String procedure = autoCompleteTextViewProcedure.getText().toString();
                String sex = autoCompleteTextViewSex.getText().toString();
                String age = autoCompleteTextViewAge.getText().toString();
                String name = editName.getEditText().getText().toString();
                String kind = editKind.getEditText().getText().toString();
                String date = editDate.getEditText().getText().toString();
                String time = editTime.getEditText().getText().toString();
                Scheduling scheduling = new Scheduling(MainActivity.account.getFullName(),typeOfProcedure,procedure,name,sex,age,kind,date,time);
                reference.child(MainActivity.account.getFullName()).setValue(scheduling);
            }
        });
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select A Date");
        builder.setCalendarConstraints(constraintBuilder.build());
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        final MaterialDatePicker materialDatePicker = builder.build();
        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getChildFragmentManager(),"Date_Picker");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                editDate.getEditText().setText(materialDatePicker.getHeaderText());
            }
        });

        MaterialTimePicker.Builder builderTime = new MaterialTimePicker.Builder();
        builderTime.setTitleText("Select Time");
        builderTime.setTimeFormat(TimeFormat.CLOCK_12H);
        builderTime.setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD);
        final MaterialTimePicker materialTimePicker = builderTime.build();
        imgTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTimePicker.show(getChildFragmentManager(),"Time_Picker");
            }
        });
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (materialTimePicker.getHour() <12){
                    editTime.getEditText().setText(materialTimePicker.getHour()+":"+materialTimePicker.getMinute()+" " +"AM");
                }
                else {
                    editTime.getEditText().setText(materialTimePicker.getHour()+":"+materialTimePicker.getMinute()+" " +"PM");
                }
            }
        });
    }

    private void initView() {
        autoCompleteTextViewTypeOfProcedure = view.findViewById(R.id.type_of_procedure);
        autoCompleteTextViewProcedure = view.findViewById(R.id.procedure);
        autoCompleteTextViewSex = view.findViewById(R.id.sex_animal);
        autoCompleteTextViewAge = view.findViewById(R.id.age_animal);
        editName = view.findViewById(R.id.name_animal_scheduling);
        editKind = view.findViewById(R.id.category_animal);
        editDate = view.findViewById(R.id.date_picker_scheduling);
        editTime = view.findViewById(R.id.time_picker_scheduling);
        btnSingUpScheduling = view.findViewById(R.id.btn_sing_up_scheduling);
        imgDatePicker = view.findViewById(R.id.img_date_picker);
        imgTimePicker = view.findViewById(R.id.img_time_picker);
    }

}