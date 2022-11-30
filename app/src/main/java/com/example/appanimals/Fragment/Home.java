package com.example.appanimals.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.appanimals.Activity.MainActivity;
import com.example.appanimals.Activity.MapsActivity;
import com.example.appanimals.Adapter.CategoryAdapter;
import com.example.appanimals.Adapter.ProductAdapter;
import com.example.appanimals.Model.Category;
import com.example.appanimals.Model.ISendDataListener;
import com.example.appanimals.Model.Product;
import com.example.appanimals.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;

public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ViewFlipper viewFlipper;
    private MainActivity mainActivity;
    private RecyclerView rcvProductMain,rcvCategoryMain;
    private ArrayList<Product> productArrayList;
    private ArrayList<Category> categoryArrayList;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private ImageButton imageButton;
    private DatabaseReference reference;
//    public Home() {
//        // Required empty public constructor
//    }
    private ISendDataListener iSendDataListener;
    public static Home getInstance(Category category) {
        Home home = new Home();
        Bundle bundle = new Bundle();
        bundle.putSerializable("infocategory",category);
        home.setArguments(bundle);
        return home;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainActivity = (MainActivity) getActivity();
        rcvCategoryMain = view.findViewById(R.id.rcv_category_main);
        rcvProductMain = view.findViewById(R.id.rcv_product_main);
        rcvProductMain.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rcvCategoryMain.setLayoutManager(linearLayoutManager);
        productArrayList = new ArrayList<>();
        categoryArrayList = new ArrayList<>();
        viewFlipper = view.findViewById(R.id.viewFlipper);
        imageButton = view.findViewById(R.id.btn_location);
        setViewFlipper();
        GetDataCategory();
        ActionView();
        return view;
    }

    private void ActionView() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.startActivity(new Intent(mainActivity, MapsActivity.class));
            }
        });
    }

    private void setViewFlipper() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.drawable.dog1);
        arrayList.add(R.drawable.dog2);
        arrayList.add(R.drawable.dog3);
        arrayList.add(R.drawable.bulldog);
        for (int i = 0; i < arrayList.size(); i++) {
            ImageView image = new ImageView(getContext());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setBackgroundResource(arrayList.get(i));
            viewFlipper.addView(image);
            viewFlipper.setFlipInterval(5000);
            viewFlipper.setAutoStart(true);
            Animation animation_sile_in = AnimationUtils.loadAnimation(getContext(),R.anim.sile_in_right);
            Animation animation_sile_out = AnimationUtils.loadAnimation(getContext(),R.anim.sile_out_right);
            viewFlipper.setInAnimation(animation_sile_in);
            viewFlipper.setOutAnimation(animation_sile_out);
        }
    }

    private void GetDataCategory() {
        reference = FirebaseDatabase.getInstance().getReference("Category");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Category category = postSnapshot.getValue(Category.class);
                    if (category != null)
                    {
                        reference = FirebaseDatabase.getInstance().getReference("Animal").child(category.getName());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                    Product product = postSnapshot.getValue(Product.class);
                                    if (product.getStatus().equals("On"))
                                    {
                                        productArrayList.add(product);
                                    }

                                }
                                productAdapter = new ProductAdapter(productArrayList,getContext());
                                rcvProductMain.setAdapter(productAdapter);
                                productAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    categoryArrayList.add(category);
                }
                categoryAdapter = new CategoryAdapter(categoryArrayList,getContext());
                rcvCategoryMain.setAdapter(categoryAdapter);
                categoryAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (productAdapter != null){
            productAdapter.release();
        }
        if (categoryAdapter != null)
        {
            categoryAdapter.release();
        }
    }

}