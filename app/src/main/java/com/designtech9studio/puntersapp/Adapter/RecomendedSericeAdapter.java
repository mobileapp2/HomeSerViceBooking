package com.designtech9studio.puntersapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.designtech9studio.puntersapp.Activity.AllServices;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Model.CategoryMasterModel;
import com.designtech9studio.puntersapp.R;

import java.util.List;

public class RecomendedSericeAdapter extends RecyclerView.Adapter<RecomendedSericeAdapter.ViewHolder> {


    private List<CategoryMasterModel> values;

    public Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recommendedImage);
            textView = itemView.findViewById(R.id.recommendedText);

        }
    }
    public RecomendedSericeAdapter(List<CategoryMasterModel> myDataset, Context context){
        values = myDataset;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recommended_service_row,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CategoryMasterModel model = values.get(position);

        holder.textView.setText(model.getCatName());
        Glide.with(this.context)
                .load(model.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllServices.class);
                intent.putExtra(IntentsTags.CAT_ID, String.valueOf(model.getCatId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }


}
