package com.example.bybocam.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bybocam.Adapter.ListviewAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Interface.ListInterface;
import com.example.bybocam.Model.FriendsData;
import com.example.bybocam.Model.GetFavouriteUserModel;
import com.example.bybocam.Model.GetFrindsModel;
import com.example.bybocam.Model.ResponseData;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.BitmapUtils;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

public class ShareVideoActivity extends AppCompatActivity {
    private Button bt_tagfriend;
    private ImageView img_thumb,imageBackReport;
    private ListView listview;
    private ListviewAdapter adapter;
    private List<FriendsData> list = new ArrayList<>();
    SessionManager session;
    private String UserID;
    ArrayList<String> list2 = new ArrayList<>();
    boolean checklist = false;
    File file;
    File f;
    private TextView tv_share;
    private  String User_friendsId;
    private EditText et_desc;
    private ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_video);
        session = new SessionManager(ShareVideoActivity.this.getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        //  email = user.get(SessionManager.KEY_EMAIL);
        UserID = user.get(SessionManager.USER_ID);
        et_desc=findViewById(R.id.et_desc);
        bt_tagfriend = findViewById(R.id.bt_tagfriend);
        listview = findViewById(R.id.listview);
        imageBackReport=findViewById(R.id.imageBackReport);
        tv_share = findViewById(R.id.tv_share);
        img_thumb = findViewById(R.id.img_thumb);
        progress=findViewById(R.id.progress);

        String path = getIntent().getStringExtra("flie");
        if (path != null) {
            file = new File(path);
            getThumbnail(file);

            Bitmap bmThumbnail;
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(path,
                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

            if (bmThumbnail != null) {
                img_thumb.setImageBitmap(bmThumbnail);
            }
        }


        imageBackReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String userids = String.valueOf(list2);
                String friendsuser_id = userids.replace("[", "").replace("]", "");
                 User_friendsId=friendsuser_id.replace(", ", ",");;
//                Log.e("ids",User_friendsId);
                 if (Common.isNetworkConnected(ShareVideoActivity.this)){
                     if (Validation()) {
                         Addinfluence();
                     }
                 }else {
                     Toast.makeText(ShareVideoActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                 }
            }
        });


        bt_tagfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checklist == false) {
                    hitApiForLikedUser();
                    checklist = true;
                } else {
                    list.clear();
                    list2.clear();
                    adapter = new ListviewAdapter(ShareVideoActivity.this, list, listInterface);
                    listview.setAdapter(adapter);
                    checklist = false;
                }

            }
        });

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//
//                FriendsData dataModel= list.get(position);
////                list.checked = !dataModel.checked;
//                adapter.notifyDataSetChanged();
//
//
//            }
//        });
    }


    private void hitApiForLikedUser() {
        Retrofit retrofit = RetrofitClientIntance.retroInit();
        ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<GetFrindsModel> call = apiListener.Friendslist(UserID);
        call.enqueue(new Callback<GetFrindsModel>() {
            @Override
            public void onResponse(Call<GetFrindsModel> call, Response<GetFrindsModel> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    GetFrindsModel data = response.body();
                    if (data != null) {
                        list.addAll(data.getData());
                        adapter = new ListviewAdapter(ShareVideoActivity.this, list, listInterface);
                        listview.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<GetFrindsModel> call, Throwable t) {

            }
        });

    }

    ListInterface listInterface = new ListInterface() {
        @Override
        public void getId(String id, int pos) {
            if (id != null) {
                try {
                    list2.add(pos, id);
                } catch (Exception e) {

                }
            } else {
                try {
                    list2.remove(pos);
                } catch (Exception e) {

                }

            }


        }


    };

    private void getThumbnail(File imageFile) {
        Uri uri = Uri.parse(imageFile.getAbsolutePath());
        if (uri != null) {
            final String selectedPathVideo = getRealPathFromURIPath(uri, ShareVideoActivity.this);
            if (selectedPathVideo != null) {
                //uploadVideoToServer(selectedPathVideo);
                try {
                    final Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedPathVideo, MediaStore.Video.Thumbnails.MINI_KIND);
                    String name = System.currentTimeMillis() + "_video.jpeg";
                    final String path = BitmapUtils.insertImage(ShareVideoActivity.this.getContentResolver(), thumb, name, null);
                    if (!TextUtils.isEmpty(path)) {
                        f = new File(ShareVideoActivity.this.getCacheDir(), name);
                        try {
                            f.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//Convert bitmap to byte array
                        Bitmap bitmap = thumb;
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(f);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write(bitmapdata);
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.e("thumbfile", f.getAbsolutePath());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ShareVideoActivity.this, "selectedPathVideo null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ShareVideoActivity.this, "uri null", Toast.LENGTH_SHORT).show();
        }
    }


    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    private void Addinfluence() {
        try {
            RequestBody senderId = RequestBody.create(MediaType.parse("text/plain"), UserID);
            RequestBody revicersIds = RequestBody.create(MediaType.parse("text/plain"), User_friendsId);
            RequestBody discriptions = RequestBody.create(MediaType.parse("text/plain"), et_desc.getText().toString());

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part RandomVideo = MultipartBody.Part.createFormData("RandomVideo", file.getName(), requestFile);

            RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            MultipartBody.Part videoImage = MultipartBody.Part.createFormData("videoImage", f.getName(), requestFile2);

            progress.setVisibility(View.VISIBLE);
//            retrofit = RetrofitClientIntance.retroInit();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .build();


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
            Call<ResponseData> call = apiListener.AddRandomVideo(RandomVideo,videoImage,senderId,revicersIds,discriptions);
            call.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        progress.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                Toast.makeText(ShareVideoActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                                //         progress.setVisibility(View.INVISIBLE);
                            } else {
                                progress.setVisibility(View.GONE);
                            }
                        }
                    }else {
                        Toast.makeText(ShareVideoActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    Toast.makeText(ShareVideoActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.e("exception",e.getMessage());
        }
    }


    private boolean Validation (){
        if (et_desc.getText().toString().length()>50){
            Toast.makeText(this, "Description Should be max 50 Charachters", Toast.LENGTH_SHORT).show();
            return false;
        }else if (list2.size()==0){
            Toast.makeText(this, "Please Tag Atleast One Person", Toast.LENGTH_SHORT).show();
            return false;
        }

       return true;
    }
}