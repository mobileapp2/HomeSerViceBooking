package com.designtech9studio.puntersapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.designtech9studio.puntersapp.Activity.AllServices;
import com.designtech9studio.puntersapp.Activity.SubCategoryActivity;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Model.CategoryMasterModel;
import com.designtech9studio.puntersapp.R;

import java.util.List;

public class MainPageAdapterLower extends RecyclerView.Adapter<MainPageAdapterLower.ViewHolder>{

    private List<CategoryMasterModel> values;

    public Context context;


    public class ViewHolder extends RecyclerView.ViewHolder{

        public View layout;
        public TextView serviceHeading_txtView, serviceDescription_txt;
        public ImageView serverImage;
        public CardView cardView;

        public ViewHolder(View v)
        {
            super(v);
            layout = v;
            cardView = v.findViewById(R.id.main_page_salon_card);
            serviceHeading_txtView = v.findViewById(R.id.serviceHeading);
            serverImage = v.findViewById(R.id.service_image);
            serviceDescription_txt = v.findViewById(R.id.serviceDescription);

        }
    }
    public MainPageAdapterLower(List<CategoryMasterModel> myDataset, Context context){
        values = myDataset;
        this.context = context;
    }

    public MainPageAdapterLower.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_file_client_home_page,parent,false);
        MainPageAdapterLower.ViewHolder vh = new MainPageAdapterLower.ViewHolder(v);
        return vh;
    }



    public void onBindViewHolder(MainPageAdapterLower.ViewHolder holder, final int position){

        Log.i("Adapter", "onBindViewHolder");
        final CategoryMasterModel mainCategory = values.get(position);
        try
        {

            holder.serviceHeading_txtView.setText(mainCategory.getCatName());
            holder.serviceDescription_txt.setText(mainCategory.getDescription());

            //Picasso.with(this).load("htt://cdn.journaldev.com/wp-content/uploads/2016/11/android-image-picker-project-structure.png").into(imageView)
            String _strGetImage = mainCategory.getImage();
            //~/UploadImage/CategoryImage/26/Untitled_design_(25).png
            System.out.println("Image URL: " + _strGetImage);

            Glide.with(this.context)
                    .load(_strGetImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.serverImage);
            /*Picasso
                    .get()
                    .load(_strGetImage)
                    .resize(50, 50)
                    .noFade()
                    .into(holder.serverImage);*/
        }
        catch (Exception ex)
        {

        }

        try {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                   /* String _strCatID = itemsArrayList.get(position).getCatID();
                    String _strsubCatID = itemsArrayList.get(position).getSubCatId();
                    String _strChildSubCatID = itemsArrayList.get(position).getChildSubCatId();*/
                    //Intent intent = new Intent(context, SubCategoryActivity.class);
                    Intent intent = new Intent(context, AllServices.class);
                    intent.putExtra(IntentsTags.CAT_ID, String.valueOf(mainCategory.getCatId()));
                    context.startActivity(intent);
                }
            });
        }
        catch (Exception ex)
        {

        }
        //Picasso.get().load(classListItems.getImg()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}