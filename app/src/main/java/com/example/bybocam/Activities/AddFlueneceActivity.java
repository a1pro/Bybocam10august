package com.example.bybocam.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.ResponseData;
import com.example.bybocam.Model.ViewProfileModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.LocationTrack;
import com.example.bybocam.Utils.NothingSelectedSpinnerAdapter;
import com.example.bybocam.Utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddFlueneceActivity extends AppCompatActivity {
    private SeekBar seekbar;
    private TextView tv_mid_value;
    private Retrofit retrofit;
    private ProgressBar progress;
    SessionManager session;
    private String UserID,username,lat,longi,image;
    private String Price="0";
    private CircleImageView edit_profile_image;
    private TextView edit_user_name;
    private Spinner spiner_race,spiner_industry,spiner_gender;
    private String race_,industry_,gender_;
    private Button bt_save;
    private EditText et_desc;
    String race[] = {"Black/African American", "Asian", "white", "American Indian/Alaska Native", "Hispanic/Latino", "Native Hawaiian or Other Pacific Islander"};
    String Gender[] = {"Male", "Female", "Neutral or Prefer not to say"};
    String industry[]={"Advertising", "Creative industry", "Entertainment industry","Fashion","Media","Retail","Technology Industry","Education","Finance","Music industry","Service Industry"};
    File file;
    Geocoder geocoder;
    List<Address> addresses;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    double latitude, longtitude;
    private EditText et_location;
    private ImageView imageBackReport;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fluenece);
        init();
        getLocation();
        setSpinners();
        session = new SessionManager(AddFlueneceActivity.this.getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        //  email = user.get(SessionManager.KEY_EMAIL);
        UserID = user.get(SessionManager.USER_ID);
        username=user.get(SessionManager.USER_NAME);
        lat= String.valueOf(latitude);
        longi= String.valueOf(longtitude);
        image=user.get(SessionManager.USER_IMAGE);

        if (Common.isNetworkConnected(AddFlueneceActivity.this)) {
            viewDataHitApi();
        } else {
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(AddFlueneceActivity.this, "Check your network", Toast.LENGTH_SHORT).show();
        }

        imageBackReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isNetworkConnected(AddFlueneceActivity.this)){
                    Addinfluence();
                }else {
                    Toast.makeText(AddFlueneceActivity.this, "check Internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void setSpinners() {
        spiner_race.setPrompt("Select Race");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, race);
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
        imageBackReport=findViewById(R.id.imageBackReport);
        et_location=findViewById(R.id.et_location);
        et_desc=findViewById(R.id.et_desc);
        bt_save=findViewById(R.id.bt_save);
        spiner_gender=findViewById(R.id.spiner_gender);
        spiner_industry=findViewById(R.id.spiner_industry);
        spiner_race=findViewById(R.id.spiner_race);
        edit_user_name = findViewById(R.id.edit_user_name);
        edit_profile_image = findViewById(R.id.edit_profile_image);
        progress = findViewById(R.id.progress);
        tv_mid_value = findViewById(R.id.tv_mid_value);
        seekbar = findViewById(R.id.seekbar);
        seekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.yellow_color_picker), PorterDuff.Mode.SRC_IN);
        seekbar.getThumb().setColorFilter(getResources().getColor(R.color.yellow_color_picker), PorterDuff.Mode.SRC_IN);
        seekbar.setMax(10000);
    }


    private void viewDataHitApi() {
        progress.setVisibility(View.VISIBLE);
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<ViewProfileModel> call = apiListener.viewProfileAPI(UserID);
        call.enqueue(new Callback<ViewProfileModel>() {
            @Override
            public void onResponse(Call<ViewProfileModel> call, Response<ViewProfileModel> response) {
                if (response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    ViewProfileModel data = response.body();
                    if (data != null) {
                        if (data.getCode().equalsIgnoreCase("201")) {

                            if (data.getData().get(0).getUserName() != null) {
                                edit_user_name.setText(data.getData().get(0).getUserName());
                            }

                            if (data.getData().get(0).getUserImage() != null) {
                                String imageUri = Common.image_base_url + data.getData().get(0).getUserImage();
                                Uri uri = Uri.parse(imageUri);
                                Glide.with(getApplicationContext()).load(uri).into(edit_profile_image);
                            }

                            //         progress.setVisibility(View.INVISIBLE);
                        } else {
                            progress.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ViewProfileModel> call, Throwable t) {
                Toast.makeText(AddFlueneceActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
            }
        });
    }


    private void Addinfluence() {
        try {
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), UserID);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), username);
            RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), lat);
            RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), longi);
            RequestBody address = RequestBody.create(MediaType.parse("text/plain"), et_location.getText().toString());
            RequestBody race = RequestBody.create(MediaType.parse("text/plain"), race_);
            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), gender_);
            RequestBody industry = RequestBody.create(MediaType.parse("text/plain"), industry_);
            RequestBody price = RequestBody.create(MediaType.parse("text/plain"), Price);
            RequestBody discription = RequestBody.create(MediaType.parse("text/plain"), et_desc.getText().toString());

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            MultipartBody.Part picture = MultipartBody.Part.createFormData("picture", "", requestFile);


            progress.setVisibility(View.VISIBLE);
//            retrofit = RetrofitClientIntance.retroInit();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl("https://a1professionals.net/bybocam/api/")
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            final ApiListener apiListener = retrofit2.create(ApiListener.class);
            Call<ResponseData> call = apiListener.AddInfluence(userId, name, latitude, longitude, address
                    , race, gender, industry, price, discription,picture);
            call.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                Toast.makeText(AddFlueneceActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                                //         progress.setVisibility(View.INVISIBLE);
                            } else {
                                progress.setVisibility(View.GONE);
                            }
                        }
                    }else {
                        Toast.makeText(AddFlueneceActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    Toast.makeText(AddFlueneceActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.e("exception",e.getMessage());
        }
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

        locationTrack = new LocationTrack(AddFlueneceActivity.this);
        if (locationTrack.canGetLocation()) {


            longtitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();


            // Toast.makeText(getActivity().getApplicationContext(), "Longitude:" + Double.toString(longtitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();

            geocoder = new Geocoder(AddFlueneceActivity.this, Locale.getDefault());

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
                return (ActivityCompat.checkSelfPermission(AddFlueneceActivity.this, permission) == PackageManager.PERMISSION_GRANTED);
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
        new AlertDialog.Builder(AddFlueneceActivity.this)
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