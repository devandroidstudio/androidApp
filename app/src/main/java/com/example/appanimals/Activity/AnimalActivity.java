package com.example.appanimals.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.appanimals.Adapter.AnimalAdapter;
import com.example.appanimals.Model.Category;
import com.example.appanimals.Model.Product;
import com.example.appanimals.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnimalActivity extends AppCompatActivity {
    private RecyclerView rcvAnimal;
    private AnimalAdapter animalAdapter;
    private ArrayList<Product> productArrayList;
    private String name = "";
    private DatabaseReference reference;
    private MaterialToolbar toolBar;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_animal);
        initView();
        GetDataName();
        ActionTollBar();
    }
    private void ActionTollBar(){
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_animal,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
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
            case R.id.menu_shopping:
                startActivity(new Intent(AnimalActivity.this,CartActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    private void GetDataName() {
        reference = FirebaseDatabase.getInstance().getReference("Animal");
        Category category = (Category) getIntent().getSerializableExtra("Category");
        name = category.getName();
        toolBar.setTitle(name);
        Query checkCategory = reference.orderByChild("category").equalTo(name);
        checkCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Product product = postSnapshot.getValue(Product.class);
                    productArrayList.add(product);
                }
                animalAdapter = new AnimalAdapter(productArrayList,AnimalActivity.this);
                rcvAnimal.setAdapter(animalAdapter);
                animalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initView() {
        rcvAnimal = findViewById(R.id.rcv_animal);
        rcvAnimal.setLayoutManager(new LinearLayoutManager(AnimalActivity.this));
        toolBar = findViewById(R.id.toolbar_animal);
        productArrayList = new ArrayList<>();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getApplicationContext(),R.anim.layout_up_to_down);
        rcvAnimal.setLayoutAnimation(layoutAnimationController);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animalAdapter != null){
            animalAdapter.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animalAdapter != null)
        {
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
}