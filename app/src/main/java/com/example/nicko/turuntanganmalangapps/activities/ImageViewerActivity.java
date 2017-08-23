package com.example.nicko.turuntanganmalangapps.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.nicko.turuntanganmalangapps.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class ImageViewerActivity extends AppCompatActivity {

    private PhotoView img_photo_viewer;
    private String the_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ttm_white_action_bar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c3e50")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#2c3e50"));
        }

        the_image = getIntent().getExtras().getString("the_image");
        img_photo_viewer = (PhotoView) findViewById(R.id.img_photo_viewer);
        Picasso.with(ImageViewerActivity.this).load(the_image).placeholder(R.drawable.ttm_logo).error(R.drawable.ttm_logo).into(img_photo_viewer);
    }
}
