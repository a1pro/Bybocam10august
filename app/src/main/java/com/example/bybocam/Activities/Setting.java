package com.example.bybocam.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.LoginModel;
import com.example.bybocam.Model.ViewProfileModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Setting extends AppCompatActivity {

    private LinearLayout reportLayout, notificationLinear,
            delete_account,
            clear_search_history,
            clear_conversion,
            clear_data, share_app, log_out,shoutout,blocked_users,liked_post;
    private ImageView imageBackSetting, edit_photo;
    private final int SELECT_IMAGE = 1;
    private ImageView settingProfileImage;
    TextView dialog_title, dialog_message, setting_user_dateofborth, setting_user_phone, setting_email, setting_username;
    Button dismis, clear;
    Retrofit retrofit;
    String email, userID;
    SessionManager sessionManager;
    ProgressBar progress;
    private Switch switch_id,switch_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initialUi();
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        userID = user.get(SessionManager.USER_ID);
        if (userID != null) {

            if (Common.isNetworkConnected(Setting.this)) {
                viewDataHitApi();
            }
            else {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(Setting.this, "Check your network", Toast.LENGTH_SHORT).show();
            }

        }

        switch_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true){
                    savetouchid("1");
                }else {
                    savetouchid("0");
                }
            }
        });

        switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        liked_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Setting.this,LikedPostsActivity.class);
                startActivity(intent);
            }
        });

        reportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reportIntent = new Intent(Setting.this, ReportProblem.class);
                startActivity(reportIntent);
            }
        });

        shoutout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shout = new Intent(Setting.this, ShoutoutLoadActivity.class);
                startActivity(shout);
            }
        });
        notificationLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reportIntent = new Intent(Setting.this, NotificationScreen.class);
                startActivity(reportIntent);
            }
        });

        blocked_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(Setting.this,BlockedUsersActivity.class);
            startActivity(intent);
            }
        });
        imageBackSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog alertDialog = new Dialog(Setting.this);
                LayoutInflater inflater = Setting.this.getLayoutInflater();
                View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);

                dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
                dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
                dismis = add_menu_layout.findViewById(R.id.discard_btn);
                clear = add_menu_layout.findViewById(R.id.cancle_btn);
                dialog_title.setText(R.string.delete_account);
                dialog_message.setText("Are you sure you want to delete your account");
                clear.setText("Delete");
                dismis.setText("Cancel");
                dismis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                alertDialog.setContentView(add_menu_layout);

                alertDialog.show();


            }
        });
        clear_search_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog alertDialog = new Dialog(Setting.this);
                LayoutInflater inflater = Setting.this.getLayoutInflater();
                final View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);

                dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
                dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
                dismis = add_menu_layout.findViewById(R.id.discard_btn);
                clear = add_menu_layout.findViewById(R.id.cancle_btn);
                dialog_title.setText(R.string.clear_serach_history);
                dialog_message.setText("Are you sure you want to clear your history");
                dismis.setText("Cancel");
                dismis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                alertDialog.setContentView(add_menu_layout);

                alertDialog.show();
            }
        });
        clear_conversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog alertDialog = new Dialog(Setting.this);
                LayoutInflater inflater = Setting.this.getLayoutInflater();
                View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);

                dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
                dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
                dismis = add_menu_layout.findViewById(R.id.discard_btn);
                clear = add_menu_layout.findViewById(R.id.cancle_btn);
                dismis.setText("Cancel");

                dismis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                alertDialog.setContentView(add_menu_layout);

                alertDialog.show();
            }
        });
        clear_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog alertDialog = new Dialog(Setting.this);
                LayoutInflater inflater = Setting.this.getLayoutInflater();
                View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);

                dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
                dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
                dismis = add_menu_layout.findViewById(R.id.discard_btn);
                clear = add_menu_layout.findViewById(R.id.cancle_btn);
                dialog_title.setText(R.string.clear_data);
                dialog_message.setText("Are you sure you want to clear data");
                dismis.setText("Cancel");
                dismis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                alertDialog.setContentView(add_menu_layout);

                alertDialog.show();

            }
        });
        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog alertDialog = new Dialog(Setting.this);
                LayoutInflater inflater = Setting.this.getLayoutInflater();
                View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);

                dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
                dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
                dismis = add_menu_layout.findViewById(R.id.discard_btn);
                clear = add_menu_layout.findViewById(R.id.cancle_btn);
                dialog_title.setText(R.string.log_out);
                dialog_message.setText("Are you sure you want to logout");
                clear.setText(R.string.log_out);
                dismis.setText("Cancel");
                dismis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Common.isNetworkConnected(Setting.this)) {
                            hitApiForLogout();
                        }
                        else {
                            Toast.makeText(Setting.this, "Check your network", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alertDialog.setContentView(add_menu_layout);
                alertDialog.show();
            }
        });
        edit_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Setting.this, ViewProfile.class);
                startActivity(intent);

            }
        });
    }

    private void hitApiForLogout() {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<AddComentModel> call = apiListener.logoutApi(userID);
        call.enqueue(new Callback<AddComentModel>() {
            @Override
            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                if (response.isSuccessful()){
                    AddComentModel data=response.body();
                    if (data.getCode().equalsIgnoreCase("201")){
                        Toast.makeText(Setting.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                        sendBroadcast(broadcastIntent);
                        sessionManager.logoutUser();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddComentModel> call, Throwable t) {
                Toast.makeText(Setting.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                settingProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialUi() {
        switch_notification=findViewById(R.id.switch_notification);
        switch_id=findViewById(R.id.switch_id);
        liked_post=findViewById(R.id.liked_post);
        blocked_users=findViewById(R.id.blocked_users);
        shoutout=findViewById(R.id.shoutout);
        reportLayout = findViewById(R.id.reportLayout);
        notificationLinear = findViewById(R.id.notificationLinear);
        imageBackSetting = findViewById(R.id.imageBackSetting);
        delete_account = findViewById(R.id.delete_account);
        clear_search_history = findViewById(R.id.clear_search_history);
        clear_conversion = findViewById(R.id.clear_conversion);
        clear_data = findViewById(R.id.clear_data);
        share_app = findViewById(R.id.share_app);
        log_out = findViewById(R.id.log_out);
        edit_photo = findViewById(R.id.edit_photo);
        settingProfileImage = findViewById(R.id.profile_image_setting);
        setting_user_dateofborth = findViewById(R.id.setting_user_dateofborth);
        setting_user_phone = findViewById(R.id.setting_user_phone);
        setting_email = findViewById(R.id.setting_email);
        setting_username = findViewById(R.id.setting_username);
        progress = findViewById(R.id.progress);
    }

    private void viewDataHitApi() {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<ViewProfileModel> call = apiListener.viewProfileAPI(userID);
        call.enqueue(new Callback<ViewProfileModel>() {
            @Override
            public void onResponse(Call<ViewProfileModel> call, Response<ViewProfileModel> response) {
                ViewProfileModel data = response.body();
                if (data != null) {
                    if (data.getCode().equalsIgnoreCase("201")) {
                        if (data.getData().get(0).getUserName() != null) {
                            setting_username.setText(data.getData().get(0).getUserName());
                        }
                        if (data.getData().get(0).getEmail() != null) {
                            setting_email.setText(data.getData().get(0).getEmail());
                        }

                        if (data.getData().get(0).getPhone() != null) {
                            setting_user_phone.setText(String.valueOf(data.getData().get(0).getPhone()));
                        }
                        if (data.getData().get(0).getDateOfBirth() != null) {
                            setting_user_dateofborth.setText(String.valueOf(data.getData().get(0).getDateOfBirth()));
                        }
                        if (data.getData().get(0).getUserImage() != null) {
                            String imageUri = Common.image_base_url + data.getData().get(0).getUserImage();
                            Uri uri = Uri.parse(imageUri);
                            Picasso.get().load(uri).into(settingProfileImage);
                        }
                        progress.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Toast.makeText(Setting.this, "Please update profile", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onFailure(Call<ViewProfileModel> call, Throwable t) {
                Toast.makeText(Setting.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void savetouchid(String Touchid) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Touchid", Touchid);
        editor.apply();

    }
}
