package com.example.appanimals.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appanimals.Adapter.PhotoAdapter;
import com.example.appanimals.Model.Cart;
import com.example.appanimals.Model.Product;
import com.example.appanimals.R;
import com.example.appanimals.transformer.DepthPageTransformer;
import com.example.appanimals.transformer.ZoomOutPageTransformer;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.common.base.Converter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.Internal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator3;

public class DetailActivity extends AppCompatActivity {
    private Toolbar toolbarDetail;
    private Spinner spinner;
    private ViewPager2 viewPager2;
    private TextView tvDescription,tvPrice,tvAge,tvSex,tvOrigin,tvWeight,tvDateUpdate;
    private Button btnAdd;
    private CircleIndicator3 circleIndicator3;
    private Menu mMenu;
    private boolean isExpanded = true;
    private PhotoAdapter photoAdapter;
    private String name = "";
    private String category = "";
    private Long price;
    private String description;
    private String sex;
    private String origin;
    private String weight;
    private String age;
    private Product product;
    private ArrayList<String> arrayListImage = new ArrayList<>();
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ArrayList<String> arrayList;
    private AppBarLayout appBarLayout;
    private DatabaseReference reference;
    private RadioButton rBtnFavorite;
    private TextView textCartItemCount;
    private int mCartItemCount = MainActivity.cartArrayList.size();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        initView();
        ActionView();
        ActionToolBar();
        GetData();
        CatchEventSpinner();
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)>200){
                    isExpanded =false;
                }
                else {
                    isExpanded = true;
                }
                invalidateOptionsMenu();
            }
        });
    }

    private void ActionView() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.cartArrayList.size() >0 )
                {
                    int newcount = Integer.parseInt(spinner.getSelectedItem().toString());
                    boolean exists = false;
                    for (int i = 0; i < MainActivity.cartArrayList.size(); i++) {
                        if (MainActivity.cartArrayList.get(i).getName().equals(name) && MainActivity.cartArrayList.get(i).getCategory().equals(category))
                        {
                            MainActivity.cartArrayList.get(i).setCount(MainActivity.cartArrayList.get(i).getCount() + newcount);
                            if (MainActivity.cartArrayList.get(i).getCount() >=10)
                            {
                                MainActivity.cartArrayList.get(i).setCount(10);
                            }
                            MainActivity.cartArrayList.get(i).setPrice(price * MainActivity.cartArrayList.get(i).getCount());
                            exists = true;
                        }

                    }
                    if (exists == false)
                    {
                        int countproduct = Integer.parseInt(spinner.getSelectedItem().toString());
                        long newprice = countproduct * price;
                        String capacityNew = category;
                        MainActivity.cartArrayList.add(new Cart(name,newprice,arrayListImage,countproduct,capacityNew));
                    }
                }
                else
                {
                    int countproduct = Integer.parseInt(spinner.getSelectedItem().toString());
                    long newprice = countproduct * price;
                    MainActivity.cartArrayList.add(new Cart(name,newprice,arrayListImage,countproduct,category));

                }
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        rBtnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("Favorite");
                Query checkProduct = reference.orderByChild("name").equalTo(name);
                checkProduct.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            return;
                        }
                        else {
                            reference.child(MainActivity.account.getFullName()).child(name).setValue(product);
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }


    private void GetData() {
        product = (Product) getIntent().getSerializableExtra("informationAnimal");
        tvDescription.setText(product.getDescription());
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        tvPrice.setText("$"+ decimalFormat.format(product.getPrice()));
        name = product.getName().trim();
        category = product.getCategory();
        arrayList = product.getImg();
        sex = product.getSex();
        origin = product.getOrigin();
        weight = product.getWeight();
        arrayListImage = product.getImg();
        price = product.getPrice();
        age = product.getAge();
        description = product.getDescription();
        photoAdapter = new PhotoAdapter(arrayList,getApplicationContext());
        viewPager2.setAdapter(photoAdapter);

        photoAdapter.notifyDataSetChanged();
        circleIndicator3.setViewPager(viewPager2);
        tvAge.setText(product.getAge());
        tvOrigin.setText(product.getOrigin());
        tvSex.setText(product.getSex());
        tvWeight.setText(product.getWeight());
//        tvDateUpdate.setText(product.getDateUpdate());
        viewPager2.setPageTransformer(new DepthPageTransformer());
    }

    private void initView() {
        toolbarDetail = findViewById(R.id.toolbar_detail);
        spinner = findViewById(R.id.spinner_detail);
        viewPager2 = findViewById(R.id.viewpager2_detail);
        tvDescription = findViewById(R.id.tv_description_animal);
        tvPrice = findViewById(R.id.tv_price_detail);
        btnAdd = findViewById(R.id.btn_add_to_cart_detail);
        circleIndicator3 = findViewById(R.id.circl_indicator_3);
        appBarLayout = findViewById(R.id.appbarlayout_detail);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        arrayList = new ArrayList<>();
        tvAge = findViewById(R.id.tv_age_detail);
        tvOrigin = findViewById(R.id.tv_origin_detail);
        tvWeight = findViewById(R.id.tv_weight_detail);
        tvSex = findViewById(R.id.tv_sex_detail);
        rBtnFavorite = findViewById(R.id.rBtn_favorite);
        tvDateUpdate = findViewById(R.id.tv_date_update_product);
    }


    private void ActionToolBar(){
        setSupportActionBar(toolbarDetail);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void CatchEventSpinner(){
        Integer[] count = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,count);
        spinner.setAdapter(arrayAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        mMenu = menu;

        final MenuItem menuItem = menu.findItem(R.id.cartFragment);
        View actionView = menuItem.getActionView();

        textCartItemCount = actionView.findViewById(R.id.cart_badge_text_view);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mMenu != null && (!isExpanded || mMenu.size() != 1)){
            menu.add("Favorite").setIcon(R.drawable.ic_baseline_favorite_24).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            collapsingToolbarLayout.setTitle(name);
//            collapsingToolbarLayout.setContentScrimColor(R.style.Widget_MaterialComponents_CollapsingToolbar);
            collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#ffffff"));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getTitle() == "Favorite"){
//            startActivity(new Intent(DetailActivity.this,CartActivity.class));
//        }
        switch (item.getItemId())
        {
            case R.id.cartFragment:
                startActivity(new Intent(getApplicationContext(),CartActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }



}