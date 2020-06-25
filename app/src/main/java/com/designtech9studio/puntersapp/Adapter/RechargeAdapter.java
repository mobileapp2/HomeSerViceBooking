package com.designtech9studio.puntersapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.designtech9studio.puntersapp.Model.BookingModel;
import com.designtech9studio.puntersapp.Model.RechargeModel;
import com.designtech9studio.puntersapp.R;

import java.util.List;

public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.ViewHolder> {

    Context context;
    List<RechargeModel> listItems;

    String message;

    public RechargeAdapter(Context context, List<RechargeModel> listItems, final String message) {
        this.context = context;
        this.listItems = listItems;
        this.message = message;

        Log.i("BookingAdapter", listItems.size()+"");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View mView = LayoutInflater.from(context).inflate(R.layout.recharge_row_file, parent, false);
        return new ViewHolder(mView);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RechargeModel rechargeModel = listItems.get(position);
        //holder.serviceNameTv.setText(rechargeModel.get());

        holder.bookingDate.setText("Date : " + rechargeModel.getTimeStamp());
        holder.bookingmode.setText(rechargeModel.getPaymentMode());
        Log.i("Amount", rechargeModel.getCartAmount()+"");
        holder.bookingAmount.setText(String.valueOf(rechargeModel.getCartAmount()));
        holder.coins_tv.setText(String.valueOf(rechargeModel.getCoins()));

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView bookingDate, bookingTime, bookingAddress, bookingAmount, bookingmode, bookingid, serviceNameTv, coins_tv;
        TextView qty_tv, customerName_tv, contact_tv, accept_tv;
        TextView dummpyFeild_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookingDate = itemView.findViewById(R.id.bookingDate);
            bookingAmount = itemView.findViewById(R.id.appointmentAmount);
            bookingmode = itemView.findViewById(R.id.bookingmode);
            coins_tv = itemView.findViewById(R.id.coinsTxt);


        }
    }
}

