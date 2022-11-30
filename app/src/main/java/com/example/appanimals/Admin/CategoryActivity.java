package com.example.appanimals.Admin;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appanimals.Activity.ImageActivity;
import com.example.appanimals.Adapter.CategoryAnimalAdapter;
import com.example.appanimals.Model.Category;
import com.example.appanimals.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CORES = 1;
    private RecyclerView rcvCategory;
    private MaterialToolbar toolbar;
    private TextView tvEmpty;
    private FloatingActionButton fabAddCategory;
    private DatabaseReference reference;
    private ArrayList<Category> categories;
    private CategoryAnimalAdapter adapter;
    private SearchView searchView;
    private Uri imageUri;
    private ImageButton imageButton;
    private StorageReference storageReference;
    private StorageTask mUploadTask;
    private ProgressDialog mProgressDialog;
//    private ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//        @Override
//        public void onActivityResult(Uri result) {
//            if (!result.toString().isEmpty())
//            {
//
//            }
//        }
//    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category);
        initView();
        actionView();
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_product_admin,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.app_bar_search_category_admin).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_admin:
                startActivity(new Intent(CategoryActivity.this, AdminActivity.class));
                finishAffinity();
            case R.id.add_category_admin:
                showDialogAddCategory();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialogAddCategory() {
        final Dialog dialog = new Dialog(CategoryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_category);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.WRAP_CONTENT);

        TextInputLayout textInputLayoutName = dialog.findViewById(R.id.name_category_edit);
        AutoCompleteTextView autoCompleteTextViewStatus = dialog.findViewById(R.id.auto_insert_category_status_edit);
        ArrayList<String> arrayListStatus = new ArrayList<>();
        arrayListStatus.add("On");
        arrayListStatus.add("Off");
        ArrayAdapter<String> arrayAdapterStatus = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayListStatus);
        arrayAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextViewStatus.setAdapter(arrayAdapterStatus);
        autoCompleteTextViewStatus.setText(arrayAdapterStatus.getItem(0),false);


        imageButton = dialog.findViewById(R.id.image_category_admin);


        Button btnImage = dialog.findViewById(R.id.btn_choose_image_category_admin);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onCLickOpenGallery();
            }
        });

        Button btnInsert = dialog.findViewById(R.id.btn_insert_category_edit);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(CategoryActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    insetCategory(textInputLayoutName,autoCompleteTextViewStatus,dialog);
                }

            }
        });
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_category_edit);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void insetCategory(TextInputLayout textInputLayoutName, AutoCompleteTextView autoCompleteTextViewStatus, Dialog dialog) {
        if (imageUri != null)
        {
            dialog.dismiss();
            mProgressDialog.show();
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            mUploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressDialog.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(CategoryActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            Category category = new Category(Objects.requireNonNull(textInputLayoutName.getEditText()).getText().toString().trim(),Objects.requireNonNull(uri.toString()),autoCompleteTextViewStatus.getText().toString());
                            reference.child(textInputLayoutName.getEditText().getText().toString().trim()).setValue(category);
                            mProgressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.show();
                            Toast.makeText(CategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    mProgressDialog.setProgress((int)progress);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    private void onCLickOpenGallery() {
        if (getApplicationContext() == null){
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery();
        }
        if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }
        else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            CategoryActivity.this.requestPermissions(permission,MY_REQUEST_CORES);
        }
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, MY_REQUEST_CORES);
//        activityResultLauncher.launch("image/*");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != RESULT_OK)
        {
            Toast.makeText(this, "Please allow", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CORES && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.error).into(imageButton);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }






    private void getData() {
        if (!categories.isEmpty())
        {
            tvEmpty.setVisibility(View.INVISIBLE);
        }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (categories != null)
                {
                    categories.clear();
                }
                for (DataSnapshot postSnapshot : snapshot.getChildren())
                {
                    Category category = postSnapshot.getValue(Category.class);
                    categories.add(category);
                }
                adapter = new CategoryAnimalAdapter(categories,getApplicationContext());
                rcvCategory.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actionView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fabAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddCategory();
            }
        });


    }

    private void initView() {
        rcvCategory = findViewById(R.id.rcv_category_admin);
        rcvCategory.setLayoutManager(new LinearLayoutManager(this));
        rcvCategory.setHasFixedSize(true);
        categories = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar_category_admin);
//        tvEmpty = findViewById(R.id.tv_empty_category_admin);
        fabAddCategory = findViewById(R.id.fab_add_category_admin);
        reference = FirebaseDatabase.getInstance().getReference("Category");
        reference.keepSynced(true);
        storageReference = FirebaseStorage.getInstance().getReference("Category");
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Uploading");
        if (imageButton != null)
        {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(CategoryActivity.this, ImageActivity.class);
//                    intent.putExtra("InfoImage",imageUri);
//                    startActivity(intent);
                    Toast.makeText(CategoryActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}