package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bybocam.Adapter.BlockedUserAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Interface.UnblockInterface;
import com.example.bybocam.Model.BlockedData;
import com.example.bybocam.Model.BlockedUser;
import com.example.bybocam.Model.ResponseData;
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

public class BlockedUsersActivity extends AppCompatActivity {
    private RecyclerView recycler_view;
    private ImageView imageBackSetting;
    private BlockedUserAdapter adapter;
    List<BlockedData> list=new ArrayList<>();
    private ProgressBar progress;
    Retrofit retrofit;
    private TextView tv_alert;
    SessionManager session;
    private String userID;
    TextView dialog_title, dialog_message;
    Button Unblock,clear;
    private String unblockid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);
        recycler_view=findViewById(R.id.recycler_view);
        imageBackSetting=findViewById(R.id.imageBackSetting);
        progress=findViewById(R.id.progress);
        tv_alert=findViewById(R.id.tv_alert);

        session = new SessionManager(BlockedUsersActivity.this.getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        userID = user.get(SessionManager.USER_ID);

        if (userID!=null){
            BlockUserApi();
        }

        imageBackSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private void BlockUserApi( ) {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<BlockedUser> call = apiListener.GetBlockUserList(userID);
        call.enqueue(new Callback<BlockedUser>() {
            @Override
            public void onResponse(Call<BlockedUser> call, Response<BlockedUser> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    BlockedUser data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        list.addAll(data.getData());

                        if (list.size()>0){
                            adapter  = new BlockedUserAdapter(BlockedUsersActivity.this,list,unblockInterface);
                            recycler_view.setHasFixedSize(true);
                            recycler_view.setLayoutManager(new LinearLayoutManager(BlockedUsersActivity.this));
                            recycler_view.setAdapter(adapter);
                        }
                        progress.setVisibility(View.INVISIBLE);




                    } else {
                        Toast.makeText(BlockedUsersActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                        tv_alert.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.INVISIBLE);

                    }
                }

            }

            @Override
            public void onFailure(Call<BlockedUser> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(BlockedUsersActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    UnblockInterface unblockInterface=new UnblockInterface() {
        @Override
        public void DoUnblock(String id) {
                  if (id!=null){
                       unblockid=id;
                      opendialog();
                  }
        }
    };


    private void opendialog() {
        final Dialog alertDialog = new Dialog(BlockedUsersActivity.this);
        LayoutInflater inflater = BlockedUsersActivity.this.getLayoutInflater();
        final View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);

        dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
        dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
        Unblock = add_menu_layout.findViewById(R.id.discard_btn);
        clear = add_menu_layout.findViewById(R.id.cancle_btn);
        dialog_title.setText("Unblock User");
        dialog_message.setText("Are you sure you want to Unblock the User");
        Unblock.setText("Unblock");
        clear.setText("Cancel");
        Unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnBlockUserApi();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.setContentView(add_menu_layout);

        alertDialog.show();
    }


    private void UnBlockUserApi () {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<ResponseData> call = apiListener.BlockUser(userID,unblockid);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {

                        Toast.makeText(BlockedUsersActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.INVISIBLE);
                        finish();



                    } else {
                        progress.setVisibility(View.INVISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
            }
        });

    }

}