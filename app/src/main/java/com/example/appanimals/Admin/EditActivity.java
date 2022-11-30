package com.example.appanimals.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appanimals.Admin.Adapter.PhotoAdminAdapter;
import com.example.appanimals.Admin.Adapter.PhotoArrayAdminAdapter;
import com.example.appanimals.Model.Category;
import com.example.appanimals.Model.Product;
import com.example.appanimals.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class EditActivity extends AppCompatActivity {
    private static final int MY_CORE_IMAGE = 1;
    private AutoCompleteTextView autoCompleteTextViewKind,autoCompleteTextViewSex,autoCompleteTextViewAge,autoCompleteTextViewWeight;
    private TextInputLayout editName,editPrice,editOrigin,editDescription,editDate,editAge,editWeight;
    private ImageButton iBtnDate;
    private Button btnUpdateEdit,btnOpen;
    private MaterialToolbar toolbar;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private Uri imageUri;
    private ArrayList<Uri> arrayListUrlLocal;
    private ArrayList<String> arrayListFirebase,arrayListImageCurrent;
    private ViewPager2 viewPager2;
    private String sex = "";
    private String age = "";
    private String name = "";
    private String kind = "";
    private String weight = "";
    private String date = "";
    private long price;
    private String origin = "";
    private String description = "";
    private String status = "";
    private PhotoAdminAdapter adapter;
    private PhotoArrayAdminAdapter adapterArray;
    private ProgressDialog mpProgressDialog;
    private ArrayList<Category> categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.activity_edit);
        initView();
        ActionView();
        ActionBar();
        AutoView();
        GetDataCategory();
//        GetData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.sile_out_right,R.anim.sile_in_right);
    }

    private void GetDataCategory() {
        ArrayList<String> arrayList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Category");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren())
                {
                    Category category = postSnapshot.getValue(Category.class);
                    if (category != null && category.getStatus().equals("On"))
                    {
                        arrayList.add(category.getName());
                    }

                }
                ArrayAdapter<String> arrayAdapterKind = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList);
                arrayAdapterKind.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                autoCompleteTextViewKind.setAdapter(arrayAdapterKind);
                autoCompleteTextViewKind.setText(arrayAdapterKind.getItem(0),false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


//    private void GetData() {
//        Product product = (Product) getIntent().getSerializableExtra("informationAnimalEdit");
//        if (product != null){
//            editName.getEditText().setText(product.getName());
//            editDate.getEditText().setText(product.getDateUpdate());
//            editDescription.getEditText().setText(product.getDescription());
//            editOrigin.getEditText().setText(product.getOrigin());
//            DecimalFormat decimalFormat = new DecimalFormat("###,###");
//            editPrice.getEditText().setText("$"+decimalFormat.format(product.getPrice()));
//            autoCompleteTextViewKind.setText(product.getCategory());
//            autoCompleteTextViewWeight.setText(product.getWeight());
//            autoCompleteTextViewAge.setText(product.getAge());
//            autoCompleteTextViewSex.setText(product.getSex());
//            adapterArray = new PhotoArrayAdminAdapter(product.getImg(),getApplicationContext());
//            viewPager2.setAdapter(adapterArray);
//            arrayListImageCurrent = product.getImg();
//        }
//        else {
//        }
//
//    }

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

    private void AutoView() {

        ArrayList<String> arrayListSex = new ArrayList<>();
        arrayListSex.add("Male");
        arrayListSex.add("Female");
        ArrayAdapter<String> arrayAdapterSex = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayListSex);
        arrayAdapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextViewSex.setAdapter(arrayAdapterSex);
        autoCompleteTextViewSex.setText(arrayAdapterSex.getItem(0),false);

        ArrayList<Integer> arrayListNumber = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            arrayListNumber.add(i);
        }

        ArrayAdapter<Integer> arrayAdapterNumber = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayListNumber);
        arrayAdapterNumber.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextViewAge.setAdapter(arrayAdapterNumber);
        autoCompleteTextViewAge.setText(String.valueOf(arrayAdapterNumber.getItem(0)),false);
        autoCompleteTextViewWeight.setAdapter(arrayAdapterNumber);
        autoCompleteTextViewWeight.setText(String.valueOf(arrayAdapterNumber.getItem(0)),false);

        autoCompleteTextViewWeight.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditActivity.this);
                builder.setTitle("Chooses");
                final String [] ages = {"kg","g"};
                final int countAge = 2;
                final Set<String> selectedItems = new HashSet<String>();
                builder.setCancelable(true);
                builder.setSingleChoiceItems(ages, countAge, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedItems.clear();
                        selectedItems.add(ages[i]);
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedItems.isEmpty())
                        {
                            Toast.makeText(EditActivity.this, "Please chooses weight of pet", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        dialog.dismiss();
                        String checkedWeight = selectedItems.iterator().next();
                        editWeight.setSuffixText(checkedWeight);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });

        autoCompleteTextViewAge.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditActivity.this);
                builder.setTitle("Chooses");
                final String [] ages = {"month","year"};
                final int countAge = 2;
                final Set<String> selectedItems = new HashSet<String>();
                builder.setCancelable(true);
                builder.setSingleChoiceItems(ages, countAge, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedItems.clear();
                        selectedItems.add(ages[i]);
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedItems.isEmpty())
                        {
                            Toast.makeText(EditActivity.this, "Please chooses age of pet", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        dialog.dismiss();
                        String checkedAge = selectedItems.iterator().next();
                        editAge.setSuffixText(checkedAge);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;

            }
        });

    }

    private void ActionView() {
        btnUpdateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < arrayListUrlLocal.size(); i++) {
                    if (arrayListUrlLocal.get(i) != null) {
                        uploadToFirebase(arrayListUrlLocal.get(i));
                    } else {
                        Toast.makeText(EditActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select A Date");
        builder.setCalendarConstraints(constraintBuilder.build());
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        final MaterialDatePicker<Long> materialDatePicker = builder.build();
        iBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"Date_Picker");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Objects.requireNonNull(editDate.getEditText()).setText(materialDatePicker.getHeaderText());
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , MY_CORE_IMAGE);
            }
        });
    }

    private void uploadToFirebase(Uri uri){
        reference = FirebaseDatabase.getInstance().getReference("Animal");
        final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        sex = autoCompleteTextViewSex.getText().toString();
        age = autoCompleteTextViewAge.getText().toString().concat("\t"+Objects.requireNonNull(editAge.getSuffixTextView()).getText().toString());
        weight = autoCompleteTextViewWeight.getText().toString().concat(Objects.requireNonNull(editWeight.getSuffixTextView()).getText().toString());
        name = Objects.requireNonNull(editName.getEditText()).getText().toString();
        kind = autoCompleteTextViewKind.getText().toString();
        date = editDate.getEditText().getText().toString();
        price = Long.parseLong(Objects.requireNonNull(editPrice.getEditText()).getText().toString());
        origin = Objects.requireNonNull(editOrigin.getEditText()).getText().toString();
        status = "On";
        description = Objects.requireNonNull(editDescription.getEditText()).getText().toString();
        mpProgressDialog.show();
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String modelId = reference.push().getKey();
//                        imageView.setImageResource(R.drawable.ic_launcher_foreground);
                        arrayListFirebase.add(uri.toString());
                        if (arrayListUrlLocal.size() == arrayListFirebase.size()){
                            Product product = new Product(name,arrayListFirebase,price,description,kind,sex,origin,age,weight,status,date);
                            reference.child(kind).child(name).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EditActivity.this, AnimalEditActivity.class);
                                    intent.putExtra("name",kind);
                                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(EditActivity.this).toBundle());
                                    finishAffinity();

                                }
                            });
                            mpProgressDialog.dismiss();
                            arrayListFirebase.clear();
                        }
                    }
                });

//                String modelId = reference.push().getKey();
//                Toast.makeText(EditActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
////                        imageView.setImageResource(R.drawable.ic_launcher_foreground);
//                arrayListFirebase.add(taskSnapshot.getUploadSessionUri().toString());
//                if (arrayListUrlLocal.size() == arrayListFirebase.size()){
//
//                    mpProgressDialog.dismiss();
//                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                mpProgressDialog.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mpProgressDialog.dismiss();
                Toast.makeText(EditActivity.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_CORE_IMAGE && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            arrayListUrlLocal.add(imageUri);
            adapter = new PhotoAdminAdapter(getApplicationContext(),arrayListUrlLocal);
            viewPager2.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        autoCompleteTextViewKind = findViewById(R.id.category_animal_edit);
        autoCompleteTextViewSex = findViewById(R.id.sex_animal_edit);
        autoCompleteTextViewAge = findViewById(R.id.age_animal_edit);
        autoCompleteTextViewWeight = findViewById(R.id.weight_animal_edit);
        editName = findViewById(R.id.name_animal_edit);
        editPrice = findViewById(R.id.price_animal_edit);
        editOrigin = findViewById(R.id.origin_animal_edit);
        editDescription = findViewById(R.id.description_animal_edit);
        editDate = findViewById(R.id.date_picker_edit);
        iBtnDate = findViewById(R.id.img_date_picker_edit);
        btnUpdateEdit = findViewById(R.id.btn_update_edit);
        toolbar = findViewById(R.id.toolbar_edit);
        btnOpen = findViewById(R.id.choose_file);
        storageReference = FirebaseStorage.getInstance().getReference("Animal");
        arrayListUrlLocal = new ArrayList<>();
        arrayListFirebase = new ArrayList<>();
        arrayListImageCurrent = new ArrayList<>();
        categories = new ArrayList<>();
        editAge = findViewById(R.id.textInputAge);
        editWeight = findViewById(R.id.textInputWeight);
        viewPager2 = findViewById(R.id.viewpager2_edit);
        String currentDate = new SimpleDateFormat("MMMM dd,yyyy", Locale.getDefault()).format(new Date());
        Objects.requireNonNull(editDate.getEditText()).setText(currentDate);
        mpProgressDialog = new ProgressDialog(this);
        mpProgressDialog.setMessage("Uploading");
    }
}