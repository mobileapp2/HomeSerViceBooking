package com.designtech9studio.puntersapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.designtech9studio.puntersapp.Activity.ClientHomepageActivity;
import com.designtech9studio.puntersapp.Activity.FeedBackActivity;
import com.designtech9studio.puntersapp.Activity.SelectModeOfPayment;
import com.designtech9studio.puntersapp.Helpers.Constant;
import com.designtech9studio.puntersapp.Helpers.DistanceComparator;
import com.designtech9studio.puntersapp.Helpers.DistanceHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.LocalDatabaseHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.BookingModel;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.Model.VendorProfileModel;
import com.designtech9studio.puntersapp.R;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class VendorBookingAdapter extends RecyclerView.Adapter<VendorBookingAdapter.ViewHolder> {

    Context context;
    List<BookingModel> listItems;
    boolean hideFeatures;

    String message;

    public VendorBookingAdapter(Context context, List<BookingModel> listItems, final String message, boolean hideFeatures) {
        this.context = context;
        this.listItems = listItems;
        this.message = message;
        this.hideFeatures = hideFeatures;

        Log.i("BookingAdapter", listItems.size()+"");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View mView = LayoutInflater.from(context).inflate(R.layout.vendor_booking_appointment_row_file, parent, false);
        return new ViewHolder(mView);
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final BookingModel bookingAppoinementModel = listItems.get(position);
        Log.i("Binding", bookingAppoinementModel.getTran_id()+"");
        //holder.serviceNameTv.setText(bookingAppoinementModel.get());

        holder.bookingDate.setText("Date : " + bookingAppoinementModel.getDate());
        holder.bookingid.setText("Booking Id : " +bookingAppoinementModel.getTran_id());
        holder.bookingTime.setText( bookingAppoinementModel.getTime());
        holder.bookingAddress.setText(bookingAppoinementModel.getAddress());
        holder.bookingmode.setText(bookingAppoinementModel.getModeOfPayment());
        holder.qty_tv.setText(""+bookingAppoinementModel.getQty());
        holder.customerName_tv.setText(bookingAppoinementModel.getCustomerName());
        //holder.contact_tv.setText(bookingAppoinementModel.getCustomerMobile());
        holder.contact_tv.setText("+91 70604 70608");
        holder.serviceNameTv.setText(bookingAppoinementModel.getServiceName());


        Log.i("Amount", bookingAppoinementModel.getAmount()+"");


        holder.bookingAmount.setText(String.valueOf(bookingAppoinementModel.getAmount()));

        if (bookingAppoinementModel.getIsCancelled() == 1) {
            holder.status_tv.setText("Cancelled");
            holder.accept_tv.setEnabled(false);
            holder.cancel_tv.setEnabled(false);
        }else if (bookingAppoinementModel.getIsAccepted() == 1) {
            holder.status_tv.setText("Accepted");
            holder.cancel_tv.setEnabled(false);
            holder.accept_tv.setEnabled(false);
        }else {
            holder.status_tv.setText("Pending");
        }


        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setTitle("Cancel");

// Set up the input
        final EditText input = new EditText(new ContextThemeWrapper(context, R.style.EditTextTheme));
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT);

        input.setHint("Why you want to cancel?");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(convertPixelsToDp(20,context), 0, 0, 0);
        input.setLayoutParams(lp);
        builder.setView(input, 50, 10, 50, 0);

// Set up the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cancelReason = input.getText().toString();
                FindVendorTask findVendorTask = new FindVendorTask();
                /*Striing 0-> userId
                 * 1->vendor Id
                 * 2->tranId
                 * 3 - Cancel reason*/
                String userId = bookingAppoinementModel.getUserId()+"";
                String vendorId = bookingAppoinementModel.getVendorId() +"" ;
                String traId = bookingAppoinementModel.getTran_id() + "";
                /*InpuDialog*/
                listItems.remove(position);
                findVendorTask.execute(userId, vendorId, traId, cancelReason);
                bookingAppoinementModel.setIsCancelled(1);
                dialog.cancel();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        if (hideFeatures) {
            holder.cancel_tv.setEnabled(false);
            holder.accept_tv.setEnabled(false);
            holder.completed_tv.setEnabled(false);
        }

        holder.cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Cancel Action*/
                builder.show();
            }
        });

        holder.accept_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Accept Action*/
                holder.addtionalfeatures.setVisibility(View.VISIBLE);
                AcceptTask acceptTask = new AcceptTask();
                acceptTask.execute(bookingAppoinementModel.getTran_id()+"");
                System.out.println("Accepted");
                bookingAppoinementModel.setIsAccepted(1);
                notifyDataSetChanged();
            }
        });
        holder.completed_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Booking completed*/
                if (bookingAppoinementModel.getIsAccepted() == 1) {
                    String id = bookingAppoinementModel.getTran_id()+"";
                    String modeOfPayment = bookingAppoinementModel.getModeOfPayment();
                    String mobile = bookingAppoinementModel.getCustomerMobile();
                    String amount = bookingAppoinementModel.getAmount() + "";
                    Completed completed = new Completed();
                    completed.execute(id, modeOfPayment, mobile, amount);
                }else {
                    Toast.makeText(context, "Please Accept Booking First!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.map_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*give intent to map*/
                double lat = bookingAppoinementModel.getCustomerLat();
                double lot = bookingAppoinementModel.getCustomerLng();
                // Create a Uri from an intent string. Use the result to create an Intent.
                //Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");

                /*String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 37.7749, -122.4194);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);*/

                /*google.navigation:q=a+street+address
google.navigation:q=latitude,longitude*/

                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lot);
// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
// Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

// Attempt to start an activity that can handle the Intent
                context.startActivity(mapIntent);

            }
        });



    }public static int convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return (int)dp;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView bookingDate, bookingTime, bookingAddress, bookingAmount, bookingmode, bookingid, serviceNameTv, cancel_tv;
        TextView qty_tv, customerName_tv, contact_tv, accept_tv, status_tv;
        TextView dummpyFeild_tv;
        LinearLayout addtionalfeatures, basicFateure;
        TextView completed_tv, map_tv;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookingDate = itemView.findViewById(R.id.bookingDate);
            bookingTime = itemView.findViewById(R.id.bookingTime);
            bookingAddress = itemView.findViewById(R.id.bookingAddress);
            bookingAmount = itemView.findViewById(R.id.appointmentAmount);
            bookingmode = itemView.findViewById(R.id.bookingmode);
            bookingid = itemView.findViewById(R.id.bookingid);
            serviceNameTv = itemView.findViewById(R.id.serviceNameTv);
            cancel_tv = itemView.findViewById(R.id.vendorBook_cancel);
            accept_tv = itemView.findViewById(R.id.vendorBook_accept);
            qty_tv = itemView.findViewById(R.id.appointmentQty);
            customerName_tv = itemView.findViewById(R.id.bookingCustomerName);
            contact_tv = itemView.findViewById(R.id.bookingContact);
            status_tv = itemView.findViewById(R.id.bookingStatus);
            addtionalfeatures =  itemView.findViewById(R.id.vendorBook_addtionalfeatures);
            completed_tv= itemView.findViewById(R.id.vendorBook_completed);
            map_tv = itemView.findViewById(R.id.vendorBook_map);
            itemView = itemView.findViewById(R.id.vendorBooking_ImageView);
            basicFateure = itemView.findViewById(R.id.vendorBooking_basicfaetures);

        }
    }


    private class AcceptTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Accepting");
            progress.setMessage("Please wait ...");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{
                VendorDatabaseHelper dataBaseHelper = new VendorDatabaseHelper();
                dataBaseHelper.acceptBooking(strings[0]);
            }catch (Exception e){
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();

            }
            return "";
        }
        protected void onPostExecute(String msg){

            progress.dismiss();

        }
    }

    private class Completed extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;
        String modeOfpayment, mobile, amount;

        protected void onPreExecute(){
            progress = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Completed");
            progress.setMessage("Please wait ...");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{
                VendorDatabaseHelper dataBaseHelper = new VendorDatabaseHelper();
                dataBaseHelper.completeBooking(strings[0]);

                modeOfpayment = strings[1];
                mobile = strings[2];
                amount = strings[3];
                /*Completed Booking msg*/

            }catch (Exception e){
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();

            }
            return "";
        }
        protected void onPostExecute(String msg){

            progress.dismiss();
            if (modeOfpayment.equalsIgnoreCase("cash")) {
                MessageTask messageTask = new MessageTask();
                messageTask.execute(mobile, amount);
            }

        }
    }
    class MessageTask extends AsyncTask<String, String, String>{

        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){

            progress = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("SMS");
            progress.setMessage("Sending payment sms to customer");
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... uri) {
            String responseString = null;

            /*0->mobile
            * 1->amount*/
            int amount = Integer.valueOf(uri[1]);
            String customerMsgTxt = "Please pay Rs "+ amount + " to Perfect Punters for 'Service Complition' . Thanks.";
            String customerMsg = "http://admagister.net/api/mt/SendSMS?user=PERPUN2020&password=PERPUN2020&senderid=PERPUN&channel=trans&DCS=0&flashsms=0&number="+ uri[0] +
                    "&text="+ customerMsgTxt +"&route=14";

            Log.i("CustomerNumber", uri[0]);

            try {
                URL url = new URL(customerMsg);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK){
                    // Do normal input or output stream reading
                    Log.i("CustomerMsg", "Done");
                }
                else {
                    Log.i("CustomerMsg", "Failed"); // See documentation for more info on response handling
                }
            }  catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            progress.dismiss();
            Toast.makeText(context, "Service Completed", Toast.LENGTH_SHORT).show();
        }
    }


    private class FindVendorTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;
        VendorProfileModel selectedVendor = null;
        VendorDatabaseHelper vendorDatabaseHelper = new VendorDatabaseHelper();
        String tranId = "", cancelReason = "";
        protected void onPreExecute(){
            progress = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Cancelling");
            progress.setMessage("Transferring your booking..");
            progress.setIndeterminate(true);
            progress.show();

        }

        protected String doInBackground(String... strings){
            try{

                //vendorDatabaseHelper.deleteVendor();

                //vendorDatabaseHelper.setVendorActivity(17, 1, 1, 0, 0,"reg","", "", "");

                /*Get city name from userid*/


                /*Striing 0-> userId
                * 1->vendor Id
                * 2->tranId
                * 3 - Cancel reason*/
                tranId = strings[2];
                cancelReason = strings[3];
                String cityName = vendorDatabaseHelper.cityName(strings[0]);
                ArrayList<VendorProfileModel> vendorProfileModelArrayList = vendorDatabaseHelper.getVendorDetailsForBooking(cityName);


                if (vendorProfileModelArrayList.size() == 0 ){
                    //Toast.makeText(AddressActivity.this, , Toast.LENGTH_SHORT).show();
                    msg = "Sorry! No vendor available near by";

                }else {
                    /*some vendor are there*/
                    System.out.println("Some vendor are present near by "+ vendorProfileModelArrayList.size());

                    LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(context);
                    List<CatSubChildModel> cartDataModels = localDatabaseHelper.fetchCartData();

                    ArrayList<VendorProfileModel> vendorProvideService= new ArrayList<>();

                    /*Find one vendor sutitable for all the service need by user*/
                    for (VendorProfileModel vendorProfileModel: vendorProfileModelArrayList) {

                        int count = 0;
                        for (CatSubChildModel catSubChildModel: cartDataModels) {
                            if (catSubChildModel.getCatId() == vendorProfileModel.getCatId() && catSubChildModel.getSubCatId() == vendorProfileModel.getSubId()){
                                count++;
                            }
                        }
                        if (count == cartDataModels.size()) {
                            /*vendor is providing service needed by user*/
                            vendorProvideService.add(vendorProfileModel);
                        }

                    }

                    ArrayList<VendorProfileModel> newArrayList = new ArrayList<>();
                    for (VendorProfileModel vendorProfileModelL :vendorProvideService)  {
                        if (vendorProfileModelL.getVendorId().equals(strings[1])) {
                            //vendorProfileModelArrayList.remove(vendorProfileModelL);
                            System.out.println("Removing: " + vendorProfileModelL.getVendorId());
                        }else {
                            newArrayList.add(vendorProfileModelL);
                        }
                    }

                    vendorProvideService.clear();
                    vendorProvideService.addAll(newArrayList);

                    DistanceHelper distanceHelper = new DistanceHelper();

                    String userLatLog[] = vendorDatabaseHelper.userLatLng(strings[0]).split(",");
                    double userLat = Double.parseDouble(userLatLog[0]);
                    double userLot = Double.parseDouble(userLatLog[1]);

                    vendorProfileModelArrayList  = distanceHelper.getUserVendorDistance(vendorProvideService, userLat, userLot);

                    Collections.sort(vendorProfileModelArrayList, new DistanceComparator());

                    ArrayList<VendorProfileModel> distanceFillteredVendors = new ArrayList<>();

                    for (VendorProfileModel model: vendorProfileModelArrayList) {
                        if (model.getDistanceFromCustomer() <= Constant.VENDOR_RADIUS) {
                            distanceFillteredVendors.add(model);

                        }
                        System.out.println(model.getDistanceFromCustomer());
                    }

                    if (distanceFillteredVendors.size() == 0) {
                        msg = "Sorry! No vendor available near by";
                        //Toast.makeText(AddressActivity.this, "Sorry! No vendor available near by", Toast.LENGTH_SHORT).show();

                    }else{
                        /*Arranging vendors according to decending coins from vendor*/

                        for (VendorProfileModel vendorProfileModel: distanceFillteredVendors) {
                            if (vendorProfileModel.getCoins() >= Constant.VENDOR_MIN_COINS) {
                                selectedVendor = vendorProfileModel;
                                break;
                            }
                        }

                        if (selectedVendor == null) {
                            msg = "Sorry! No vendor available near by";
                            //Toast.makeText(AddressActivity.this, "Sorry! No vendor available near by", Toast.LENGTH_SHORT).show();
                        }else{
                            System.out.println("Selected vendorId: " + selectedVendor.getVendorId());
                            String vendorId = selectedVendor.getVendorId();
                            String vendorMobile = selectedVendor.getPhone();
                            /*vendor selected*/
                            /*dataBaseHelper.setAddress(addressModel, sharedPreferenceHelper.getUserid());
                            sharedPreferenceHelper.setAddress(addressModel);*/
                            Log.i("Date", "Distributed");

                        }


                    }


                }

            }catch (Exception e){
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();

            }
            return "";
        }
        protected void onPostExecute(String msg){

            progress.dismiss();
            try {
                if (selectedVendor!=null){
                    /*Profile model*/
                    vendorDatabaseHelper.cancelBooking(tranId, cancelReason,  selectedVendor.getVendorId() );

                }else{
                    vendorDatabaseHelper.cancelBooking(tranId, cancelReason,null );
                    //Toast.makeText(SelectModeOfPayment.this, "Sorry! No vendor near by", Toast.LENGTH_SHORT).show();
                }
                System.out.println("Booking transfered and canceled");

            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
            notifyDataSetChanged();
        }
    }
}

