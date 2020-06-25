package com.designtech9studio.puntersapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.designtech9studio.puntersapp.Activity.SubCategoryActivity;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.LocalDatabaseHelper;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.Model.CategoryMasterModel;
import com.designtech9studio.puntersapp.R;

import java.util.List;

public class ChildCatAdapter  extends RecyclerView.Adapter<ChildCatAdapter.ViewHolder>{

    private List<CatSubChildModel> values;

    public Context context;
    public LocalDatabaseHelper localDatabaseHelper;


    public class ViewHolder extends RecyclerView.ViewHolder{

        public View layout;
        public TextView childCatName_tv, childCatPrice_tv, childDescription_tv;
        public ImageView childImage, inc_button, dec_button;
        public TextView childCatQuantity_tv;
        public TextView childQTY_tv;


        public ViewHolder(View v)
        {
            super(v);
            layout = v;
            childCatName_tv = v.findViewById(R.id.childCatName);
            childCatPrice_tv = v.findViewById(R.id.childcatPrice);
            childImage = v.findViewById(R.id.childImage);
            childDescription_tv = v.findViewById(R.id.childDescription_TV);
            childCatQuantity_tv = v.findViewById(R.id.childCatQuantity);
            inc_button = v.findViewById(R.id.childCatIncrement);
            dec_button = v.findViewById(R.id.childCatDecrement);
            childQTY_tv = v.findViewById(R.id.childCatQuantity);
        }
    }
    public ChildCatAdapter(List<CatSubChildModel> myDataset, Context context){
        values = myDataset;
        this.context = context;
        localDatabaseHelper = new LocalDatabaseHelper(context);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.child_sub_category_row_file,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    public void onBindViewHolder(final ViewHolder holder, final int position){

        Log.i("Adapter", "onBindViewHolder");
        final CatSubChildModel mainCategory = values.get(position);
        try
        {

            holder.childCatName_tv.setText(mainCategory.getSubChildName());
            holder.childDescription_tv.setText(mainCategory.getChildDescription());
            holder.childCatPrice_tv.setText(Html.fromHtml("&#x20B9;")+ " " +String.valueOf(mainCategory.getAmount()));
            holder.childCatQuantity_tv.setText(String.valueOf(mainCategory.getQty()));

            Glide.with(this.context)
                    .load(mainCategory.getChildImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.childImage);

            String _strGetImage = mainCategory.getChildImage();
            //~/UploadImage/CategoryImage/26/Untitled_design_(25).png
            System.out.println("Image URL: " + _strGetImage);
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
        }

        holder.inc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(holder.childCatQuantity_tv.getText().toString());
                qty+=1;
                holder.childCatQuantity_tv.setText(String.valueOf(qty));
                /*Insert into dataBase*/
                localDatabaseHelper.insertData(mainCategory, false);
                Log.i("LocalDb", "Inserted In database INC operation");
                SubCategoryActivity sub = (SubCategoryActivity)context;
                sub.setCountBoxAndTotal();
                values.get(position).setQty(qty);
            }
        });
        holder.dec_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(holder.childCatQuantity_tv.getText().toString());
                qty-=1;
                if (qty<0) qty=0;
                holder.childCatQuantity_tv.setText(String.valueOf(qty));

                if (qty>0) {

                    localDatabaseHelper.insertData(mainCategory, true);

                }else {
                    localDatabaseHelper.deleteItem(mainCategory);
                }

                values.get(position).setQty(qty);
                Log.i("LocalDb", "Inserted In database DEC operation");
                SubCategoryActivity sub = (SubCategoryActivity)context;
                sub.setCountBoxAndTotal();
            }
        });


    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public interface MyCallback{
        void onItemClicked();
    }

    private MyCallback listener;

    public void setOnItemClickListener(MyCallback callback){
        listener = callback;
    }
}