package com.example.bybocam.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.R;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.LocationTrack;
import com.example.bybocam.Utils.NothingSelectedSpinnerAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class InfluenceFilterActivity extends AppCompatActivity {
    private SeekBar seekbar;
    private Spinner spiner_race,spiner_industry,spiner_gender;
    private String race_,industry_,gender_;
    private TextView tv_mid_value;
    private ProgressBar progress;
    private Button bt_save;
    private FusedLocationProviderClient client;
    private EditText et_desc,et_location;
    private String Price="0";
    Geocoder geocoder;
    List<Address> addresses;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    double latitude, longtitude;
    String race[] = {"Black/African American", "Asian", "white", "American Indian/Alaska Native", "Hispanic/Latino", "Native Hawaiian or Other Pacific Islander"};
    String Gender[] = {"Male", "Female", "Neutral or Prefer not to say"};
    String industry[]={"Advertising", "Creative industry", "Entertainment industry","Fashion","Media","Retail","Technology Industry","Education","Finance","Music industry","Service Industry"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influence_filter);
        init();
        setSpinners();
        if (Common.isNetworkConnected(InfluenceFilterActivity.this)){
            getLocation();
        }else {
            Toast.makeText(this, "check Your Internet", Toast.LENGTH_SHORT).show();
        }

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lat= String.valueOf(latitude);
                String longi=String.valueOf(longtitude);
                Intent intent=new Intent();
                intent.putExtra("race",race_);
                intent.putExtra("gender",gender_);
                intent.putExtra("industry",industry_);
                intent.putExtra("price",Price);
                intent.putExtra("latitude",lat);
                intent.putExtra("logitude",longi);
                setResult(1,intent);
                finish();

            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                tv_mid_value.setText("" + i);
                Price=""+i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void setSpinners() {
        spiner_race.setPrompt("Select Race");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, race);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_race.setAdapter(dataAdapter);
        spiner_race.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        dataAdapter,
                        R.layout.contact_spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));

        spiner_race.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                race_=""+spiner_race.getSelectedItem();
                //        Toast.makeText(AddFlueneceActivity.this, ""+spiner_race.getSelectedItem(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spiner_gender.setPrompt("Select Gender");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Gender);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_gender.setAdapter(dataAdapter2);
        spiner_gender.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        dataAdapter2,
                        R.layout.contact_spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
        spiner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender_=""+spiner_gender.getSelectedItem();
                //         Toast.makeText(AddFlueneceActivity.this, ""+spiner_gender.getSelectedItem(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spiner_industry.setPrompt("Select Industry");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, industry);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_industry.setAdapter(dataAdapter3);
        spiner_industry.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        dataAdapter3,
                        R.layout.contact_spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
        spiner_industry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                industry_=""+spiner_industry.getSelectedItem();
                //          Toast.makeText(AddFlueneceActivity.this, ""+spiner_industry.getSelectedItem(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void init() {
        et_location=findViewById(R.id.et_location);
        et_desc=findViewById(R.id.et_desc);
        bt_save=findViewById(R.id.bt_save);
        spiner_gender=findViewById(R.id.spiner_gender);
        spiner_industry=findViewById(R.id.spiner_industry);
        spiner_race=findViewById(R.id.spiner_race);
        progress = findViewById(R.id.progress);
        tv_mid_value = findViewById(R.id.tv_mid_value);
        seekbar = findViewById(R.id.seekbar);
        seekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.yellow_color_picker), PorterDuff.Mode.SRC_IN);
        seekbar.getThumb().setColorFilter(getResources().getColor(R.color.yellow_color_picker), PorterDuff.Mode.SRC_IN);
        seekbar.setMax(10000);
    }


    public void getLocation() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        locationTrack = new LocationTrack(InfluenceFilterActivity.this);
        if (locationTrack.canGetLocation()) {


            longtitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();


            // Toast.makeText(getActivity().getApplicationContext(), "Longitude:" + Double.toString(longtitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();

            geocoder = new Geocoder(InfluenceFilterActivity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longtitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses!=null){
                if (addresses.size()!=0) {
                    String address = addresses.get(0).getAddressLine(0);
                    et_location.setText(address);
                }
            }
        } else {

            locationTrack.showSettingsAlert();
        }
    }


    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (ActivityCompat.checkSelfPermission(InfluenceFilterActivity.this, permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel(
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }

                break;
        }

    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(InfluenceFilterActivity.this)
                .setMessage("These permissions are mandatory for the application. Please allow access.")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }



}