package com.example.bybocam.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bybocam.Activities.Setting;
import com.example.bybocam.Activities.ViewAllFollowers;
import com.example.bybocam.Activities.ViewProfile;
import com.example.bybocam.Adapter.ProfileRecyclerAdapter;
import com.example.bybocam.Adapter.ProfileRecyclerViewAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Interface.DeletePost;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.Model.ResponseData;
import com.example.bybocam.Model.ViewProfileModel;
import com.example.bybocam.Model.ViewProfileOurUserModel;
import com.example.bybocam.Model.ViewThumbnails;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.BitmapUtils;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;
import com.hbb20.CountryCodePicker;
import com.iceteck.silicompressorr.SiliCompressor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView, recyclerView2;
    ImageView listViewRecycler, GridViewRecycler, listViewRecycler2, GridViewRecycler2, profile_image, settingProfile, followings;
    LinearLayout linearLayout1, linearLayout2;
    TextView profile_edit_profile, usernumber, NameProfile, userEmailProfile, addVideoProfile, postText, noOfFollowing, noOfFollowers, no_video_found, textFollowers;
    Retrofit retrofit;
    String userID, email, urlVideo1, urlVideo2, urlVideo3;
    SessionManager session;
    ProgressBar progress;
    public static ProgressBar progress1;
    Uri uri1, uri2, uri3;
    static Context context;
    public String videoName1, videoName2, videoName3, videoId1, videoId2, videoId3;
    ProfileRecyclerViewAdapter adapter21, adapter22, adapter23;
    int pos;
    int vID = 0;
    String id;
    private LinearLayout followingLinear, followerLinear;
    File f;
    SwipeRefreshLayout refresh;
    private int REQUEST_TAKE_GALLERY_VIDEO = 2;
    private int REQUEST_TAKE_GALLERY_VIDEO2 = 36;
    private File file;
    private ViewProfileOurUserModel data;
    private View viewofprofile;
    TextView dialog_title, dialog_message,text_flag;
    Button dismis, clear;
    private String posId;
    private String userno, countrycode;
    public static final String LOG_TAG = ProfileFragment.class.getSimpleName();
    public static final String FILE_PROVIDER_AUTHORITY = ".silicompressor.provider";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE_VID = 2;
    private static final int REQUEST_TAKE_VIDEO = 200;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VIDEO = 2;
    private int type1 = TYPE_VIDEO;
    File imageFile;
    String mCurrentPhotoPath;
    Uri capturedUri = null;
    Uri compressUri = null;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initialUi(view);
        context = container.getContext();
        session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        userID = user.get(SessionManager.USER_ID);
        userEmailProfile.setText(email);

        if (Common.isNetworkConnected(getContext())) {
            viewDataHitApi();
            //  getAllPostApi();

        } else {
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "Check your network", Toast.LENGTH_SHORT).show();
        }





        profile_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent(getActivity(), ViewProfile.class);
                startActivity(viewIntent);
            }
        });
        addVideoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog alertDialog = new Dialog(getContext());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);
                dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
                dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
                dismis = add_menu_layout.findViewById(R.id.discard_btn);
                clear = add_menu_layout.findViewById(R.id.cancle_btn);
                dialog_title.setText("Upload video");
                dialog_message.setText("Select option to proceed");
                clear.setText("Camera");
                dismis.setText("Gallery");
                dismis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        chooseVideoFromGallary();
                    }
                });

                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        chooseVideoFromCamera();
                    }
                });

                alertDialog.setContentView(add_menu_layout);
                alertDialog.show();

            }
        });

        listViewRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(getContext(), data, 1, deletePost);
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }
        });

        listViewRecycler2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout2.setVisibility(View.INVISIBLE);
                linearLayout1.setVisibility(View.VISIBLE);
                ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(getContext(), data, 1, deletePost);
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }
        });

        GridViewRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout1.setVisibility(View.INVISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(getContext(), data, 0, deletePost);
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adapter);
            }
        });

        GridViewRecycler2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout1.setVisibility(View.INVISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(getContext(), data, 0, deletePost);
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adapter);
            }
        });
        settingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Setting.class);
                startActivity(intent);
            }
        });

        followingLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent followingIntent = new Intent(getContext(), ViewAllFollowers.class);
                followingIntent.putExtra("userIdMe", userID);
                followingIntent.putExtra("type", "following");
                startActivity(followingIntent);
            }
        });
        followerLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent followersIntent = new Intent(getContext(), ViewAllFollowers.class);
                followersIntent.putExtra("userIdMe", userID);
                followersIntent.putExtra("type", "follower");
                startActivity(followersIntent);
            }
        });
        return view;
    }

    private void chooseVideoFromCamera() {
        requestPermissions(TYPE_VIDEO);
    }

    private void requestPermissions(int mediaType) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_STORAGE_VID);

        } else {
            if (mediaType == TYPE_VIDEO) {
                // Want to compress a video
                dispatchTakeVideoIntent();
            }

        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {

                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                capturedUri = FileProvider.getUriForFile(getContext(),
                        getActivity().getPackageName() + FILE_PROVIDER_AUTHORITY,
                        createMediaFile(TYPE_VIDEO));

                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedUri);
                Log.d(LOG_TAG, "VideoUri: " + capturedUri.toString());
                startActivityForResult(takeVideoIntent, REQUEST_TAKE_VIDEO);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private File createMediaFile(int type) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = (type == TYPE_IMAGE) ? "JPEG_" + timeStamp + "_" : "VID_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                type == TYPE_IMAGE ? Environment.DIRECTORY_PICTURES : Environment.DIRECTORY_MOVIES);
        File file = File.createTempFile(
                fileName,  /* prefix */
                type == TYPE_IMAGE ? ".jpg" : ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Get the path of the file created
        mCurrentPhotoPath = file.getAbsolutePath();
        Log.d(LOG_TAG, "mCurrentPhotoPath: " + mCurrentPhotoPath);
        return file;
    }

    //   private void getAllPostApi() {
//        if (refresh.isRefreshing()) {
//            refresh.setRefreshing(false);
//        }
//        retrofit = RetrofitClientIntance.retroInit();
//        final ApiListener apiListener = retrofit.create(ApiListener.class);
//        Call<GetAllPostModel> call = apiListener.getAllPostApi(userID);
//        call.enqueue(new Callback<GetAllPostModel>() {
//            @Override
//            public void onResponse(Call<GetAllPostModel> call, Response<GetAllPostModel> response) {
//                if (response.isSuccessful()) {
//                    if (refresh.isRefreshing()) {
//                        refresh.setRefreshing(false);
//                    }
//                    data = response.body();
//                    if (data.getCode().equalsIgnoreCase("201")) {
//                        if (data.getPostData().size() == 0) {
//                            postText.setText("You have no posts");
//                            postText.setVisibility(View.VISIBLE);
//                        }
//                        ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(getContext(), data, "1");
//                        recyclerView.setHasFixedSize(true);
//                        recyclerView.setNestedScrollingEnabled(false);
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//                        recyclerView.setLayoutManager(linearLayoutManager);
//                        recyclerView.setAdapter(adapter);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetAllPostModel> call, Throwable t) {
//                if (refresh.isRefreshing()) {
//                    refresh.setRefreshing(false);
//                }
//                Toast.makeText(getContext(), "Not get post", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }


    private void viewUserVideos() {
        try {
            retrofit = RetrofitClientIntance.retroInit();
            final ApiListener apiListener = retrofit.create(ApiListener.class);
            Call<ViewProfileModel> call = apiListener.viewProfileAPI(userID);
            call.enqueue(new Callback<ViewProfileModel>() {
                @Override
                public void onResponse(Call<ViewProfileModel> call, Response<ViewProfileModel> response) {

                    ViewProfileModel data = response.body();
                    if (data != null) {
                        if (data.getCode().equalsIgnoreCase("201")) {
                            progress1.setVisibility(View.INVISIBLE);
                            if (data.getUserVideos().size() >= 1) {
                                videoName1 = data.getUserVideos().get(0).getVideoThumbnailimg();
                                videoId1 = data.getUserVideos().get(0).getId();

                            } else {
                                no_video_found.setVisibility(View.VISIBLE);
                            }
                            if (data.getUserVideos().size() >= 2) {
                                videoName2 = data.getUserVideos().get(1).getVideoThumbnailimg();
                                videoId2 = data.getUserVideos().get(1).getId();

                            }
                            if (data.getUserVideos().size() >= 3) {
                                videoName3 = data.getUserVideos().get(2).getVideoThumbnailimg();
                                videoId3 = data.getUserVideos().get(2).getId();


                            }
                            if (videoName1 != null) {
                                urlVideo1 = Common.video_base_url + videoName1;
                                uri1 = Uri.parse(urlVideo1);

                            }
                            if (videoName2 != null) {
                                urlVideo2 = Common.video_base_url + videoName2;
                                uri2 = Uri.parse(urlVideo2);
                            }
                            if (videoName3 != null) {
                                addVideoProfile.setVisibility(View.INVISIBLE);
                                urlVideo3 = Common.video_base_url + videoName3;
                                uri3 = Uri.parse(urlVideo3);
                            }
                            if (uri3 != null) {
                                ViewThumbnails[] viewThumbnails = new ViewThumbnails[]{(new ViewThumbnails(uri1)),
                                        (new ViewThumbnails(uri2)), (new ViewThumbnails(uri3))};
                                adapter21 = new ProfileRecyclerViewAdapter(viewThumbnails, data, "0");
                                recyclerView2.setHasFixedSize(true);
                                recyclerView2.setNestedScrollingEnabled(false);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                                recyclerView2.setLayoutManager(gridLayoutManager);
                                recyclerView2.setAdapter(adapter21);
                                adapter21.SetOnItemClickListener(new ProfileRecyclerViewAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        chooseVideoFromGallary2();
                                        pos = position;
                                    }
                                });
                            } else if (uri2 != null) {
                                ViewThumbnails[] viewThumbnails = new ViewThumbnails[]{(new ViewThumbnails(uri1)),
                                        (new ViewThumbnails(uri2))};
                                adapter22 = new ProfileRecyclerViewAdapter(viewThumbnails, data, "0");
                                recyclerView2.setHasFixedSize(true);
                                recyclerView2.setNestedScrollingEnabled(false);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                                recyclerView2.setLayoutManager(gridLayoutManager);
                                recyclerView2.setAdapter(adapter21);
                                recyclerView2.setAdapter(adapter22);

                                adapter22.SetOnItemClickListener(new ProfileRecyclerViewAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        chooseVideoFromGallary2();
                                        pos = position;
                                    }
                                });

                            } else if (uri1 != null) {
                                ViewThumbnails[] viewThumbnails = new ViewThumbnails[]{(new ViewThumbnails(uri1))};
                                adapter23 = new ProfileRecyclerViewAdapter(viewThumbnails, data, "0");
                                recyclerView2.setHasFixedSize(true);
                                recyclerView2.setNestedScrollingEnabled(false);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                                recyclerView2.setLayoutManager(gridLayoutManager);
                                recyclerView2.setAdapter(adapter21);
                                recyclerView2.setAdapter(adapter23);


                                adapter23.SetOnItemClickListener(new ProfileRecyclerViewAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        chooseVideoFromGallary2();
                                        pos = position;
                                    }
                                });


                            }
                        } else {
                            progress1.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), "User have no videos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ViewProfileModel> call, Throwable t) {
                    Toast.makeText(getContext(), "failed to load user data", Toast.LENGTH_SHORT).show();
                    progress1.setVisibility(View.INVISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chooseVideoFromGallary2() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("video/*");
        startActivityForResult(openGalleryIntent, REQUEST_TAKE_GALLERY_VIDEO2);
    }

    private void editUserVideo(String path, File thumbnail) {
        if (pos == 0) {
            progress.setVisibility(View.VISIBLE);
            File videoFile = new File(path);

            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userID);
            RequestBody videoID = RequestBody.create(MediaType.parse("text/plain"), videoId1);
            String name = System.currentTimeMillis() + "_video.jpeg";
            RequestBody videoBody1 = RequestBody.create(MediaType.parse("video/*"), thumbnail);
            MultipartBody.Part vFile1 = MultipartBody.Part.createFormData("videoThumbnailimg", name, videoBody1);

            RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
            MultipartBody.Part vFile = MultipartBody.Part.createFormData("addVideo", videoFile.getName(), videoBody);
            retrofit = RetrofitClientIntance.retroInit();
            final ApiListener apiListener = retrofit.create(ApiListener.class);
            Call<AddComentModel> call = apiListener.uploadVideo(userId, videoID, vFile, vFile1);
            call.enqueue(new Callback<AddComentModel>() {
                @Override
                public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                    AddComentModel result = response.body();
                    Toast.makeText(getContext(), "Video updated successfully", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<AddComentModel> call, Throwable t) {
                    Log.e("error", t.getMessage());
                    Toast.makeText(getContext(), "updating failed", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                }
            });

        } else if (pos == 1) {
            progress.setVisibility(View.VISIBLE);
            File videoFile = new File(path);
            String name = System.currentTimeMillis() + "_video.jpeg";
            RequestBody videoBody1 = RequestBody.create(MediaType.parse("video/*"), thumbnail);
            MultipartBody.Part vFile1 = MultipartBody.Part.createFormData("videoThumbnailimg", name, videoBody1);
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userID);
            RequestBody videoID = RequestBody.create(MediaType.parse("text/plain"), videoId2);
            RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
            MultipartBody.Part vFile = MultipartBody.Part.createFormData("addVideo", videoFile.getName(), videoBody);
            retrofit = RetrofitClientIntance.retroInit();
            final ApiListener apiListener = retrofit.create(ApiListener.class);
            Call<AddComentModel> call = apiListener.uploadVideo(userId, videoID, vFile, vFile1);
            call.enqueue(new Callback<AddComentModel>() {
                @Override
                public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                    AddComentModel result = response.body();
                    Toast.makeText(getContext(), "Video updated successfully", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<AddComentModel> call, Throwable t) {
                    Log.e("error", t.getMessage());
                    Toast.makeText(getContext(), "updating failed", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                }
            });

        } else if (pos == 2) {
            progress.setVisibility(View.VISIBLE);
            File videoFile = new File(path);

            String name = System.currentTimeMillis() + "_video.jpeg";
            RequestBody videoBody1 = RequestBody.create(MediaType.parse("video/*"), thumbnail);
            MultipartBody.Part vFile1 = MultipartBody.Part.createFormData("videoThumbnailimg", name, videoBody1);


            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userID);
            RequestBody videoID = RequestBody.create(MediaType.parse("text/plain"), videoId3);
            RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
            MultipartBody.Part vFile = MultipartBody.Part.createFormData("addVideo", videoFile.getName(), videoBody);
            retrofit = RetrofitClientIntance.retroInit();
            final ApiListener apiListener = retrofit.create(ApiListener.class);
            Call<AddComentModel> call = apiListener.uploadVideo(userId, videoID, vFile, vFile1);
            call.enqueue(new Callback<AddComentModel>() {
                @Override
                public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                    AddComentModel result = response.body();
                    Toast.makeText(getContext(), "Video updated successfully", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<AddComentModel> call, Throwable t) {
//                    Log.e("error", t.getMessage());
                    Toast.makeText(getContext(), "Updating failed", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                }
            });

        }
    }


    private void initialUi(View view) {
        text_flag = view.findViewById(R.id.text_flag);
        usernumber = view.findViewById(R.id.usernumber);
        recyclerView = view.findViewById(R.id.myProfileRecyclerview);
        listViewRecycler = view.findViewById(R.id.listViewRecycler);
        GridViewRecycler = view.findViewById(R.id.GridViewRecycler);
        linearLayout1 = view.findViewById(R.id.gridlistlinear1);
        linearLayout2 = view.findViewById(R.id.gridlistlinear2);
        listViewRecycler2 = view.findViewById(R.id.listViewRecycler2);
        GridViewRecycler2 = view.findViewById(R.id.GridViewRecycler2);
        profile_edit_profile = view.findViewById(R.id.profile_edit_profile);
        NameProfile = view.findViewById(R.id.NameProfile);
        userEmailProfile = view.findViewById(R.id.userEmailProfile);
        progress = view.findViewById(R.id.progress);
        progress1 = view.findViewById(R.id.progress2);
        profile_image = view.findViewById(R.id.profile_image);
        recyclerView2 = view.findViewById(R.id.profile_recyler);
        viewofprofile = view.findViewById(R.id.viewofprofile);
        refresh = view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        settingProfile = view.findViewById(R.id.settingProfile);
        addVideoProfile = view.findViewById(R.id.addVideoProfile);
        postText = view.findViewById(R.id.postText);
        noOfFollowing = view.findViewById(R.id.noOfFollowing);
        noOfFollowers = view.findViewById(R.id.noOfFollowers);
        followings = view.findViewById(R.id.followings);
        no_video_found = view.findViewById(R.id.no_video_found);
        followingLinear = view.findViewById(R.id.followingLinear);
        followerLinear = view.findViewById(R.id.followerLinear);
        textFollowers = view.findViewById(R.id.textFollowers);

    }

    private void viewDataHitApi() {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<ViewProfileOurUserModel> call = apiListener.viewProfileOurUserAPI(userID, userID);
        call.enqueue(new Callback<ViewProfileOurUserModel>() {
            @Override
            public void onResponse(Call<ViewProfileOurUserModel> call, Response<ViewProfileOurUserModel> response) {
                progress.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                    }
                    data = response.body();
                    if (data != null) {
                        if (data.getCode().equalsIgnoreCase("201")) {
                            if (data.getData().get(0).getUserName() != null) {
                                NameProfile.setText(data.getData().get(0).getUserName());

                            }

                            if (data.getData().get(0).getUserImage() != null) {
                                String imageUri = Common.image_base_url + data.getData().get(0).getUserImage();
                                Uri uri = Uri.parse(imageUri);
                                Glide.with(getActivity().getApplicationContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.ALL).into(profile_image);

                            }

                            if (data.getData().get(0).getPhone() != null) {
                                userno = data.getData().get(0).getPhone();
                                usernumber.setText(userno);
                            }

                            if (data.getData().get(0).getCountryCode() != null) {
                                countrycode = data.getData().get(0).getCountryCode();
                                text_flag.setText(countrycode);


     //    Log.e("newcode",newcode);


                            }

                        }
                        noOfFollowers.setText(String.valueOf(data.getFollowers()));
                        noOfFollowing.setText(String.valueOf(data.getTotalfavouriteusers()));
                    }
                    if (response.isSuccessful()) {

                        if (data.getPostData().size() == 0) {
                            postText.setText("You have no posts");
                            postText.setVisibility(View.VISIBLE);
                        } else {
                            postText.setVisibility(View.INVISIBLE);
                        }

                        ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(getContext(), data, 1, deletePost);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setNestedScrollingEnabled(false);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        //linearLayoutManager.setStackFromEnd(true);
                        linearLayoutManager.setReverseLayout(true);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                    }

                    progress1.setVisibility(View.VISIBLE);
                    viewUserVideos();
                }
            }

            @Override
            public void onFailure(Call<ViewProfileOurUserModel> call, Throwable t) {
                if (refresh.isRefreshing()) {
                    refresh.setRefreshing(false);
                }
                //  Toast.makeText(getActivity(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    public void chooseVideoFromGallary() {

        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("video/*");
        startActivityForResult(openGalleryIntent, REQUEST_TAKE_GALLERY_VIDEO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
            if (data != null) {

                Uri uri = data.getData();
                if (uri != null) {
                    String selectedPathVideo = getRealPathFromURIPath(uri, getActivity());
                    if (selectedPathVideo != null) {
                        file = new File(selectedPathVideo);
                    }
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = (type1 == TYPE_IMAGE) ? "JPEG_" + timeStamp + "_" : "VID_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        type1 == TYPE_IMAGE ? Environment.DIRECTORY_PICTURES : Environment.DIRECTORY_MOVIES);
                File file1 = null;
                try {
                    file1 = File.createTempFile(
                            fileName,  /* prefix */
                            type1 == TYPE_IMAGE ? ".jpg" : ".mp4",         /* suffix */
                            storageDir      /* directory */
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Get the path of the file created

                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
                if (f.mkdirs() || f.isDirectory())
                    //compress and output new video specs
                    new VideoCompressAsyncTask(getContext()).execute(file.getAbsolutePath(), f.getPath());


            }
        } else {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO2) {
                if (data != null) {

                    Uri uri = data.getData();
                    if (uri != null) {
                        String selectedPathVideo = getRealPathFromURIPath(uri, getActivity());
                        if (selectedPathVideo != null) {
                            file = new File(selectedPathVideo);
                        } else {
                            Toast.makeText(getActivity(), "selectedPathVideo2 null", Toast.LENGTH_SHORT).show();

                        }
                    }

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String fileName = (type1 == TYPE_IMAGE) ? "JPEG_" + timeStamp + "_" : "VID_" + timeStamp + "_";
                    File storageDir = Environment.getExternalStoragePublicDirectory(
                            type1 == TYPE_IMAGE ? Environment.DIRECTORY_PICTURES : Environment.DIRECTORY_MOVIES);
                    File file1 = null;
                    try {
                        file1 = File.createTempFile(
                                fileName,  /* prefix */
                                type1 == TYPE_IMAGE ? ".jpg" : ".mp4",         /* suffix */
                                storageDir      /* directory */
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Get the path of the file created

                    File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
                    if (f.mkdirs() || f.isDirectory())
                        //compress and output new video specs
                        new VideoCompressAsyncTask2(getContext()).execute(file.getAbsolutePath(), f.getPath());
                }
            }
        }
        if (requestCode == REQUEST_TAKE_VIDEO && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                //create destination directory
                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
                if (f.mkdirs() || f.isDirectory())
                    //compress and output new video specs
                    new VideoCompressAsyncTask(getContext()).execute(mCurrentPhotoPath, f.getPath());
            }
        }
    }

    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        Context mContext;

        public VideoCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
            Toast.makeText(mContext, "Please wait video is uploading", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return filePath;

        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            imageFile = new File(compressedFilePath);
            float length = imageFile.length() / 1024f; // Size in KB
            String value;
            if (length >= 1024)
                value = length / 1024f + " MB";
            else
                value = length + " KB";
            getThumbnail(imageFile);

        }
    }

    class VideoCompressAsyncTask2 extends AsyncTask<String, String, String> {

        Context mContext;

        public VideoCompressAsyncTask2(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return filePath;

        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            imageFile = new File(compressedFilePath);
            float length = imageFile.length() / 1024f; // Size in KB
            String value;
            if (length >= 1024)
                value = length / 1024f + " MB";
            else
                value = length + " KB";
            getThumbnail2(imageFile);

        }
    }

    private void getThumbnail2(File imageFile) {
        Uri uri = Uri.parse(imageFile.getAbsolutePath());
        if (uri != null) {
            String selectedPathVideo = getRealPathFromURIPath(uri, getActivity());
            if (selectedPathVideo != null) {
                file = new File(selectedPathVideo);
                try {
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedPathVideo, MediaStore.Video.Thumbnails.MICRO_KIND);
                    String name = System.currentTimeMillis() + "_video.jpeg";
                    final String path = BitmapUtils.insertImage(getActivity().getContentResolver(), thumb, name, null);
                    if (!TextUtils.isEmpty(path)) {
                        f = new File(getActivity().getCacheDir(), name);
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editUserVideo(selectedPathVideo, f);
            }
        }
    }

    private void getThumbnail(File imageFile) {
        Uri uri = Uri.parse(imageFile.getAbsolutePath());
        if (uri != null) {
            final String selectedPathVideo = getRealPathFromURIPath(uri, getActivity());
            if (selectedPathVideo != null) {
                //uploadVideoToServer(selectedPathVideo);
                try {
                    final Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedPathVideo, MediaStore.Video.Thumbnails.MINI_KIND);
                    String name = System.currentTimeMillis() + "_video.jpeg";
                    final String path = BitmapUtils.insertImage(getActivity().getContentResolver(), thumb, name, null);
                    if (!TextUtils.isEmpty(path)) {
                        f = new File(getActivity().getCacheDir(), name);
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

                    }
                    uploadVideoToServer(selectedPathVideo, f);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "selectedPathVideo null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "uri null", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadVideoToServer(String pathToVideoFile, File encodeImage) {
        File videoFile = new File(pathToVideoFile);
        String name = System.currentTimeMillis() + "_video.jpeg";
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userID);
        RequestBody videoID = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(vID));
        RequestBody videoBody1 = RequestBody.create(MediaType.parse("video/*"), encodeImage);
        MultipartBody.Part vFile1 = MultipartBody.Part.createFormData("videoThumbnailimg", name, videoBody1);

        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("addVideo", videoFile.getName(), videoBody);
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<AddComentModel> call = apiListener.uploadVideo(userId, videoID, vFile, vFile1);
        call.enqueue(new Callback<AddComentModel>() {
            @Override
            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                AddComentModel result = response.body();
                Toast.makeText(getContext(), "Video upload successfully", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<AddComentModel> call, Throwable t) {
                Log.e("error", t.getMessage());
                Toast.makeText(getContext(), "Uploading failed", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });
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

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(6);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString() + ".jpeg";
    }

    @Override
    public void onRefresh() {
        viewDataHitApi();
        // getAllPostApi();

    }


    DeletePost deletePost = new DeletePost() {
        @Override
        public void getpost(String postid) {
            if (postid != null) {
                posId = postid;
                openAlertDialog();
            }
        }
    };

    private void openAlertDialog() {
        final Dialog alertDialog = new Dialog(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);

        dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
        dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
        dismis = add_menu_layout.findViewById(R.id.discard_btn);
        clear = add_menu_layout.findViewById(R.id.cancle_btn);
        dialog_title.setText("Delete Post !");
        dismis.setText("Delete");
        dialog_message.setText("Are you sure you want to Delete Post");
        clear.setText("Cancel");
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        dismis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeletePost();
                alertDialog.cancel();


            }
        });

        alertDialog.setContentView(add_menu_layout);

        alertDialog.show();
    }


    private void DeletePost() {
        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<ResponseData> call = apiListener.DeletePost(userID, posId);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData data = response.body();
                    if (data.getCode().equalsIgnoreCase("201")) {
                        viewDataHitApi();
                        Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.INVISIBLE);

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