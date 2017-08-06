package com.kutugondrong.halodocgallery;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class ViewImage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        String f = getIntent().getStringExtra("img");
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageURI(Uri.parse(f));
    }
}
