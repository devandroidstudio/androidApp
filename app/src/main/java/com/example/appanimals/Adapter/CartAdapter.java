package com.example.appanimals.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appanimals.Activity.CartActivity;
import com.example.appanimals.Activity.MainActivity;
import com.example.appanimals.Model.Cart;
import com.example.appanimals.Model.Product;
import com.example.appanimals.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Cart> cartArrayList;

    public CartAdapter(Context context, ArrayList<Cart> cartArrayList) {
        this.context = context;
        this.cartArrayList = cartArrayList;
    }

    @Override
    public int getCount() {
        if (cartArrayList != null)
        {
            return cartArrayList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return cartArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_cart,null);
            viewHolder.tvName = convertView.findViewById(R.id.tv_name_cart);
            viewHolder.tvPrice = convertView.findViewById(R.id.tv_price_cart);
            viewHolder.imgCart = convertView.findViewById(R.id.img_cart);
            viewHolder.fabIncrease = convertView.findViewById(R.id.fab_increase);
            viewHolder.tvCount = convertView.findViewById(R.id.tv_item_count_cart);
            viewHolder.fabDecrease = convertView.findViewById(R.id.fab_decrease);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Cart cart = cartArrayList.get(position);
        viewHolder.tvName.setText(cart.getName());
        viewHolder.tvName.setMaxLines(1);
        viewHolder.tvName.setEllipsize(TextUtils.TruncateAt.END);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        viewHolder.tvPrice.setText("Price: $"+decimalFormat.format(cart.getPrice()));
        for (int i = 0; i < cart.getImg().size(); i++) {
            Picasso.with(context).load(cart.getImg().get(0)).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.error).into(viewHolder.imgCart);
        }
        viewHolder.tvCount.setText(cart.getCount()+"");
        int count = Integer.parseInt(viewHolder.tvCount.getText().toString());
        if (count >= 10)
        {
            viewHolder.fabIncrease.setVisibility(View.INVISIBLE);
            viewHolder.fabDecrease.setVisibility(View.VISIBLE);

        }
        else if (count <= 1)
        {
            viewHolder.fabDecrease.setVisibility(View.INVISIBLE);
        }
        else
        {
            viewHolder.fabIncrease.setVisibility(View.VISIBLE);
            viewHolder.fabDecrease.setVisibility(View.VISIBLE);
        }
        viewHolder.fabIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newCount = Integer.parseInt(viewHolder.tvCount.getText().toString()) + 1;
                int currentCount = MainActivity.cartArrayList.get(position).getCount();
                long currentPrice = MainActivity.cartArrayList.get(position).getPrice();

                MainActivity.cartArrayList.get(position).setCount(newCount);
                long newPrice = (currentPrice * newCount)/currentCount;
                MainActivity.cartArrayList.get(position).setPrice(newPrice);
                DecimalFormat decimalFormat1 = new DecimalFormat("###,###");
                viewHolder.tvPrice.setText("$"+ decimalFormat1.format(newPrice));
                CartActivity.EnventUtil();
                if (newCount >9)
                {
                    viewHolder.fabIncrease.setVisibility(View.INVISIBLE);
                    viewHolder.fabDecrease.setVisibility(View.VISIBLE);
                    viewHolder.tvCount.setText(String.valueOf(newCount));
                }
                else
                {
                    viewHolder.tvCount.setText(String.valueOf(newCount));
                    viewHolder.fabDecrease.setVisibility(View.VISIBLE);
                    viewHolder.fabIncrease.setVisibility(View.VISIBLE);
                }
            }
        });
        viewHolder.fabDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newCount = Integer.parseInt(viewHolder.tvCount.getText().toString()) - 1;
                int currentCount = MainActivity.cartArrayList.get(position).getCount();
                long currentPrice = MainActivity.cartArrayList.get(position).getPrice();

                MainActivity.cartArrayList.get(position).setCount(newCount);
                long newPrice = (currentPrice * newCount)/currentCount;
                MainActivity.cartArrayList.get(position).setPrice(newPrice);
                DecimalFormat decimalFormat1 = new DecimalFormat("###,###");
                viewHolder.tvPrice.setText("$"+ decimalFormat1.format(newPrice));
                CartActivity.EnventUtil();
                if (newCount <2)
                {
                    viewHolder.fabDecrease.setVisibility(View.INVISIBLE);
                    viewHolder.fabIncrease.setVisibility(View.VISIBLE);
                    viewHolder.tvCount.setText(String.valueOf(newCount));
                }
                else
                {
                    viewHolder.tvCount.setText(String.valueOf(newCount));
                    viewHolder.fabDecrease.setVisibility(View.VISIBLE);
                    viewHolder.fabIncrease.setVisibility(View.VISIBLE);
                }
            }
        });
        return convertView;
    }
    private class ViewHolder{
        TextView tvName,tvPrice, tvCount;
        CircleImageView imgCart;
        FloatingActionButton fabIncrease,fabDecrease;

    }
}
