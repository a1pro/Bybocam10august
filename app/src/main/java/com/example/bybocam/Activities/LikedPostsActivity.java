package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bybocam.Adapter.LikedPostAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Interface.UnLikePostInterface;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.LikedModel;
import com.example.bybocam.Model.LikedPostData;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LikedPostsActivity extends AppCompatActivity {
    private RecyclerView recycler_view;
    private ImageView imageBackSetting;
    LikedPostAdapter adapter;
    SessionManager session;
    private String userID;
    private List<LikedPostData> list = new ArrayList<>();
    Retrofit retrofit;
    private ProgressBar progress;
    private TextView tv_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_posts);
        recycler_view = findViewById(R.id.recycler_view);
        imageBackSetting = findViewById(R.id.imageBackSetting);
        progress = findViewById(R.id.progress);
        tv_alert=findViewById(R.id.tv_alert);

        session = new SessionManager(LikedPostsActivity.this.getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        userID = user.get(SessionManager.USER_ID);
        // Log.e("userid",userID);

        if (userID != null) {
            LikedPost();
        }


        imageBackSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void LikedPost() {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<LikedModel> call = apiListener.GetLikedPosts(userID);
        call.enqueue(new Callback<LikedModel>() {
            @Override
            public void onResponse(Call<LikedModel> call, Response<LikedModel> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    LikedModel data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        list.addAll(data.getPostData());

                        if (list.size() > 0) {
                            adapter = new LikedPostAdapter(LikedPostsActivity.this, list, unLikePostInterface);
                            recycler_view.setHasFixedSize(true);
                            recycler_view.setLayoutManager(new GridLayoutManager(LikedPostsActivity.this, 2));
                            recycler_view.setAdapter(adapter);

                        } else {
                            tv_alert.setVisibility(View.VISIBLE);
                       //     Toast.makeText(LikedPostsActivity.this, "o size", Toast.LENGTH_SHORT).show();
                        }
                        progress.setVisibility(View.INVISIBLE);


                    } else {
                      //  Toast.makeText(LikedPostsActivity.this, "" + data.getStatus(), Toast.LENGTH_SHORT).show();
                        tv_alert.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.INVISIBLE);

                    }
                }

            }

            @Override
            public void onFailure(Call<LikedModel> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(LikedPostsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    UnLikePostInterface unLikePostInterface = new UnLikePostInterface() {
        @Override
        public void Dounlike(String postid) {
            if (postid != null) {
                      HitUnlikePostApi(postid);
            }
        }
    };

    private void HitUnlikePostApi(String id) {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<AddComentModel> call = apiListener.postLikeAPI(userID,id,"0");
        call.enqueue(new Callback<AddComentModel>() {
            @Override
            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                if (response.isSuccessful()) {
                    AddComentModel data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        Toast.makeText(LikedPostsActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                        LikedPost();
                        progress.setVisibility(View.INVISIBLE);


                    } else {
                        Toast.makeText(LikedPostsActivity.this, "" + data.getStatus(), Toast.LENGTH_SHORT).show();

                        progress.setVisibility(View.INVISIBLE);

                    }
                }

            }

            @Override
            public void onFailure(Call<AddComentModel> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(LikedPostsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userID != null) {
            LikedPost();
        }
    }
}