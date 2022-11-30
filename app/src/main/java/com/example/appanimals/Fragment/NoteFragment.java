package com.example.appanimals.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appanimals.Activity.MainActivity;
import com.example.appanimals.Adapter.FavoriteAdapter;
import com.example.appanimals.Adapter.ProductAdapter;
import com.example.appanimals.Model.Category;
import com.example.appanimals.Model.Product;
import com.example.appanimals.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NoteFragment extends Fragment {



    private View view;
    private RecyclerView rcvFavorite;
    private ArrayList<Product> productArrayList;
    private FavoriteAdapter favoriteAdapter;
    private DatabaseReference reference;
    private String TAG = "Hello";
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        Log.d(TAG, "onResume: ");
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        Log.d(TAG, "onAttach: ");
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        productArrayList.clear();
//        GetDataFavorite();
//        Log.d(TAG, "onStop: ");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        Log.d(TAG, "onPause: ");
//    }
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        productArrayList.clear();
//        GetDataFavorite();
//        Log.d(TAG, "onStart: ");
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate: ");
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_note, container, false);
        initView();
        GetDataFavorite();
        return view;
    }

    private void GetDataFavorite() {
        reference = FirebaseDatabase.getInstance().getReference("Favorite").child(MainActivity.account.getFullName());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (productArrayList != null){
                    productArrayList.clear();
                }
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Product product = postSnapshot.getValue(Product.class);
                    productArrayList.add(product);
                }
                favoriteAdapter = new FavoriteAdapter(getContext(),productArrayList);
                rcvFavorite.setAdapter(favoriteAdapter);
                favoriteAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void initView() {
        rcvFavorite = view.findViewById(R.id.rcv_favorite);
        productArrayList = new ArrayList<>();
        rcvFavorite.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (favoriteAdapter != null){
            favoriteAdapter.release();
        }
    }
}