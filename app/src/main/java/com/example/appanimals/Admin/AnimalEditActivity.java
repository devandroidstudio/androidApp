package com.example.appanimals.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.appanimals.Activity.AnimalActivity;
import com.example.appanimals.Activity.CartActivity;
import com.example.appanimals.Adapter.AnimalAdapter;
import com.example.appanimals.Adapter.CategoryAnimalAdapter;
import com.example.appanimals.Adapter.PhotoAdapter;
import com.example.appanimals.Admin.Adapter.PhotoAdminAdapter;
import com.example.appanimals.Admin.Adapter.ProductAdminAdapter;
import com.example.appanimals.Admin.Adapter.RecyclerViewitemtouchhelper;
import com.example.appanimals.Model.Category;
import com.example.appanimals.Model.ItemTouchHelperListener;
import com.example.appanimals.Model.Product;
import com.example.appanimals.R;
import com.example.appanimals.transformer.DepthPageTransformer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AnimalEditActivity extends AppCompatActivity implements ItemTouchHelperListener, ProductAdminAdapter.ISendData {
    private static final int MY_CORE_IMAGE_DIALOG = 1;
    private MaterialToolbar toolbar;
    private AppBarLayout appBarLayout;
    private ArrayList<Product> productArrayList;
    private RecyclerView rcvAdmin;
    private NestedScrollView rootView;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private ProductAdminAdapter animalAdapter;
    private CategoryAnimalAdapter adapter;
    private SearchView searchView;
    private FloatingActionButton fabAddProduct;
    private ArrayList<Category> categories;
    private ArrayList<String> arrayListCurrent,arrayListCurrentDialog;
    private ArrayList<Uri> arrayListUri,arrayListUriUpdate;
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
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_animal_edit);
        initView();
        ActionBar();
        GetData();
        isExpand();
    }
    private void isExpand() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)>150)
                {
                    fabAddProduct.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.uptodown);
                    fabAddProduct.setAnimation(animation);
                }
                else {
                    fabAddProduct.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void GetData() {
        String strName = getIntent().getStringExtra("name");
        toolbar.setTitle(strName);
        reference = FirebaseDatabase.getInstance().getReference("Animal").child(strName);
        reference.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (productArrayList != null)
                {
                    productArrayList.clear();
                }
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Product product = postSnapshot.getValue(Product.class);
                    if (product != null)
                    {
                        if (product.getStatus().equals("On"))
                        {
                            ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewitemtouchhelper(0,ItemTouchHelper.LEFT,AnimalEditActivity.this);
                            new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvAdmin);
                        }
                        productArrayList.add(product);
                    }

                }
                animalAdapter = new ProductAdminAdapter(productArrayList,AnimalEditActivity.this,AnimalEditActivity.this);
                rcvAdmin.setAdapter(animalAdapter);
                animalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AnimalEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(fabAddProduct,"fab_add_product_trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AnimalEditActivity.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.app_bar_search_admin).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                animalAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                animalAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_admin:
                startActivity(new Intent(AnimalEditActivity.this, AdminActivity.class));
                overridePendingTransition(R.anim.sile_in_right,R.anim.sile_out_right);
                break;
            case R.id.add_product:
                startActivity(new Intent(AnimalEditActivity.this, EditActivity.class));
                overridePendingTransition(R.anim.sile_in_right,R.anim.sile_out_right);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar_admin);
        rcvAdmin = findViewById(R.id.rcv_admin);
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getApplicationContext(),R.anim.layout_up_to_down);
        rcvAdmin.setLayoutAnimation(layoutAnimationController);
        productArrayList = new ArrayList<>();
        rcvAdmin.setLayoutManager(new LinearLayoutManager(this));
        rootView = findViewById(R.id.layout_showView);
        fabAddProduct = findViewById(R.id.fab_add_product_admin);
        categories = new ArrayList<>();
        arrayListCurrent = new ArrayList<>();
        arrayListUri = new ArrayList<>();
        storageReference = FirebaseStorage.getInstance().getReference("Animal");
        mProgressDialog = new ProgressDialog(this);
        arrayListCurrentDialog = new ArrayList<>();
        appBarLayout = findViewById(R.id.app_bar_animal_edit);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof ProductAdminAdapter.ProductAdminViewHolder) {
            String nameUserDelete = productArrayList.get(viewHolder.getLayoutPosition()).getName();
            Product productDelete = productArrayList.get(viewHolder.getLayoutPosition());
            int indexDelete = viewHolder.getLayoutPosition();
            animalAdapter.updateItem(indexDelete,nameUserDelete,productDelete);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(getIntent());
                }
            },3000);
            Snackbar snackbar = Snackbar.make(rootView,nameUserDelete +"stopped!",Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animalAdapter.undoItem(productDelete,indexDelete,nameUserDelete);
//                    if (indexDelete == 0 || indexDelete == productArrayList.size() - 1)
//                    {
//                        rcvAdmin.scrollToPosition(indexDelete);
//                    }
                    finish();
                    startActivity(getIntent());
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animalAdapter != null){
            animalAdapter.release();
        }
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void sendDataProduct(Product product) {
        final Dialog dialog = new Dialog(AnimalEditActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_animal);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        AutoCompleteTextView autoCompleteTextViewKind = dialog.findViewById(R.id.category_animal_edit_dialog);
        AutoCompleteTextView autoCompleteTextViewSex = dialog.findViewById(R.id.sex_animal_edit_dialog);
        AutoCompleteTextView autoCompleteTextViewAge = dialog.findViewById(R.id.age_animal_edit_dialog);
        AutoCompleteTextView autoCompleteTextViewWeight = dialog.findViewById(R.id.weight_animal_edit_dialog);

        TextInputLayout editName = dialog.findViewById(R.id.name_animal_edit_dialog);
        TextInputLayout editPrice = dialog.findViewById(R.id.price_animal_edit);
        TextInputLayout editOrigin = dialog.findViewById(R.id.origin_animal_edit_dialog);
        TextInputLayout editDescription = dialog.findViewById(R.id.description_animal_edit_dialog);
        TextInputLayout editDate = dialog.findViewById(R.id.date_picker_edit_dialog);
        TextInputLayout editAge = dialog.findViewById(R.id.textInputAge_dialog);
        TextInputLayout editWeight = dialog.findViewById(R.id.textInputWeight_dialog);

        Objects.requireNonNull(editName.getEditText()).setText(product.getName());
        Objects.requireNonNull(editPrice.getEditText()).setText(String.valueOf(product.getPrice()));
        Objects.requireNonNull(editOrigin.getEditText()).setText(product.getOrigin());
        Objects.requireNonNull(editDescription.getEditText()).setText(product.getDescription());
        Objects.requireNonNull(editDate.getEditText()).setText(product.getDateUpdate());
        autoCompleteTextViewKind.setText(product.getCategory());
        autoCompleteTextViewAge.setText(product.getAge());
        autoCompleteTextViewSex.setText(product.getSex());
        autoCompleteTextViewWeight.setText(product.getWeight());

        autoView(autoCompleteTextViewSex,autoCompleteTextViewAge,autoCompleteTextViewWeight,editAge,editWeight);

        ImageButton btnChooseDate = dialog.findViewById(R.id.img_date_picker_edit_dialog);
        Button btnChooseImage = dialog.findViewById(R.id.choose_file_dialog);
        Button btnUpdate = dialog.findViewById(R.id.btn_update_edit_dialog);
        ViewPager2 viewPager2 = dialog.findViewById(R.id.viewpager2_edit_dialog);
        actionView(btnChooseImage,btnChooseDate,editDate,viewPager2);
        arrayListCurrent = product.getImg();
        PhotoAdapter adapter = new PhotoAdapter(arrayListCurrent,dialog.getContext());
        viewPager2.setAdapter(adapter);
        viewPager2.setPageTransformer(new DepthPageTransformer());
        adapter.notifyDataSetChanged();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayListUri.size() >0)
                {
                    for (int i = 0; i < arrayListUri.size(); i++) {
                        uploadToFirebase(arrayListUri.get(i),autoCompleteTextViewAge,autoCompleteTextViewWeight,autoCompleteTextViewKind,autoCompleteTextViewSex,editAge,editWeight,editDate,editPrice,editDescription,editOrigin,editName,product);
                        arrayListCurrent.remove(arrayListCurrent.size() -1);
                        dialog.dismiss();
                    }
                }
                else {
                    uploadToFirebase(null,autoCompleteTextViewAge,autoCompleteTextViewWeight,autoCompleteTextViewKind,autoCompleteTextViewSex,editAge,editWeight,editDate,editPrice,editDescription,editOrigin,editName,product);
                    dialog.dismiss();
                }


            }
        });
        dialog.show();
    }

    private void actionView(Button btnChooseImage, ImageButton btnChooseDate, TextInputLayout editDate, ViewPager2 viewPager2) {
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select A Date");
        builder.setCalendarConstraints(constraintBuilder.build());
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        final MaterialDatePicker<Long> materialDatePicker = builder.build();
        btnChooseDate.setOnClickListener(new View.OnClickListener() {
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

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCLickOpenGallery();
            }
        });
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
            AnimalEditActivity.this.requestPermissions(permission,MY_CORE_IMAGE_DIALOG);
        }
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, MY_CORE_IMAGE_DIALOG);
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
        if (requestCode == MY_CORE_IMAGE_DIALOG && resultCode == RESULT_OK && data != null){
            arrayListUri.add(data.getData());
            arrayListCurrent.add(data.getData().toString());
        }
    }

    private void autoView(@NonNull AutoCompleteTextView autoCompleteTextViewSex, AutoCompleteTextView autoCompleteTextViewAge, AutoCompleteTextView autoCompleteTextViewWeight, TextInputLayout editAge, TextInputLayout editWeight) {
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
                AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AnimalEditActivity.this);
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
                            Toast.makeText(AnimalEditActivity.this, "Please chooses weight of pet", Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AnimalEditActivity.this);
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
                            Toast.makeText(AnimalEditActivity.this, "Please chooses age of pet", Toast.LENGTH_SHORT).show();
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

    private void uploadToFirebase(Uri uri, AutoCompleteTextView autoCompleteTextViewAge, AutoCompleteTextView autoCompleteTextViewWeight, AutoCompleteTextView autoCompleteTextViewKind, AutoCompleteTextView autoCompleteTextViewSex, TextInputLayout editAge, TextInputLayout editWeight, TextInputLayout editDate, TextInputLayout editPrice, TextInputLayout editDescription, TextInputLayout editOrigin, TextInputLayout editName, Product product){
        reference = FirebaseDatabase.getInstance().getReference("Animal");
        sex = autoCompleteTextViewSex.getText().toString();
        age = autoCompleteTextViewAge.getText().toString().concat(Objects.requireNonNull(editAge.getSuffixTextView()).getText().toString());
        weight = autoCompleteTextViewWeight.getText().toString().concat(Objects.requireNonNull(editWeight.getSuffixTextView()).getText().toString());
        name = Objects.requireNonNull(editName.getEditText()).getText().toString();
        kind = autoCompleteTextViewKind.getText().toString();
        date = Objects.requireNonNull(editDate.getEditText()).getText().toString();
        price = Long.parseLong(Objects.requireNonNull(editPrice.getEditText()).getText().toString());
        origin = Objects.requireNonNull(editOrigin.getEditText()).getText().toString();
        status = "On";
        String priceCurrent = String.valueOf(product.getPrice());
        description = Objects.requireNonNull(editDescription.getEditText()).getText().toString();
        if (uri ==  null)
        {
            if (!product.getName().equals(name) || !product.getStatus().equals(status) || !product.getAge().equals(age) || !product.getCategory().equals(kind) || !product.getDescription().equals(description) || !product.getDateUpdate().equals(date) || !product.getOrigin().equals(origin) || !product.getSex().equals(sex) || !product.getWeight().equals(weight) || !product.getImg().equals(arrayListCurrent) || !String.valueOf(product.getPrice()).equals(String.valueOf(price)))
            {
                product.setName(name);
                product.setPrice(price);
                product.setImg(arrayListCurrent);
                product.setStatus(status);
                product.setAge(age);
                product.setDescription(description);
                product.setDateUpdate(date);
                product.setOrigin(origin);
                product.setSex(sex);
                product.setWeight(weight);
                product.setStatus(status);
                reference.child(kind).child(name).updateChildren(product.NotUriToMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AnimalEditActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressDialog.dismiss();
                        Toast.makeText(AnimalEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else {
                Toast.makeText(this, "Information of animal not change", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }


        }
        else
        {
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            mProgressDialog.show();
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String modelId = reference.push().getKey();
//                        imageView.setImageResource(R.drawable.ic_launcher_foreground);
//                        arrayListCurrentDialog.add(uri.toString());
//                        if (arrayListUri.size() == arrayListCurrentDialog.size()){
//
//
//                        }
                            arrayListCurrent.add(uri.toString());
//                            product.setName(name);
//                            product.setPrice(price);
//                            product.setImg(arrayListCurrent);
//                            product.setStatus(status);
//                            product.setAge(age);
//                            product.setDescription(description);
//                            product.setDateUpdate(date);
//                            product.setOrigin(origin);
//                            product.setSex(sex);
//                            product.setWeight(weight);
//                            product.setStatus(status);
                            Product product = new Product(name,arrayListCurrent,price,description,kind,sex,origin,age, AnimalEditActivity.this.weight,status,date);
                            if (product.getName().equals(name))
                            {
                                reference.child(kind).child(name).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AnimalEditActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();
                                    }
                                });
                                arrayListCurrent.clear();
                            }
                            else {
                                mProgressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(AnimalEditActivity.this);
                                builder.setTitle("Warning");
                                builder.setMessage("Name of animal was changed. Do you want create an animal?");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mProgressDialog.show();
                                        reference.child(kind).child(name).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(AnimalEditActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                mProgressDialog.dismiss();
                                            }
                                        });
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
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
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    mProgressDialog.setMessage("Loading " + (int) progress+"%");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AnimalEditActivity.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }



    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}