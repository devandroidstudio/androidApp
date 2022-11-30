package com.example.appanimals.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.appanimals.Adapter.ExampleBroadcastReceivers;
import com.example.appanimals.Adapter.ViewpagerAdapter;
import com.example.appanimals.Model.Account;
import com.example.appanimals.Model.Cart;
import com.example.appanimals.Model.Product;
import com.example.appanimals.R;
import com.example.appanimals.Util.CheckConnection;
import com.example.appanimals.transformer.DepthPageTransformer;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager2 mViewpager2;
    private ViewpagerAdapter viewpagerAdapter;
    private DatabaseReference reference;
    public static ArrayList<Account> accountArrayList;
    private String name;
    private FloatingActionButton fabCart;
    public static ArrayList<Cart> cartArrayList;
    private ArrayList<Product> productsFavorite;
    public static Account account;
    private ExampleBroadcastReceivers mBroadcastReceivers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (CheckConnection.haveNetworkConnection(MainActivity.this)){
            initView();
            ActionView();

        }
        else {
            CheckConnection.ShowToast_Short(MainActivity.this);
        }

//        Date date = new Date();
//        date.getTime();
//        SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");
//        String newDateStr = postFormater.format(date);
//        Toast.makeText(getApplicationContext(), newDateStr+"", Toast.LENGTH_SHORT).show();
//        reference = FirebaseDatabase.getInstance().getReference("Account");
//        Account account = new Account("admin","nguyen van a","123456789","admin@gmail.com","admin",newDateStr);
//        reference.child("admin").setValue(account);
    }
    private void GetAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = "";
        if (user != null){
            email = user.getEmail();
            Toast.makeText(this, user.isEmailVerified()+"", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(MainActivity.this,IntroActivity.class));
            finishAffinity();
        }

        reference = FirebaseDatabase.getInstance().getReference("Account");
        reference.keepSynced(true);
        Query CheckAccount = reference.orderByChild("email").equalTo(email);
        CheckAccount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot postSnapshot : snapshot.getChildren()){
                        account = postSnapshot.getValue(Account.class);
                    }
                    name = account.getFullName();
                    reference = FirebaseDatabase.getInstance().getReference("Favorite").child(name);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (productsFavorite != null){
                                productsFavorite.clear();
                            }
                            for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                Product product = postSnapshot.getValue(Product.class);
                                productsFavorite.add(product);
                            }
                            BadgeDrawable badgeFavorite = bottomNavigationView.getOrCreateBadge(R.id.bottom_favorite);
                            badgeFavorite.setNumber(productsFavorite.size());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ActionView() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_home:
                        mViewpager2.setCurrentItem(0);
                        break;
                    case R.id.bottom_favorite:
                        mViewpager2.setCurrentItem(1);
                        BadgeDrawable badgeFavorite = bottomNavigationView.getOrCreateBadge(R.id.bottom_favorite);
                        badgeFavorite.setVisible(false);
                        break;
                    case R.id.bottom_service:
                        mViewpager2.setCurrentItem(2);
                        break;
                    case R.id.bottom_person:
                        mViewpager2.setCurrentItem(3);
                        break;
                    default:
                        mViewpager2.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });

        mViewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);
                        bottomNavigationView.getOrCreateBadge(R.id.bottom_home);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_favorite).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_service).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_person).setChecked(true);
                        break;
                }
            }
        });

        fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CartActivity.class));
            }
        });
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        if (!bottomNavigationView.getMenu().getItem(2).isEnabled()){
            bottomNavigationView.setBackground(null);
        }
        mViewpager2 = findViewById(R.id.viewpager2);
        viewpagerAdapter = new ViewpagerAdapter(this);
        mViewpager2.setAdapter(viewpagerAdapter);
        mViewpager2.setPageTransformer(new DepthPageTransformer());
        fabCart = findViewById(R.id.fab_cart);
        cartArrayList = new ArrayList<>();
        accountArrayList = new ArrayList<>();
        if (cartArrayList != null){

        }
        else {
            cartArrayList = new ArrayList<>();
        }
        productsFavorite = new ArrayList<>();
        accountArrayList = new ArrayList<>();
        mBroadcastReceivers = new ExampleBroadcastReceivers();

//        String  userRecord = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
//// See the UserRecord reference doc for the contents of userRecord.
//        System.out.println("Successfully fetched user data: " + userRecord);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBroadcastReceivers,intentFilter);
        GetAccount();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceivers);
        Log.d("Hello", "onDestroy: Hello");
    }
}