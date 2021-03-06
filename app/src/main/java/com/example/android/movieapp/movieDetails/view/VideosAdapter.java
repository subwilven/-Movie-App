package com.example.android.movieapp.movieDetails.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieapp.POJO.Video;
import com.example.android.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by eslam on 15-Oct-17.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoHolder> implements VideosAdapterView{

    private List<Video> mData;
    OnVideoClicked mOnVideoClicked;
    Context mContext;

    @Override
    public void sendData(List<Video> videos) {
        this.mData = videos;
        notifyDataSetChanged();
    }

    public interface OnVideoClicked
    {
        void handleOnClickVideo(String key);
    }
    public VideosAdapter(OnVideoClicked onVideoClicked)
    {
        mOnVideoClicked=onVideoClicked;
    }
    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int layoutID = R.layout.list_item_video;
        View view = inflater.inflate(layoutID, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Video video = mData.get(position);
        holder.setNameTextView(video.getName());
        holder.setSizeTextView(video.getSize());
        holder.setVideoImage(video.getKey());
        holder.setTag(video.getKey());
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else
            return 0;

    }


    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        final TextView nameTextView;
        final TextView sizeTextView;
        final ImageView videoImage;

        public VideoHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.video_name_text_view);
            sizeTextView = (TextView) itemView.findViewById(R.id.video_size);
            videoImage= (ImageView) itemView.findViewById(R.id.video_image);
            itemView.setOnClickListener(this);
        }

        public void setVideoImage(String key)
        {
            Picasso.with(mContext)
                    .load(buildThumbnailUrl(key))
                    .into(videoImage);
        }
        public void setNameTextView(String text) {
            nameTextView.setText(text);
        }

        public void setSizeTextView(String text) {
            sizeTextView.setText(text);
        }

        public void setTag(String tag) {
            itemView.setTag(tag);
        }

        @Override
        public void onClick(View view) {
            mOnVideoClicked.handleOnClickVideo((String)itemView.getTag());
        }
    }
    private String buildThumbnailUrl(String videoId) {
        return "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
    }
}

