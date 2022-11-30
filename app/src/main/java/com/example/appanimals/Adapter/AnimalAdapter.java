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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appanimals.Activity.DetailActivity;
import com.example.appanimals.Model.Product;
import com.example.appanimals.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> implements Filterable {
    private ArrayList<Product> arrayList;
    private Context context;
    private ArrayList<Product> mArrayList;

    public AnimalAdapter(ArrayList<Product> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.mArrayList = arrayList;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animal,parent,false);
        return new AnimalViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        Product product = arrayList.get(position);
        if (product == null){
            return;
        }
        holder.tvName.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        holder.tvPrice.setText("$" + decimalFormat.format(product.getPrice()));
        holder.tvDescription.setMaxLines(1);
        holder.tvDescription.setEllipsize(TextUtils.TruncateAt.END);
        holder.tvDescription.setText(product.getDescription());
        for (int i = 0; i < product.getImg().size(); i++) {
            Picasso.with(context).load(product.getImg().get(0)).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.error).into(holder.imgAnimal);
        }
        holder.tvSex.setText(product.getSex());
        holder.tvOrigin.setText(product.getOrigin());
        holder.tvAge.setText(product.getAge());
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
                    ArrayList<Product> list = new ArrayList<>();
                    for (Product product : mArrayList){
                        if (product.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(product);
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
                arrayList = (ArrayList<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvPrice,tvDescription,tvAge,tvOrigin,tvSex;
        CircleImageView imgAnimal;
        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnimal = itemView.findViewById(R.id.img_animal);
            tvName = itemView.findViewById(R.id.tv_name_animal);
            tvPrice = itemView.findViewById(R.id.tv_price_animal);
            tvDescription = itemView.findViewById(R.id.tv_description_animal);
            tvAge = itemView.findViewById(R.id.tv_age_animal);
            tvOrigin = itemView.findViewById(R.id.tv_origin_animal);
            tvSex = itemView.findViewById(R.id.tv_sex_animal);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,DetailActivity.class);
                    intent.putExtra("informationAnimal",arrayList.get(getAdapterPosition()));
                    Pair[] pairs = new Pair[7];
                    pairs[0] = new Pair<View,String>(imgAnimal,"image_main_trans");
                    pairs[1] = new Pair<View,String>(tvName,"name_main_trans");
                    pairs[2] = new Pair<View,String>(tvPrice,"price_main_trans");
                    pairs[3] = new Pair<View,String>(tvDescription,"description_animal_trans");
                    pairs[4] = new Pair<View,String>(tvSex,"sex_main_trans");
                    pairs[5] = new Pair<View,String>(tvAge,"age_main_trans");
                    pairs[6] = new Pair<View,String>(tvOrigin,"origin_main_trans");
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
