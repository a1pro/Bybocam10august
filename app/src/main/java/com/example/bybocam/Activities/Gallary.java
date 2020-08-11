package com.example.bybocam.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bybocam.Fragments.OneFragment;
import com.example.bybocam.Fragments.ThreeFragment;
import com.example.bybocam.Fragments.TwoFragment;
import com.example.bybocam.R;
import com.google.android.material.tabs.TabLayout;

public class Gallary extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    //ViewPager viewPager;
    public static TextView title;
    public static String tittle;
    public static TabLayout tabLayout;
    public static TextView gallaryCancleImage;
    public static int i;
    public static TextView nextTxt;
    public static ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = findViewById(R.id.textTitle);
        nextTxt = findViewById(R.id.nextTxt);
        progress = findViewById(R.id.progress);
        // viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        gallaryCancleImage = findViewById(R.id.gallaryCancleImage);
        tabLayout.addTab(tabLayout.newTab().setText("Gallery"));
        tabLayout.addTab(tabLayout.newTab().setText("Photo"));
        tabLayout.addTab(tabLayout.newTab().setText("Video"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                new TwoFragment()).commit();

        title.setText("Photo");
        tittle = title.getText().toString().trim();
        gallaryCancleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tabLayout.getTabAt(1).select();
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        i = tab.getPosition();
        switch (i) {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                        new OneFragment()).commit();
                title.setText("Gallery");
                tittle = title.getText().toString().trim();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                        new TwoFragment()).commit();

                title.setText("Photo");
                tittle = title.getText().toString().trim();
                break;
            case 2:

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                        new ThreeFragment()).commit();
                title.setText("Video");
                tittle = title.getText().toString().trim();
//
//                Intent intent=new Intent(Gallary.this,VideoCameraActivity.class);
//                startActivity(intent);
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                        new OneFragment()).commit();

                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

