package com.example.bybocam.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bybocam.Fragments.OneFragment;
import com.example.bybocam.R;


public class MediaStoreAdapter extends RecyclerView.Adapter<MediaStoreAdapter.ViewHolder>  {
    private Cursor mMediaStoreCursor;
    private  Context mActivity;
    private OnClickThumbListener mOnClickThumbListener;

    public interface OnClickThumbListener{
        void onClickImage(Uri imageUri);
        void onClickVideo(Uri videoUri);
    }

    public MediaStoreAdapter(Context activity,OnClickThumbListener onClickThumbListener) {
        this.mActivity = activity;
        this.mOnClickThumbListener=onClickThumbListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.media_image_view,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mActivity.getApplicationContext())
                .load(getUriFromMediaStore(position))
                .centerCrop()
                .override(126,122)
                .into(holder.getmImageView());
        if (!getUriFromMediaStore(position).toString().contains(".mp4")){
            holder.playImage.setVisibility(View.INVISIBLE);
        }

            Glide.with(mActivity.getApplicationContext())
                    .load(getUriFromMediaStore(position))
                    .centerCrop()
                    .into(OneFragment.mPreview);




    }

    @Override
    public int getItemCount() {
        return (mMediaStoreCursor == null) ? 0 :mMediaStoreCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView,playImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playImage=itemView.findViewById(R.id.playImage);
            mImageView=itemView.findViewById(R.id.mediaStoreImageView);
            mImageView.setOnClickListener(this);
        }
        public ImageView getmImageView(){
            return mImageView;
        }

        @Override
        public void onClick(View view) {
            getOnClickUri(getAdapterPosition());
        }
    }
    private Cursor swapCursor(Cursor cursor) {
        if (mMediaStoreCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mMediaStoreCursor;
        this.mMediaStoreCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }
    public void changeCursor(Cursor cursor){
        Cursor oldCursor = swapCursor(cursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    private Uri getUriFromMediaStore(int position){
        int dataIndex=mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
        mMediaStoreCursor.moveToPosition(position);
            String dataString=mMediaStoreCursor.getString(dataIndex);
            Uri mediaUri=Uri.parse("file://" + dataString);
            return mediaUri;
    }
    private void getOnClickUri(int position){
        int madiaTypeIndex=mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);
        int dataIndex=mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

        mMediaStoreCursor.moveToPosition(position);
        String dataString=mMediaStoreCursor.getString(dataIndex);
        Uri mediaUri=Uri.parse("file://" +  dataString);
        switch (mMediaStoreCursor.getInt(madiaTypeIndex)){
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                mOnClickThumbListener.onClickImage(mediaUri);
                break;
            case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
                mOnClickThumbListener.onClickVideo(mediaUri);
                break;
            default:

        }
    }
}
