package com.designtech9studio.puntersapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.designtech9studio.puntersapp.Activity.CheckOutActivity;
import com.designtech9studio.puntersapp.Activity.SubCategoryActivity;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.LocalDatabaseHelper;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.Model.SubCategoryModel;
import com.designtech9studio.puntersapp.R;

import java.util.List;

public class AllServiceListAdapter extends RecyclerView.Adapter<AllServiceListAdapter.ViewHolder>  {

    private List<SubCategoryModel> values;

    public Context context;
    public LocalDatabaseHelper localDatabaseHelper;


    public class ViewHolder extends RecyclerView.ViewHolder{

        public View layout;
        public TextView subCatName_tv;
        public ImageView subcatImage, clickButtonImage;
        public LinearLayout clickLayout;


        public ViewHolder(View v) {
            super(v);
            layout = v;

            clickLayout = v.findViewById(R.id.allServicelayout);
            subCatName_tv = v.findViewById(R.id.allServiceName);
            subcatImage = v.findViewById(R.id.allServiceViewImage);
            clickButtonImage = v.findViewById(R.id.allServiceClickButton);
        }
    }
    public AllServiceListAdapter(List<SubCategoryModel> myDataset, Context context){
        values = myDataset;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.all_serviice_row_file,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    public void onBindViewHolder(final ViewHolder holder, final int position){

        Log.i("Adapter", "onBindViewHolder");
        final SubCategoryModel mainCategory = values.get(position);
        try
        {

            holder.subCatName_tv.setText(mainCategory.getSubcategoryName());

            Glide.with(this.context)
                    .load(mainCategory.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.subcatImage);
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
        }

        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent to sub category*/
                Intent intent = new Intent(context, SubCategoryActivity.class);
                intent.putExtra(IntentsTags.CAT_ID, String.valueOf(mainCategory.getCatID()));
                intent.putExtra(IntentsTags.SUB_CAT_ID,String.valueOf(mainCategory.getSubCatId()));
                Log.i("CatId", mainCategory.getCatID()+ "");
                Log.i("SUbCatId", mainCategory.getSubCatId() + "");
                context.startActivity(intent);
            }
        });
        holder.clickButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
