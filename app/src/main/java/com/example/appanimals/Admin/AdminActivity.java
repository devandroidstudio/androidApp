package com.example.appanimals.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appanimals.Activity.IntroActivity;
import com.example.appanimals.Activity.MainActivity;
//import com.example.appanimals.Admin.Adapter.OrderAdapter;
//import com.example.appanimals.Admin.Adapter.RecyclerViewitemtouchhelperOrder;
import com.example.appanimals.Fragment.SingInFragment;
import com.example.appanimals.Model.InfoCustomer;
import com.example.appanimals.Model.ItemTouchHelperListener;
import com.example.appanimals.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements ItemTouchHelperListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final float END_SCALE = 0.7f;
    private CoordinatorLayout contentView;
//    private OrderAdapter orderAdapter;
    private RecyclerView rcvOrder;
    private DatabaseReference reference;
    private ArrayList<InfoCustomer> arrayList;
    private NestedScrollView rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin);
        initView();
        ActionBar();
        GetDataOrder();
    }

    private void GetDataOrder() {
        reference = FirebaseDatabase.getInstance().getReference("InformationCustomer");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (arrayList != null){
                    arrayList.clear();
                }
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    InfoCustomer infoCustomer = postSnapshot.getValue(InfoCustomer.class);
                    arrayList.add(infoCustomer);
                }
                orderAdapter = new OrderAdapter(arrayList, AdminActivity.this, new OrderAdapter.IClickShowDataOrderListener() {
                    @Override
                    public void onClickShowDataOrder(InfoCustomer infoCustomer) {
                        final Dialog dialog = new Dialog(AdminActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.layout_dialog);
                        Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.WRAP_CONTENT);
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCancelable(true);
                        TextView tvName = dialog.findViewById(R.id.tv_name_detail_order_admin);
                        TextView tvPhone = dialog.findViewById(R.id.tv_phone_detail_order_admin);
                        TextView tvEmail = dialog.findViewById(R.id.tv_email_detail_order_admin);
                        TextView tvAnimal = dialog.findViewById(R.id.tv_animal_detail_order_admin);
                        TextView tvTotalPrice = dialog.findViewById(R.id.tv_price_order_animal_admin);
                        TextView tvDate = dialog.findViewById(R.id.tv_date_detail_order_admin);
                        TextView tvCount = dialog.findViewById(R.id.tv_count_detail_order_admin);
                        tvAnimal.setText("Animal:");
                        DecimalFormat decimalFormat = new DecimalFormat("###,###");
                        tvName.setText("Customer: "+infoCustomer.getUsername());
                        tvEmail.setText("Email: "+infoCustomer.getEmail());
                        tvDate.setText(infoCustomer.getDate());
                        tvTotalPrice.setText("$"+decimalFormat.format(Integer.parseInt(infoCustomer.getTotalPrice())));
                        tvPhone.setText("Phone: "+infoCustomer.getPhone());
                        int count = 0;
                        for (int i = 0; i < infoCustomer.getCarts().size(); i++) {
                            tvAnimal.append(" "+infoCustomer.getCarts().get(i).getName());
                            count+= infoCustomer.getCarts().get(i).getCount();
                        }
                        tvCount.setText("Count:"+count);
                        dialog.show();
                    }
                });
                rcvOrder.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        break;
                    case R.id.nav_dog:
                        Intent intentDog = new Intent(getApplicationContext(),AnimalEditActivity.class);
                        Pair[] pairsDog = new Pair[1];
                        pairsDog[0] = new Pair<View,String>(findViewById(R.id.nav_dog),"transition_product");
                        ActivityOptions optionsDog = ActivityOptions.makeSceneTransitionAnimation(AdminActivity.this,pairsDog);
                        intentDog.putExtra("name","Dog");
                        startActivity(intentDog,optionsDog.toBundle());
                        break;
                    case R.id.nav_cat:
                        Intent intentCat = new Intent(getApplicationContext(),AnimalEditActivity.class);
                        Pair[] pairsCat = new Pair[1];
                        pairsCat[0] = new Pair<View,String>(findViewById(R.id.nav_cat),"transition_product");
                        ActivityOptions optionsCat = ActivityOptions.makeSceneTransitionAnimation(AdminActivity.this,pairsCat);
                        intentCat.putExtra("name","Cat");
                        startActivity(intentCat,optionsCat.toBundle());
                        break;
                    case R.id.nav_category:
                        Intent intentCategory = new Intent(getApplicationContext(),CategoryActivity.class);
                        Pair[] pairsCategory = new Pair[1];
                        pairsCategory[0] = new Pair<View,String>(findViewById(R.id.nav_category),"transition_product");
                        ActivityOptions optionsCategory = ActivityOptions.makeSceneTransitionAnimation(AdminActivity.this,pairsCategory);
                        intentCategory.putExtra("name","Category");
                        startActivity(intentCategory,optionsCategory.toBundle());
                        break;
                    case R.id.nav_product:
                        Intent intentProduct = new Intent(getApplicationContext(),AnimalEditActivity.class);
                        Pair[] pairsProduct = new Pair[1];
                        pairsProduct[0] = new Pair<View,String>(findViewById(R.id.nav_product),"transition_product");
                        ActivityOptions optionsProduct = ActivityOptions.makeSceneTransitionAnimation(AdminActivity.this,pairsProduct);
                        intentProduct.putExtra("name","Product");
                        startActivity(intentProduct,optionsProduct.toBundle());
                        break;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AdminActivity.this, MainActivity.class));
                        finishAffinity();
                        break;
                    case R.id.nav_profile:
                        Intent intentProfile = new Intent(getApplicationContext(),DetailProfileActivity.class);
                        Pair[] pairsProfile = new Pair[1];
                        pairsProfile[0] = new Pair<View,String>(findViewById(R.id.nav_profile),"transition_profile");
                        ActivityOptions optionsProfile = ActivityOptions.makeSceneTransitionAnimation(AdminActivity.this,pairsProfile);
                        intentProfile.putExtra("name","Account");
                        startActivity(intentProfile,optionsProfile.toBundle());
                        break;
                    case R.id.nav_agenda:
                        Intent intentScheduling = new Intent(getApplicationContext(), SchedulingActivity.class);
                        Pair[] pairsScheduling = new Pair[1];
                        pairsScheduling[0] = new Pair<View,String>(findViewById(R.id.nav_agenda),"transition_scheduling");
                        ActivityOptions optionsScheduling = ActivityOptions.makeSceneTransitionAnimation(AdminActivity.this,pairsScheduling);
                        intentScheduling.putExtra("name","Scheduling");
                        startActivity(intentScheduling,optionsScheduling.toBundle());
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_home);
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.purple_200));
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }
    private void initView() {
        toolbar  = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        contentView = findViewById(R.id.content);
        rcvOrder = findViewById(R.id.rcv_order);
        rcvOrder.setHasFixedSize(true);
        rcvOrder.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<>();
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewitemtouchhelperOrder(0,ItemTouchHelper.LEFT,AdminActivity.this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvOrder);
        rootView = findViewById(R.id.layout_rcvOrder_admin);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orderAdapter != null){
            orderAdapter.release();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof OrderAdapter.OrderViewHolder){
            String strUsername = arrayList.get(viewHolder.getLayoutPosition()).getUsername();
            InfoCustomer infoCustomerDelete = arrayList.get(viewHolder.getLayoutPosition());
            int index = viewHolder.getLayoutPosition();
            orderAdapter.removeItem(index,strUsername);
            Snackbar snackbar = Snackbar.make(rootView,strUsername+"removed",Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderAdapter.undoItem(infoCustomerDelete,index,strUsername);
                    if (index == 0 || index == arrayList.size() -1){
                        rcvOrder.scrollToPosition(index);
                    }
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.sile_out_right,R.anim.sile_in_right);
    }
}