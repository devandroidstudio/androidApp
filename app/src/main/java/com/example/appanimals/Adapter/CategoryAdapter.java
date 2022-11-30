package com.example.appanimals.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appanimals.Activity.AnimalActivity;
import com.example.appanimals.Model.Category;
import com.example.appanimals.R;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private ArrayList<Category> arrayList;
    private Context context;
    private String name = "";

    public CategoryAdapter(ArrayList<Category> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_main,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = arrayList.get(position);
        if (category == null)
        {
            return;
        }
        holder.tvNameCategory.setText(category.getName());
        name = category.getName().trim();
        Picasso.with(context).load(category.getImage()).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.error).into(holder.imgCategory);
        Random rnd = new Random();
        int color = Color.argb(100, rnd.nextInt(200), rnd.nextInt(255), rnd.nextInt(255));
        holder.cardView.setCardBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        if (arrayList != null)
        {
            return arrayList.size();
        }
        return 0;
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameCategory;
        ImageView imgCategory;
        MaterialCardView cardView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameCategory = itemView.findViewById(R.id.tv_name_category);
            imgCategory = itemView.findViewById(R.id.img_logo_category);
            cardView = itemView.findViewById(R.id.cardview_category);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AnimalActivity.class);
                    intent.putExtra("Category",arrayList.get(getAdapterPosition()));
                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View,String>(itemView,"category_trans");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,pairs);
                    context.startActivity(intent,options.toBundle());
                }
            });
        }
    }

    public void release(){
        context = null;
    }
}
