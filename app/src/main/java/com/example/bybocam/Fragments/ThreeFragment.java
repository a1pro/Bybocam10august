package com.example.bybocam.Fragments;


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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.coremedia.iso.boxes.Container;
import com.example.bybocam.Activities.Gallary;
import com.example.bybocam.Activities.Home;
import com.example.bybocam.Activities.VideoCameraActivity;
import com.example.bybocam.Interface.ApiListener;
import com.example.bybocam.Interface.ProgressBarListener;
import com.example.bybocam.Model.AddComentModel;
import com.example.bybocam.ProgressBar.SegmentedProgressBar;
import com.example.bybocam.R;
import com.example.bybocam.URL.RetrofitClientIntance;
import com.example.bybocam.Utils.BitmapUtils;
import com.example.bybocam.Utils.SessionManager;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ThreeFragment extends Fragment implements View.OnClickListener{
    CameraView cameraView;
    File file;
    int number=0;
    String outputFilePath=null;
    private int REQUEST_TAKE_GALLERY_VIDEO = 2;
    ArrayList<String> videopaths=new ArrayList<>();

    ImageView record_image;
    ImageButton done_btn;
    boolean is_recording=false;
    boolean is_flash_on=false;
    private Retrofit retrofit;

    ImageButton flash_btn;

    SegmentedProgressBar video_progress;

    RelativeLayout camera_options;

    ImageButton rotate_camera;
    private ImageView img_gallery_video,img_profile,invite;
    public static int Sounds_list_Request_code=1;

    private final static int READ_EXTERNAL_STORAGE_PERMISSION_RESULT = 0;
    private static final String VIDEO_DIRECTORY_NAME = "Bybocam";
    int sec_passed=0;
    SessionManager session;
    private String userID;
    File f;


    public ThreeFragment() {
        // Required empty public constructor
    }


    public static View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_three, container, false);
        session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        chechReadExternalstorage();
        video_progress=view.findViewById(R.id.video_progress);




        Variables.Selected_sound_id="null";
        Variables.recording_duration=Variables.max_recording_duration;

        cameraView = view.findViewById(R.id.camera);
        camera_options=view.findViewById(R.id.camera_options);

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
            }

            @Override
            public void onError(CameraKitError cameraKitError) {
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });


        record_image=view.findViewById(R.id.record_image);
     //   record_image.setBackgroundColor(Color.WHITE);

        //   findViewById(R.id.upload_layout).setOnClickListener(this);


        //  done_btn=view.findViewById(R.id.done);
        //done_btn.setEnabled(false);
      //  done_btn.setOnClickListener(this);
        Gallary.nextTxt.setVisibility(View.GONE);


        rotate_camera=view.findViewById(R.id.rotate_camera);
        rotate_camera.setBackgroundColor(Color.TRANSPARENT);
        rotate_camera.setOnClickListener(this);
        flash_btn=view.findViewById(R.id.flash_camera);
        flash_btn.setBackgroundColor(Color.TRANSPARENT);
        flash_btn.setOnClickListener(this);

        //    findViewById(R.id.Goback).setOnClickListener(this);

//        add_sound_txt=findViewById(R.id.add_sound_txt);
//        add_sound_txt.setOnClickListener(this);


//        Intent intent=getIntent();
//        if(intent.hasExtra("sound_name")){
//            add_sound_txt.setText(intent.getStringExtra("sound_name"));
//            Variables.Selected_sound_id=intent.getStringExtra("sound_id");
//            PreparedAudio();
//        }

        Gallary.nextTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                append();
                getThumbnail(file);
            }
        });



        // this is code hold to record the video
        final Timer[] timer = {new Timer()};
        record_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    timer[0] =new Timer();

                    timer[0].schedule(new TimerTask() {
                        @Override
                        public void run() {

                           getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!is_recording)
                                        Start_or_Stop_Recording();
                                }
                            });

                        }
                    }, 200);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    timer[0].cancel();
                    if(is_recording){
                        Start_or_Stop_Recording();
                    }
                }
                return false;
            }

        });

        initlize_Video_progress();
        return view;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rotate_camera:
                RotateCamera();
                break;



//            case R.id.done:
//                append();
//                break;

            case R.id.record_image:
                Start_or_Stop_Recording();
                break;

            case R.id.flash_camera:

                if(is_flash_on){
                    is_flash_on=false;
                    cameraView.setFlash(0);
                    flash_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off_black_24dp));

                }else {
                    is_flash_on=true;
                    cameraView.setFlash(CameraKit.Constants.FLASH_TORCH);
                    flash_btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off_black_24dp));
                }

                break;

        }

    }

    private boolean append() {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {


                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        progressDialog.setMessage("Please wait..");
                        progressDialog.show();
                    }
                });

                ArrayList<String> video_list=new ArrayList<>();
                for (int i=0;i<videopaths.size();i++){

                    File file=new File(videopaths.get(i));
                    if(file.exists()) {

                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(getActivity(), Uri.fromFile(file));
                        String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
                        boolean isVideo = "yes".equals(hasVideo);

                        if (isVideo && file.length() > 3000) {
                            Log.d("resp", videopaths.get(i));
                            video_list.add(videopaths.get(i));
                        }else {
                            Toast.makeText(getActivity(), "More than 3 sec", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Log.e("dfodsfjidjfosd","file not exists");
                    }
                }



                try {

                    Movie[] inMovies = new Movie[video_list.size()];
                    try {

                        for (int i = 0; i < video_list.size(); i++) {

                            inMovies[i] = MovieCreator.build(video_list.get(i));
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("erroriscoming",e.getMessage()+"  ,  "+e.getLocalizedMessage());
                    }


                    List<Track> videoTracks = new LinkedList<Track>();
                    List<Track> audioTracks = new LinkedList<Track>();
                    for (Movie m : inMovies) {
                        for (Track t : m.getTracks()) {
                            if (t.getHandler().equals("soun")) {
                                audioTracks.add(t);
                            }
                            if (t.getHandler().equals("vide")) {
                                videoTracks.add(t);
                            }
                        }
                    }
                    Movie result = new Movie();
                    if (audioTracks.size() > 0) {
                        result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
                    }
                    if (videoTracks.size() > 0) {
                        result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
                    }

                    Container out = new DefaultMp4Builder().build(result);


                    if(audio!=null){
                        outputFilePath=Variables.outputfile;
                    }else {
                        outputFilePath=Variables.outputfile2;
                    }

                    FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
                    out.writeContainer(fos.getChannel());
                    fos.close();

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();

                            if(audio!=null){
                                Merge_withAudio();
                            }
                            else {
                                Go_To_preview_Activity();
                            }

                        }
                    });
                } catch (Exception e) {
                    Log.e("execptionaarahihe",""+e.getMessage());
                }
            }
        }).start();



        return true;
    }

    // this will add the select audio with the video
    public void Merge_withAudio(){

        String root = Environment.getExternalStorageDirectory().toString();
        String audio_file;
        audio_file =Variables.app_folder+Variables.SelectedAudio_AAC;

        String video = root + "/"+"output.mp4";
        String finaloutput = root + "/"+"output2.mp4";

//        Merge_Video_Audio merge_video_audio=new Merge_Video_Audio(MyCameraActivity.this);
//        merge_video_audio.doInBackground(audio_file,video,finaloutput);

    }

    public void Go_To_preview_Activity(){

    }

    public void Start_or_Stop_Recording(){

        if (!is_recording && sec_passed<(Variables.recording_duration/1000)-1) {
            number=number+1;

            is_recording=true;

            file = new File(Variables.root + "/" + "myvideo"+(number)+".mp4");
            videopaths.add(Variables.root + "/" + "myvideo"+(number)+".mp4");
            cameraView.captureVideo(file);


            if(audio!=null)
                audio.start();



            video_progress.resume();


            //  done_btn.setBackgroundResource(R.mipmap.tick);
     //       done_btn.setEnabled(true);
            Gallary.nextTxt.setVisibility(View.VISIBLE);

            record_image.setImageDrawable(getResources().getDrawable(R.mipmap.click));

            camera_options.setVisibility(View.GONE);
            // add_sound_txt.setClickable(false);
            rotate_camera.setVisibility(View.GONE);

        }

        else if (is_recording) {

            is_recording=false;

            video_progress.pause();
            video_progress.addDivider();

            if(audio!=null)
                audio.pause();

            cameraView.stopVideo();


            if(sec_passed>((Variables.recording_duration/1000)/3)) {
                //  done_btn.setBackgroundResource(R.mipmap.tick);
          //      done_btn.setEnabled(true);
                Gallary.nextTxt.setVisibility(View.VISIBLE);
            }

            record_image.setImageDrawable(getResources().getDrawable(R.mipmap.click_button));
            camera_options.setVisibility(View.VISIBLE);

        }

        else if(sec_passed>(Variables.recording_duration/1000)) {
            Toast.makeText(getContext(), "Alert"+ "Video only can be a " +""+ (int) Variables.recording_duration / 1000 + " S", Toast.LENGTH_SHORT).show();
        }



    }

    // this will play the sound with the video when we select the audio
    MediaPlayer audio;
    public  void PreparedAudio(){
        File file=new File(Variables.app_folder+ Variables.SelectedAudio_AAC);
        if(file.exists()) {
            audio = new MediaPlayer();
            try {
                audio.setDataSource(Variables.app_folder + Variables.SelectedAudio_AAC);
                audio.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(getContext(), Uri.fromFile(file));
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            final int file_duration = Integer.parseInt(durationStr);

            if(file_duration<Variables.max_recording_duration){
                Variables.recording_duration=file_duration;
                initlize_Video_progress();
            }

        }


    }
    public void RotateCamera(){
        cameraView.toggleFacing();
    }


    public void initlize_Video_progress(){
        sec_passed=0;
        video_progress=view.findViewById(R.id.video_progress);
        video_progress.enableAutoProgressView(Variables.recording_duration);
        video_progress.setDividerColor(Color.WHITE);
        video_progress.setDividerEnabled(true);
        video_progress.setDividerWidth(4);
        video_progress.setShader(new int[]{Color.CYAN, Color.CYAN, Color.CYAN});

        video_progress.SetListener(new ProgressBarListener() {
            @Override
            public void TimeinMill(long mills) {
                sec_passed = (int) (mills/1000);

                if(sec_passed>(Variables.recording_duration/1000)){
                    Start_or_Stop_Recording();
                }

            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        try {

            if (audio != null) {
                audio.stop();
                audio.reset();
                audio.release();
            }
            cameraView.stop();

        }catch (Exception e){

        }
    }


    private void chechReadExternalstorage() {


        if (Build.VERSION.SDK_INT >= 23) {
            if ((getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) || (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {
                //   Log.v(TAG,"Permission is granted1");
            } else {

                //   Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_RESULT);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //  Log.v(TAG,"Permission is granted1");
            //  return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION_RESULT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }else {
                    Toast.makeText(getContext(), "Please allow permission", Toast.LENGTH_SHORT).show();
                //    finish();
                }
                break;
            default:
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void uplaodVideo(File videoFile, File fileThumbnail) {
        String name = System.currentTimeMillis() + "_video.jpeg";

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userID);
        RequestBody postType = RequestBody.create(MediaType.parse("text/plain"), "1");

        RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), fileThumbnail);
        MultipartBody.Part userImage1 = MultipartBody.Part.createFormData("postVideoThumbnailImg", name, requestFile1);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);
        MultipartBody.Part userImage = MultipartBody.Part.createFormData("postVideoImg", videoFile.getName(), requestFile);

        retrofit = RetrofitClientIntance.retroInit();
        final ApiListener apiListener = retrofit.create(ApiListener.class);
        Call<AddComentModel> call = apiListener.addPost(userId, postType, userImage, userImage1);
        call.enqueue(new Callback<AddComentModel>() {
            @Override
            public void onResponse(Call<AddComentModel> call, Response<AddComentModel> response) {
            //    progress.setVisibility(View.INVISIBLE);
                AddComentModel data = response.body();
                if (data.getCode().equalsIgnoreCase("201")) {
                    Toast.makeText(getContext(), "Video Uploaded", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), Home.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddComentModel> call, Throwable t) {
             //   progress.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Failed to upload video", Toast.LENGTH_SHORT).show();
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

                        Log.e("thumbfile", f.getAbsolutePath());
                        uplaodVideo(file,f);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "selectedPathVideo null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "uri null", Toast.LENGTH_SHORT).show();
        }
    }
}