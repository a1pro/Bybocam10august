package com.example.bybocam.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coremedia.iso.boxes.Container;
import com.example.bybocam.Interface.ProgressBarListener;
import com.example.bybocam.ProgressBar.SegmentedProgressBar;
import com.example.bybocam.R;
import com.example.bybocam.Utils.BitmapUtils;
import com.example.bybocam.Utils.Variables;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VideoCameraActivity extends AppCompatActivity {
private ImageView thumb;
private File file;
File f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_camera);
        thumb=findViewById(R.id.thumb);

        String video_url=getIntent().getStringExtra("path");
      //  String video_url= Variables.outputfile2;

        if (video_url!=null){
            Toast.makeText(this, "==="+video_url, Toast.LENGTH_SHORT).show();
            file = new File(video_url);
            getThumbnail(file);

            Bitmap bmThumbnail;
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(video_url,
                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

            if (bmThumbnail != null) {
                thumb.setImageBitmap(bmThumbnail);
            }

        }


    }

    private void getThumbnail(File imageFile) {
        Uri uri = Uri.parse(imageFile.getAbsolutePath());
        if (uri != null) {
            final String selectedPathVideo = getRealPathFromURIPath(uri, VideoCameraActivity.this);
            if (selectedPathVideo != null) {
                //uploadVideoToServer(selectedPathVideo);
                try {
                    final Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedPathVideo, MediaStore.Video.Thumbnails.MINI_KIND);
                    String name = System.currentTimeMillis() + "_video.jpeg";
                    final String path = BitmapUtils.insertImage(VideoCameraActivity.this.getContentResolver(), thumb, name, null);
                    if (!TextUtils.isEmpty(path)) {
                        f = new File(VideoCameraActivity.this.getCacheDir(), name);
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
                Toast.makeText(VideoCameraActivity.this, "selectedPathVideo null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(VideoCameraActivity.this, "uri null", Toast.LENGTH_SHORT).show();
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


}