package com.designtech9studio.puntersapp.Adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.designtech9studio.puntersapp.Activity.CheckOutActivity;
import com.designtech9studio.puntersapp.Activity.SubCategoryActivity;
import com.designtech9studio.puntersapp.Helpers.LocalDatabaseHelper;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.R;

import java.util.List;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.ViewHolder>  {

    private List<CatSubChildModel> values;

    public Context context;
    public LocalDatabaseHelper localDatabaseHelper;


    public class ViewHolder extends RecyclerView.ViewHolder{

        public View layout;
        public TextView childCatName_tv, childCatPrice_tv, childDescription_tv;
        public ImageView childImage, inc_button, dec_button, delete_button;
        public TextView childCatQuantity_tv;


        public ViewHolder(View v)
        {
            super(v);
            layout = v;
            childCatName_tv = v.findViewById(R.id.checkOutchildCatName);
            childCatPrice_tv = v.findViewById(R.id.chckOutChildcatPrice);
            childImage = v.findViewById(R.id.checkOutchildImage);
            childCatQuantity_tv = v.findViewById(R.id.checkOutchildCatQuantity);
            inc_button = v.findViewById(R.id.checkOutchildCatIncrement);
            dec_button = v.findViewById(R.id.checkOutchildCatDecrement);
            delete_button = v.findViewById(R.id.childCatDelete);
        }
    }
    public CheckOutAdapter(List<CatSubChildModel> myDataset, Context context){
        values = myDataset;
        this.context = context;
        localDatabaseHelper = new LocalDatabaseHelper(context);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.check_out_row_file,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    public void onBindViewHolder(final ViewHolder holder, final int position){

        Log.i("Adapter", "onBindViewHolder");
        final CatSubChildModel mainCategory = values.get(position);
        try
        {

            holder.childCatName_tv.setText(mainCategory.getSubChildName());
            holder.childCatPrice_tv.setText(String.valueOf(Html.fromHtml("&#x20B9;") + " "+mainCategory.getAmount()));
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
                CheckOutActivity sub = (CheckOutActivity)context;
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
                    values.get(position).setQty(qty);

                }else {
                    localDatabaseHelper.deleteItem(mainCategory);
                    values.remove(position);
                    notifyItemRemoved(position);
                }


                Log.i("LocalDb", "Inserted In database DEC operation");
                CheckOutActivity sub = (CheckOutActivity)context;
                sub.setCountBoxAndTotal();
            }
        });

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localDatabaseHelper.deleteItem(mainCategory.getSubChildId());
                Log.i("LOCAlDB", "Cart Item Deleted");
                values.remove(position);
                notifyItemRemoved(position);
                CheckOutActivity sub = (CheckOutActivity)context;
                sub.setCountBoxAndTotal();
            }
        });

    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
