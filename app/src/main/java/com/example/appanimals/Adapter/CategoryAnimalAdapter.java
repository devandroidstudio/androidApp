package com.example.appanimals.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appanimals.Activity.DetailActivity;
import com.example.appanimals.Model.Category;
import com.example.appanimals.Model.Product;
import com.example.appanimals.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAnimalAdapter extends RecyclerView.Adapter<CategoryAnimalAdapter.AnimalViewHolder> implements Filterable {
    private ArrayList<Category> arrayList;
    private Context context;
    private ArrayList<Category> mArrayList;

    public CategoryAnimalAdapter(ArrayList<Category> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.mArrayList = arrayList;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_animal,parent,false);
        return new AnimalViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        Category category = arrayList.get(position);
        if (category == null){
            return;
        }
        holder.tvName.setText(category.getName());
        Picasso.with(context).load(category.getImage()).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.error).into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        if (arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()){
                    arrayList = mArrayList;
                }
                else {
                    ArrayList<Category> list = new ArrayList<>();
                    for (Category category : mArrayList){
                        if (category.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(category);
                        }
                    }
                    arrayList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<Category>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CircleImageView imageView;
        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_category_animal);
            imageView = itemView.findViewById(R.id.image_category_animal_admin);
        }
    }
    public void release(){
        context = null;
    }
}
