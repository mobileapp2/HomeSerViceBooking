package com.designtech9studio.puntersapp.Adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.designtech9studio.puntersapp.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    List<String> imageURL;
    Context context;

    public ImageAdapter(List<String> imageURL, Context context) {
        this.imageURL = imageURL;
        this.context = context;
        Log.i("ConstructerCalled", "True");
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.image_row_file,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        try{
            Log.i("ImageUrl", imageURL.get(position));
            Glide.with(this.context)
                    .load(imageURL.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
            Log.i("ImageUrl", "Loaded");
        }catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return imageURL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bannerImages);

        }
    }
}
