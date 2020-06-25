package com.designtech9studio.puntersapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.designtech9studio.puntersapp.Activity.FeedBackActivity;
import com.designtech9studio.puntersapp.Activity.VendorNewLeadActivity;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.BookingModel;
import com.designtech9studio.puntersapp.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    Context context;
    List<BookingModel> listItems;

    String message;

    public BookingAdapter(Context context, List<BookingModel> listItems, final String message) {
        this.context = context;
        this.listItems = listItems;
        this.message = message;

        Log.i("BookingAdapter", listItems.size()+"");
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View mView = LayoutInflater.from(context).inflate(R.layout.booking_appointment_row_file, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, int position) {
        final BookingModel bookingAppoinementModel = listItems.get(position);
        Log.i("Binding", bookingAppoinementModel.getTran_id()+"");
        //holder.serviceNameTv.setText(bookingAppoinementModel.get());
        holder.serviceNameTv.setText(bookingAppoinementModel.getServiceName());
        holder.bookingDate.setText("Date : " + bookingAppoinementModel.getDate());
        holder.bookingid.setText("Booking Id : " +bookingAppoinementModel.getTran_id());
        holder.bookingTime.setText( bookingAppoinementModel.getTime());
        holder.bookingAddress.setText(bookingAppoinementModel.getAddress());
        holder.bookingmode.setText(bookingAppoinementModel.getModeOfPayment());
        holder.bookingQty_tv.setText(bookingAppoinementModel.getQty() + "");
        holder.bookingVendorName_tv.setText(bookingAppoinementModel.getVendorUserName());
        //holder.bookingContact_tv.setText(bookingAppoinementModel.getVendorMobile());
        holder.bookingContact_tv.setText("+91 70604 70608");

        Log.i("Amount", bookingAppoinementModel.getAmount()+"");
        holder.bookingAmount.setText(String.valueOf(bookingAppoinementModel.getAmount()));

        holder.feedback_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FeedBackActivity.class);
                intent.putExtra(IntentsTags.BOOKING_ID, String.valueOf(bookingAppoinementModel.getTran_id()));
                intent.putExtra(IntentsTags.VENDOR_ID, bookingAppoinementModel.getVendorId());
                context.startActivity(intent);
            }
        });

       // holder.dummpyFeild_tv.setText(String.valueOf(bookingAppoinementModel.getAmount()));

        /*holder.orderStatusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
        holder.shareLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Location : \n " + bookingAppoinementModel.getAddress());
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView bookingDate, bookingTime, bookingAddress, bookingAmount, bookingmode, bookingid, serviceNameTv, feedback_tv;
        TextView bookingQty_tv, bookingVendorName_tv, bookingContact_tv;
        TextView dummpyFeild_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookingDate = itemView.findViewById(R.id.bookingDate);
            bookingTime = itemView.findViewById(R.id.bookingTime);
            bookingAddress = itemView.findViewById(R.id.bookingAddress);
            bookingAmount = itemView.findViewById(R.id.appointmentAmount);
            bookingmode = itemView.findViewById(R.id.bookingmode);
            bookingid = itemView.findViewById(R.id.bookingid);
            serviceNameTv = itemView.findViewById(R.id.serviceNameTv);
            feedback_tv = itemView.findViewById(R.id.book_feedback);
            bookingContact_tv = itemView.findViewById(R.id.bookingContact);
            bookingVendorName_tv = itemView.findViewById(R.id.bookingVendorName);
            bookingQty_tv  = itemView.findViewById(R.id.bookingQty);

            //dummpyFeild_tv = itemView.findViewById(R.id.dummpyFeild);
            //orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            //shareLocationTextView = itemView.findViewById(R.id.shareLocationTextView);


        }
    }



}
