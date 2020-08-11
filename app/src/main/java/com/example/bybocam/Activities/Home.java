package com.example.bybocam.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bybocam.Fragments.AllMessagesFragment;
import com.example.bybocam.Fragments.ProfileFragment;
import com.example.bybocam.Fragments.RecommendationFragment;
import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {


    // private NavController navController;

    private BottomNavigationView navigation;
    private int REQUEST_CAMERA_PERMISSION_RESULT = 1;


    final Fragment fragment1 = new AllMessagesFragment();
    final Fragment fragment2 = new RecommendationFragment();
    final Fragment fragment3 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    int i = 0;
    private NavController navController;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        intialUi();

        fm.beginTransaction().add(R.id.nav_host_fragment2, fragment3, "3").hide(fragment3).addToBackStack("fragment3").commit();
        fm.beginTransaction().add(R.id.nav_host_fragment2, fragment2, "2").hide(fragment2).addToBackStack("fragment2").commit();
        fm.beginTransaction().add(R.id.nav_host_fragment2, fragment1, "1").addToBackStack("fragment1").commit();

        if (Common.isNetworkConnected(Home.this)) {

        } else {
            Toast.makeText(Home.this, "Check your network", Toast.LENGTH_SHORT).show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION_RESULT);
            }
        }
        // loadFragment(new AllMessagesFragment());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        }, intentFilter);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Home.this, "Permissions are  required to run app correctly", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    @Override
    public void onBackPressed() {
        int seletedItemId = navigation.getSelectedItemId();
        if (R.id.home != seletedItemId) {
            setHomeItem(Home.this);
        } else {
            super.onBackPressed();
            finish();
        }

    }


    public void setHomeItem(Activity activity) {
        if (i == 0) {
            BottomNavigationView bottomNavigationView = activity.findViewById(R.id.navigation);
            bottomNavigationView.setSelectedItemId(R.id.naviagtion_home);
            i = 1;
        } else {
            finish();
        }
    }

    //Initialize UI
    private void intialUi() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment2);
        navigation = findViewById(R.id.navigation);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.naviagtion_home:

                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;

                case R.id.naviagtion_uplaod:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
                case R.id.naviagtion_cart:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
            }
            return false;
        }
    };

}
