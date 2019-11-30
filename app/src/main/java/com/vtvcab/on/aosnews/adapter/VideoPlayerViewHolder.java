package com.vtvcab.on.aosnews.adapter;


import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import com.vtvcab.on.aosnews.R;
import com.vtvcab.on.aosnews.model.MediaObject;


public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {

    FrameLayout media_container;
    TextView title, tvTimeDate, tvContent;
    ImageView thumbnail, volumeControl;
    ProgressBar progressBar;
    View parent;
    RequestManager requestManager;

    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        media_container = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.tvTitle);
        tvTimeDate = itemView.findViewById(R.id.tvTimeDate);
        tvContent = itemView.findViewById(R.id.tvContent);
        progressBar = itemView.findViewById(R.id.progressBar);
        volumeControl = itemView.findViewById(R.id.volume_control);
    }

    public void onBind(MediaObject mediaObject, RequestManager requestManager) {
        this.requestManager = requestManager;
        parent.setTag(this);
        title.setText(mediaObject.getTitle());
        tvTimeDate.setText(mediaObject.getTimeDate());
        tvContent.setText(mediaObject.getDescription());

        this.requestManager
                .load(mediaObject.getThumbnail())
                .into(thumbnail);
    }

}














