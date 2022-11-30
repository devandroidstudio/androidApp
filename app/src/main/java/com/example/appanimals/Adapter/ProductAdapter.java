package com.example.appanimals.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appanimals.Activity.DetailActivity;
import com.example.appanimals.Model.Product;
import com.example.appanimals.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private ArrayList<Product> arrayList;
    private Context context;
    private final Integer[] arrayNumber1 = {640,853};
    private final Integer[] arrayNumber2 = {650,400};

    public ProductAdapter(ArrayList<Product> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = arrayList.get(position);
        if (product == null){
            return;
        }

        for (int i = 0; i < product.getImg().size(); i++) {
            Picasso.with(context).load(product.getImg().get(0)).resize(arrayNumber1[new Random().nextInt(arrayNumber1.length)], arrayNumber2[new Random().nextInt(arrayNumber2.length)]).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.error).into(holder.img);
        }
        holder.tvName.setText(product.getName());
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

    public void release(){
        context = null;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName, tvSex, tvAge,tvOrigin;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img1);
            tvName = itemView.findViewById(R.id.t1);
            tvSex = itemView.findViewById(R.id.tv_sex_main);
            tvAge = itemView.findViewById(R.id.tv_age_main);
            tvOrigin = itemView.findViewById(R.id.tv_origin_main);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("informationAnimal",arrayList.get(getAdapterPosition()));
                    Pair[] pairs = new Pair[5];
                    pairs[0] = new Pair<View,String>(img,"image_main_trans");
                    pairs[1] = new Pair<View,String>(tvName,"name_main_trans");
                    pairs[2] = new Pair<View,String>(tvSex,"sex_main_trans");
                    pairs[3] = new Pair<View,String>(tvAge,"age_main_trans");
                    pairs[4] = new Pair<View,String>(tvOrigin,"origin_main_trans");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,pairs);
                    context.startActivity(intent,options.toBundle());
                }
            });
        }
    }
}
