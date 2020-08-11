package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.Adapter.newAdapter.NewSearchAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.SearchUserModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchUser extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ImageView imageBackSearch, shoutvideo, imageSerachuser;
    private RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    TextView dialog_title, dialog_message;
    private EditText editTextSearchUser;
    Button dismis, clear;
    SessionManager sessionManager;
    Retrofit retrofit;
    String email, userID;
    ProgressBar progress;
    SwipeRefreshLayout refresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        sessionManager = new SessionManager(getApplicationContext());
        initialUi();
        hidekeyboard();
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        userID = user.get(SessionManager.USER_ID);
        if (userID != null) {

            if (Common.isNetworkConnected(SearchUser.this)) {
                hitApi();
            } else {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(SearchUser.this, "Check your network", Toast.LENGTH_SHORT).show();
            }

        }

        imageBackSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        shoutvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(SearchUser.this,ShoutLoudVideos.class);
            startActivity(intent);

            }
        });
        imageSerachuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitUserSearchApi();
            }
        });
        editTextSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hitUserSearchApi();
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                        Toast.makeText(SearchUser.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SearchUser.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void hitUserSearchApi() {
        String text = editTextSearchUser.getText().toString().trim();
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<SearchUserModel> call = apiListener.searchUserClassApi(userID, text);
        call.enqueue(new Callback<SearchUserModel>() {
            @Override
            public void onResponse(Call<SearchUserModel> call, Response<SearchUserModel> response) {
                SearchUserModel data = response.body();
                if (data != null) {
                    if (data.getCode().equalsIgnoreCase("201")) {
                        if (data.getPostData() != null) {

                            NewSearchAdapter adapter = new NewSearchAdapter(userID,data, SearchUser.this);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchUser.this, 2);
                            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                            progress.setVisibility(View.INVISIBLE);
                        } else {

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchUserModel> call, Throwable t) {
                Toast.makeText(SearchUser.this, "failure", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void hitApi() {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<SearchUserModel> call = apiListener.searchUserClassApi(userID, "0");
        call.enqueue(new Callback<SearchUserModel>() {
            @Override
            public void onResponse(Call<SearchUserModel> call, Response<SearchUserModel> response) {
                if (response.isSuccessful()) {
                    if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                    }
                    SearchUserModel data = response.body();
                    if (data != null) {
                        if (data.getCode().equalsIgnoreCase("201")) {
                            if (data.getPostData() != null) {
                                NewSearchAdapter adapter = new NewSearchAdapter(userID,data, SearchUser.this);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchUser.this, 2);
                                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);
                                progress.setVisibility(View.INVISIBLE);
                            } else {}
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchUserModel> call, Throwable t) {
                if (refresh.isRefreshing()) {
                    refresh.setRefreshing(false);
                }
                Toast.makeText(SearchUser.this, "failure", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initialUi() {
        recyclerView = findViewById(R.id.recyclerSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        imageBackSearch = findViewById(R.id.imageBackSearch);
        shoutvideo = findViewById(R.id.shoutvideo);
        editTextSearchUser = findViewById(R.id.editTextSearchUser);
        progress = findViewById(R.id.progressSerachUser);
        imageSerachuser = findViewById(R.id.imageSerachuser);
        refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void hidekeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onRefresh() {
        hitApi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
