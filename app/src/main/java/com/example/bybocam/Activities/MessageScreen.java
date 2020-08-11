package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.Adapter.newAdapter.NewMessageScreenAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.GetFavouriteUserModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessageScreen extends AppCompatActivity {

    private ImageView imageBackMessage, imageViewADD, send_message;
    private EditText typeMessage, selectedUsers;
    RecyclerView recyclerView;
    SessionManager sessionManager;
    String userID;
    GetFavouriteUserModel data;
    Dialog dialog;

    ProgressBar progress, progress2121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_screen);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        initialUi();
        imageBackMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    StringBuilder builder = new StringBuilder();
                    for (String details : NewMessageScreenAdapter.userIds) {
                        builder.append(details + ",");
                    }
                    NewMessageScreenAdapter.userIds.clear();
                    String id = method(builder.toString());
                    String message = typeMessage.getText().toString();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    hitSendMessageApi(id, message);
                }
            }
        });
        imageViewADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitlikedUserApi();
            }
        });
        selectedUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitlikedUserApi();
            }
        });

    }

    private boolean checkValidation() {
        if (typeMessage.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show();
            return false;
        } else if (NewMessageScreenAdapter.userIds.size() <= 0) {
            Toast.makeText(this, "Please select users", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void hitSendMessageApi(String id, String message) {
        progress2121.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitClientIntance.retroInit();
        ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<AddComentModel> call = apiListener.addMessageclassAPI(userID, id, message);
        call.enqueue(new Callback<AddComentModel>() {
            @Override
            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                AddComentModel data = response.body();
                if (data.getCode().equalsIgnoreCase("201")) {
           //         Toast.makeText(MessageScreen.this, "Message sent", Toast.LENGTH_SHORT).show();
                    progress2121.setVisibility(View.INVISIBLE);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AddComentModel> call, Throwable t) {
                progress2121.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void hitlikedUserApi() {
        dialog = new Dialog(MessageScreen.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.custom_dialog_liked_user_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RelativeLayout mainview = (RelativeLayout) dialog.findViewById(R.id.mainview);
        mainview.getLayoutParams().width = getHeightWidth("width", MessageScreen.this) - 20;
        recyclerView = dialog.findViewById(R.id.recyclclerMessage);
        progress = dialog.findViewById(R.id.progress);
        NewMessageScreenAdapter newMessageScreenAdapter = new NewMessageScreenAdapter(data, MessageScreen.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MessageScreen.this));
        recyclerView.setAdapter(newMessageScreenAdapter);
        if (data != null) {
            progress.setVisibility(View.INVISIBLE);
        }
        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView submit = dialog.findViewById(R.id.submit);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                NewMessageScreenAdapter.usernames.clear();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                StringBuilder builder = new StringBuilder();
                for (String details : NewMessageScreenAdapter.usernames) {
                    builder.append(details + ",");
                }
                NewMessageScreenAdapter.usernames.clear();
                selectedUsers.setText(method(builder.toString()));
            }


        });
        dialog.show();
    }


    private void initialUi() {
        typeMessage = findViewById(R.id.editTextMessage);
        imageBackMessage = findViewById(R.id.imageBackMessage);
        imageViewADD = findViewById(R.id.imageViewADD);
        selectedUsers = findViewById(R.id.selectedUsers);
        send_message = findViewById(R.id.send_message);
        progress2121 = findViewById(R.id.progress2121);
        hitApiForLikedUser();
    }

    private void hitApiForLikedUser() {
        Retrofit retrofit = RetrofitClientIntance.retroInit();
        ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<GetFavouriteUserModel> call = apiListener.likedUserclassAPI(userID);
        call.enqueue(new Callback<GetFavouriteUserModel>() {
            @Override
            public void onResponse(Call<GetFavouriteUserModel> call, Response<GetFavouriteUserModel> response) {
                data = response.body();
                if (data.getCode().equalsIgnoreCase("201")) {
                } else {
                    Toast.makeText(MessageScreen.this, "You have no liked user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetFavouriteUserModel> call, Throwable t) {

            }
        });

    }

    public static int getHeightWidth(String mode, Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int lenthval = 0;
        if (mode.equalsIgnoreCase("height")) {
            lenthval = height;
        } else if (mode.equalsIgnoreCase("width")) {
            lenthval = width;
        }
        return lenthval;
    }

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
