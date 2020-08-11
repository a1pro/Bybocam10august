package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfluencePersonActivity extends AppCompatActivity {
    private CircleImageView edit_profile_image;
    private TextView edit_user_name;
    private ImageView imageBackReport;
    private EditText et_race,et_industry,et_price,et_location,et_gender,et_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influence_person);
        edit_profile_image=findViewById(R.id.edit_profile_image);
        edit_user_name=findViewById(R.id.edit_user_name);
        et_race=findViewById(R.id.et_race);
        et_industry=findViewById(R.id.et_industry);
        et_price=findViewById(R.id.et_price);
        imageBackReport=findViewById(R.id.imageBackReport);
        et_location=findViewById(R.id.et_location);
        et_gender=findViewById(R.id.et_gender);
        et_desc=findViewById(R.id.et_desc);

        String name=getIntent().getStringExtra("name");
        String race=getIntent().getStringExtra("race");
        String industry=getIntent().getStringExtra("industry");
        String gender=getIntent().getStringExtra("gender");
        String location=getIntent().getStringExtra("location");
        String desc=getIntent().getStringExtra("desc");
        String iamge=getIntent().getStringExtra("iamge");
        String Price=getIntent().getStringExtra("price");

        if (name!=null){
            edit_user_name.setText(name);
        }

        if (race!=null){
            et_race.setText(race);
        }

        if (industry!=null){
            et_industry.setText(industry);
        }

        if (gender!=null){
            et_gender.setText(gender);
        }

        if (location!=null){
            et_location.setText(location);
        }

        if (Price!=null){
            et_price.setText(Price);
        }

        if (desc!=null){
            et_desc.setText(desc);
        }

        if (iamge!=null){
            Glide.with(InfluencePersonActivity.this).load(Common.Influencer_image_url+iamge).placeholder(R.drawable.bg_msg_from).dontAnimate().into(edit_profile_image);

        }

        imageBackReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}