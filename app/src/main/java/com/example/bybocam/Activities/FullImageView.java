package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;
import com.jsibbold.zoomage.ZoomageView;

public class FullImageView extends AppCompatActivity {

    ZoomageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_view);
        imageView = findViewById(R.id.myZoomageView);

        Intent callingActivityIntent = this.getIntent();
        if (callingActivityIntent != null) {
            String name = callingActivityIntent.getStringExtra("data");
            String imageName = callingActivityIntent.getStringExtra("imageName");
            String likedimage=callingActivityIntent.getStringExtra("likeimage");
            if (name != null) {
                Glide.with(getApplicationContext()).load(Uri.parse(Common.image_post_url + name)).fitCenter().into(imageView);
            } else if (imageName != null) {
                Glide.with(getApplicationContext()).load(Uri.parse(Common.message_Image_base_url + imageName)).fitCenter().into(imageView);

            }else if (likedimage!=null){
                Glide.with(getApplicationContext()).load(Uri.parse(Common.image_post_url+ likedimage)).fitCenter().into(imageView);

            }
        } else {
            Toast.makeText(this, "calling Activity Intent is null", Toast.LENGTH_SHORT).show();
        }

    }

}
