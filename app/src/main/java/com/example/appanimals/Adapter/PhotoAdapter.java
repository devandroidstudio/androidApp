package com.example.appanimals.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appanimals.Activity.ImageActivity;
import com.example.appanimals.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private ArrayList<String> arrayList;
    private Context context;

    public PhotoAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }



    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo,parent,false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String photo = arrayList.get(position);
        if (photo == null){
            return;
        }
        Picasso.with(context).load(photo).resize(1280,853).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_background).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageActivity.class);
                intent.putExtra("InfoImage",photo);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageView;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_viewpager2_detail);
        }
    }
}
