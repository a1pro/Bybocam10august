package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bybocam.Adapter.AllCommentsRecyclerAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.GetAllCommentsModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllComments extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView imageBackAllComments, send_message;
    private String postId, userID;
    private SessionManager session;
    private Retrofit retrofit;
    private ProgressBar progress;
    private EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comments);
        intilaUi();
        Intent callingIntent = this.getIntent();
        if (callingIntent != null) {
            postId = callingIntent.getStringExtra("postId");
        }
        if (postId != null) {
            hitCommentApi(postId);
        }
        imageBackAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextMessage.getText().toString().isEmpty()) {
                    Toast.makeText(AllComments.this, "Please enter comment", Toast.LENGTH_SHORT).show();
                } else {
                    progress.setVisibility(View.VISIBLE);
                    hitAddCommentApi(userID, postId, editTextMessage.getText().toString());
                }
            }
        });

    }

    private void hitAddCommentApi(String userID, final String postId, String comment) {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<AddComentModel> call = apiListener.addCommentAPI(userID, postId, comment);
        call.enqueue(new Callback<AddComentModel>() {
            @Override
            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                if (response.isSuccessful()) {
                    AddComentModel data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        Toast.makeText(AllComments.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                        hitCommentApi(postId);
                        editTextMessage.setText("");
                        hideKeyboard(AllComments.this);
                        progress.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddComentModel> call, Throwable t) {
                Toast.makeText(AllComments.this, "Failed to upload comment", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (postId != null) {
            hitCommentApi(postId);
        }
    }

    private void hitCommentApi(String postId) {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<GetAllCommentsModel> call = apiListener.getAllCommentsclassAPI(postId);
        call.enqueue(new Callback<GetAllCommentsModel>() {
            @Override
            public void onResponse(Call<GetAllCommentsModel> call, Response<GetAllCommentsModel> response) {
                if (response.isSuccessful()) {
                    GetAllCommentsModel data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        AllCommentsRecyclerAdapter adapter = new AllCommentsRecyclerAdapter(AllComments.this, data);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AllComments.this));
                        if (data.getData().size() != 0) {
                            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                        }
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        progress.setVisibility(View.INVISIBLE);
                    } else {
                        progress.setVisibility(View.INVISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<GetAllCommentsModel> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void intilaUi() {
        recyclerView = findViewById(R.id.all_comments_Recycler_view);
        imageBackAllComments = findViewById(R.id.imageBackAllComments);
        progress = findViewById(R.id.progress);
        editTextMessage = findViewById(R.id.editTextMessage);
        send_message = findViewById(R.id.send_message);
        session = new SessionManager(AllComments.this.getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        progress.setVisibility(View.VISIBLE);
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
