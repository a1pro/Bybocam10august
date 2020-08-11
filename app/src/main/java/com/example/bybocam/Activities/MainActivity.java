package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.bybocam.R;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    int i = 0;
    private Animation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.animation1);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (i == 0) {
            navController.navigate(R.id.splashFragment);
            i++;
        } else {
            finish();
        }
    }
}
