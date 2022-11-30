package com.example.appanimals.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.appanimals.Model.Photo;
import com.example.appanimals.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {
    private PhotoView imgDetail;
    private MaterialToolbar toolbarImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        GetData();
        ActionToolBar();
    }

    private void GetData() {
        Intent intent = getIntent();
        String image = intent.getStringExtra("InfoImage");
        Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_background).into(imgDetail);
    }

    private void initView() {
        imgDetail = findViewById(R.id.photo_view);
        toolbarImage = findViewById(R.id.toolbar_image_detail);
        toolbarImage.setTitle("");
    }
    private void ActionToolBar(){
        setSupportActionBar(toolbarImage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarImage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}