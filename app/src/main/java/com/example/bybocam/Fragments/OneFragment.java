package com.example.bybocam.Fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bybocam.Activities.FIlerImage;
import com.example.bybocam.Activities.Gallary;
import com.example.bybocam.Adapter.MediaStoreAdapter;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.BitmapUtils;
import com.example.bybocam.Utils.Common;
import com.example.bybocam.Utils.SessionManager;
import com.google.android.material.appbar.AppBarLayout;
import com.iceteck.silicompressorr.SiliCompressor;
import com.jsibbold.zoomage.ZoomageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class OneFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MediaStoreAdapter.OnClickThumbListener {
    private final static int READ_EXTERNAL_STORAGE_PERMISSION_RESULT = 0;
    private final static int MEDIA_STORE_LOADER_ID = 0;
    private RecyclerView mThumnailRecylerView;
    private MediaStoreAdapter mMediaStoreAdapter;
    private AppBarLayout mAppBarContainer;
    private TextView dialog_title, dialog_message;
    private Button dismis, clear;
    private SessionManager session;
    private String userID;
    private Retrofit retrofit;
    private File fil1;
    private String dirPath;
    private File f;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VIDEO = 2;
    private int type1 = TYPE_VIDEO;
    private File imageFile;
    public static ZoomageView mPreview;
    private Uri photoUri;

    public OneFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        mAppBarContainer = view.findViewById(R.id.mAppBarContainer);
        mThumnailRecylerView = view.findViewById(R.id.thumbnailRecylerView);
        mPreview = view.findViewById(R.id.mPreview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mThumnailRecylerView.setLayoutManager(gridLayoutManager);
        mMediaStoreAdapter = new MediaStoreAdapter(this.getActivity(), this);
        mMediaStoreAdapter.notifyDataSetChanged();
        mThumnailRecylerView.setAdapter(mMediaStoreAdapter);
        chechReadExternalstorage();
        Gallary.nextTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoUri != null) {
                    Intent intent = new Intent(getContext(), FIlerImage.class);
                    intent.setData(photoUri);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION_RESULT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLoaderManager().initLoader(MEDIA_STORE_LOADER_ID, null, this);

                }
                break;
            default:
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void chechReadExternalstorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getLoaderManager().initLoader(MEDIA_STORE_LOADER_ID, null, this);
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "App needs to view thumbnails", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_RESULT);
            }
        } else {
            getLoaderManager().initLoader(MEDIA_STORE_LOADER_ID, null, this);

        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MEDIA_TYPE
        };
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        return new CursorLoader(
                getContext(),
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mMediaStoreAdapter.changeCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        mMediaStoreAdapter.changeCursor(null);
    }

    @Override
    public void onClickImage(Uri imageUri) {
        Gallary.nextTxt.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(imageUri).centerCrop().into(mPreview);
        photoUri = imageUri;
        mAppBarContainer.setExpanded(true, true);

    }

    @Override
    public void onClickVideo(Uri videoUri) {
        Gallary.nextTxt.setVisibility(View.INVISIBLE);
        if (Common.isNetworkConnected(getActivity().getApplicationContext())) {
            dirPath = String.valueOf(videoUri);
            String fileName1 = dirPath.substring(dirPath.lastIndexOf('/') + 1);
            fil1 = new File(videoUri.getPath());
            // Get length of file in bytes
            long fileSizeInBytes = fil1.length();
            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            long fileSizeInKB = fileSizeInBytes / 1024;
            //  Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            long fileSizeInMB = fileSizeInKB / 1024;
            if (fileSizeInMB < 80) {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = (type1 == TYPE_IMAGE) ? "JPEG_" + timeStamp + "_" : "VID_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        type1 == TYPE_IMAGE ? Environment.DIRECTORY_PICTURES : Environment.DIRECTORY_MOVIES);
                File file1 = null;
                try {
                    file1 = File.createTempFile(
                            fileName,  /* prefix */
                            type1 == TYPE_IMAGE ? ".jpg" : ".mp4",
                            storageDir      /* directory */
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final Dialog alertDialog = new Dialog(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);

                dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
                dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
                dismis = add_menu_layout.findViewById(R.id.discard_btn);
                clear = add_menu_layout.findViewById(R.id.cancle_btn);
                dialog_title.setText(R.string.uplaodVideo);
                dialog_message.setText("Upload selected video in your profile");
                dismis.setText("Cancel");
                clear.setText("Upload Video");
                dismis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
                        if (f.mkdirs() || f.isDirectory())
                            //compress and output new video specs
                            new VideoCompressAsyncTask(getContext()).execute(fil1.getAbsolutePath(), f.getPath());
                    }
                });
                alertDialog.setContentView(add_menu_layout);
                alertDialog.show();

            } else {
                Toast.makeText(getActivity(), "File size in not greater than 80 mb", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getContext(), "Check your network", Toast.LENGTH_SHORT).show();
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
            Gallary.progress.setVisibility(View.VISIBLE);
            Toast.makeText(mContext, "Please wait Compressing is video", Toast.LENGTH_SHORT).show();
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
            if (Common.isNetworkConnected(getActivity().getApplicationContext())) {
                hitApi(imageFile);
            } else {
                Toast.makeText(getContext(), "Check your network", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void hitApi(final File video) {
//        final Dialog alertDialog = new Dialog(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        final View add_menu_layout = inflater.inflate(R.layout.custom_layout_dialog, null);
//
//        dialog_title = add_menu_layout.findViewById(R.id.dialog_title);
//        dialog_message = add_menu_layout.findViewById(R.id.dialog_message);
//        dismis = add_menu_layout.findViewById(R.id.discard_btn);
//        clear = add_menu_layout.findViewById(R.id.cancle_btn);
//        dialog_title.setText(R.string.uplaodVideo);
//        dialog_message.setText("Upload selected video in your profile");
//        dismis.setText("Cancel");
//        clear.setText("Upload Video");
//        dismis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.cancel();
//            }
//        });
//
//        clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
        String mVideoFileName = fil1.getAbsolutePath();
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mVideoFileName, MediaStore.Video.Thumbnails.MINI_KIND);
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
//                alertDialog.cancel();
        Gallary.progress.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "Uploading....", Toast.LENGTH_SHORT).show();
        uplaodVideo(f, video);
//            }
//        });

//        alertDialog.setContentView(add_menu_layout);
//        alertDialog.show();
    }

    private void uplaodVideo(File thumbnail, File video) {
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userID);
        RequestBody postType = RequestBody.create(MediaType.parse("text/plain"), "1");
        String name = System.currentTimeMillis() + "_video.jpeg";
        RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), thumbnail);
        MultipartBody.Part userImage1 = MultipartBody.Part.createFormData("postVideoThumbnailImg", name, requestFile1);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), video);
        MultipartBody.Part userImage = MultipartBody.Part.createFormData("postVideoImg", video.getName(), requestFile);

        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<AddComentModel> call = apiListener.addPost(userId, postType, userImage, userImage1);
        call.enqueue(new Callback<AddComentModel>() {
            @Override
            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
                Gallary.progress.setVisibility(View.INVISIBLE);
                AddComentModel data = response.body();
                if (data.getCode().equalsIgnoreCase("201")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Video Uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "errorMessage" + data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddComentModel> call, Throwable t) {
                Gallary.progress.setVisibility(View.INVISIBLE);
                Log.e("error", "" + t.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), "Failed to upload video", Toast.LENGTH_SHORT).show();
            }
        });
    }
}