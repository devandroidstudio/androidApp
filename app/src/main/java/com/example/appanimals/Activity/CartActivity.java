package com.example.appanimals.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.appanimals.Adapter.CartAdapter;
import com.example.appanimals.Model.Category;
import com.example.appanimals.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.DecimalFormat;
import java.util.EventListener;

public class CartActivity extends AppCompatActivity {
    private CartAdapter cartAdapter;
    private Button btnCheckOut;
    private ListView listViewCart;
    private MaterialToolbar toolbarCart;
    private static TextView tvTotalPrice,tvTax,tvCount,tvCountNumber,tvMessage;
    private static long tax = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
        ActionBar();
        EnventUtil();
        ActionView();
        CatchOnItemListview();
        CheckData();
    }

    private void ActionView() {
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,InformationActivity.class));
            }
        });
    }

    private void initView() {
        toolbarCart = findViewById(R.id.toolbar_cart);
        listViewCart = findViewById(R.id.listView_cart);
        cartAdapter = new CartAdapter(CartActivity.this,MainActivity.cartArrayList);
        listViewCart.setAdapter(cartAdapter);
        tvTotalPrice = findViewById(R.id.tv_totalprice);
        tvTax = findViewById(R.id.tv_tax);
        tvMessage = findViewById(R.id.tv_empty_cart);
        tvTax.setText("$"+tax);
        tvCount = findViewById(R.id.tv_count_cart);
        tvCount.setText(MainActivity.cartArrayList.size()+"item");
        tvCountNumber = findViewById(R.id.tv_count_cart_number);
        btnCheckOut = findViewById(R.id.btnCheck_Out);
//        long price =0;
//        for (int i = 0; i < MainActivity.cartArrayList.size(); i++) {
//            price += MainActivity.cartArrayList.get(i).getPrice();
//            tvCountNumber.setText("$" + price);
//
//        }
    }
    private void ActionBar(){
        setSupportActionBar(toolbarCart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarCart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public static void EnventUtil(){
        long totalPrice = 0;
        for (int i = 0; i < MainActivity.cartArrayList.size(); i++) {
            totalPrice += MainActivity.cartArrayList.get(i).getPrice();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        tvTotalPrice.setText("$"+decimalFormat.format(totalPrice));

    }
    private void CatchOnItemListview() {
        listViewCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder b =new AlertDialog.Builder(CartActivity.this);
                b.setTitle("Delete product");
                b.setMessage("Do you want to delete this product");
                b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (MainActivity.cartArrayList.size() <= 0)
                        {
                            tvTax.setText("$0");
                            tvCountNumber.setText("$0");
                            tvMessage.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            MainActivity.cartArrayList.remove(position);
                            cartAdapter.notifyDataSetChanged();
                            EnventUtil();
                            if (MainActivity.cartArrayList.size() <=0)
                            {
                                tvTax.setText("$0");
                                tvCountNumber.setText("$0");
                                tvMessage.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                                tvTax.setText("$"+decimalFormat.format(tax));
                                cartAdapter.notifyDataSetChanged();
                                EnventUtil();
                            }
                        }
                    }
                });
                b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cartAdapter.notifyDataSetChanged();
                        EnventUtil();
                    }
                });
                b.show();
                return true;
            }
        });

    }
    private void CheckData() {
        if (MainActivity.cartArrayList.size() <=0)
        {
            cartAdapter.notifyDataSetChanged();
            tvMessage.setVisibility(View.VISIBLE);
            listViewCart.setVisibility(View.INVISIBLE);
        }
        else
        {
            cartAdapter.notifyDataSetChanged();
            tvMessage.setVisibility(View.INVISIBLE);
            listViewCart.setVisibility(View.VISIBLE);
        }
    }

}