package com.example.bybocam.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bybocam.Interface.FilterListFragmentListener;
import com.example.bybocam.R;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.MyViewHolder> {

    private List<ThumbnailItem> thumbnailItemList;
    private FilterListFragmentListener listener;
    private Context mContext;
    private int selectedIndex = 0;

    public ThumbnailAdapter(Context context, List<ThumbnailItem> thumbnailItemList, FilterListFragmentListener listener) {
        mContext = context;
        this.thumbnailItemList = thumbnailItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumbnail_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final ThumbnailItem thumbnailItem = thumbnailItemList.get(position);

        holder.thumbnail.setImageBitmap(thumbnailItem.image);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFIlterSelected(thumbnailItem.filter);
                selectedIndex = position;
                notifyDataSetChanged();
            }
        });
        holder.filter_name.setText(thumbnailItem.filterName);

        if (selectedIndex == position) {
            holder.filter_name.setTextColor(ContextCompat.getColor(mContext, R.color.selected_filter));
        } else {
            holder.filter_name.setTextColor(ContextCompat.getColor(mContext, R.color.normal_filter));
        }
    }

    @Override
    public int getItemCount() {
        return thumbnailItemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView filter_name;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
            filter_name = view.findViewById(R.id.filter_name);
        }
    }

}
