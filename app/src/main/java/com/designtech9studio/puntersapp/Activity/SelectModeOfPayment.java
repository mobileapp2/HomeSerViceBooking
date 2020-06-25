package com.designtech9studio.puntersapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Helpers.Constant;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.DistanceComparator;
import com.designtech9studio.puntersapp.Helpers.DistanceHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.LocalDatabaseHelper;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.AddressModel;
import com.designtech9studio.puntersapp.Model.CartDataModel;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.Model.ProfileModel;
import com.designtech9studio.puntersapp.Model.VendorProfileModel;
import com.designtech9studio.puntersapp.PaymentActivity;
import com.designtech9studio.puntersapp.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SelectModeOfPayment extends AppCompatActivity {

    TextView paymentBtn, totalTextView;
    RadioGroup radioGroup;
    RadioButton cashRadio, onlineRadio;
    boolean cashPay = false;
    AlertDialog alert;
    ProfileModel profileModel;
    String selectedDate;
    String selectedTime;
    String vendorId = "";
    String vendorMobile = "";
    String userMobile = "";

    AddressModel addressModel;
    SharedPreferenceHelper sharedPreferenceHelper;
    DataBaseHelper dataBaseHelper;
    VendorProfileModel selectedVendor = null;
    double userLat, userLot;

    String selectedRadiotext = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode_of_payment);
        setTitle("Payment Mode");

        paymentBtn= findViewById(R.id.paymentBtn);
        totalTextView = findViewById(R.id.totalTextView);
        cashRadio = findViewById(R.id.cashOnDeliveryRadio);
        onlineRadio = findViewById(R.id.onlinePaymentRadio);
        radioGroup = findViewById(R.id.radioGroup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.addressToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();
        selectedDate = intent.getStringExtra(IntentsTags.DATE);
        selectedTime = intent.getStringExtra(IntentsTags.TIME);
        vendorId = "0";

        LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(getApplicationContext());
        final int amount = localDatabaseHelper.getGrandTotal();
        totalTextView.setText(""+amount);

        paymentBtn.setText("Pay "+ Html.fromHtml("&#x20B9;") +" "+amount+"/-");

        sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
        addressModel = sharedPreferenceHelper.getAddress();
        dataBaseHelper = new DataBaseHelper();

        userLat = Double.parseDouble(addressModel.getLat());
        userLot = Double.parseDouble(addressModel.getLot());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Log.i("BACK", "Detected");
            }
        });

        cashRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashPay = true;
                paymentBtn.setText("Book Now");
            }
        });

        onlineRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentBtn.setText("Pay "+ Html.fromHtml("&#x20B9;") +" "+amount+"/-");
            }
        });


/*        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name");        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_dailog_layout, null);
        builder.setView(customLayout);        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                Log.i("Alert", "Yes");
            }
        });        // create and show the alert dialog
        AlertDialog dialog = builder.create();*/
       // dialog.show();
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectModeOfPayment.this, R.style.AlertDialogTheme);
        builder.setMessage("Are you sure! you want to confirm")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        //confirmUserBooking(strings[0], strings[1], strings[1]);
                        SyncData syncData = new SyncData();
                        syncData.execute(selectedDate, selectedTime, "Cash");

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"Booking Failed! Please try again..",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        //Creating dialog box
        alert = builder.create();
        //Setting the title manually
        alert.setTitle("Confirm Booking");


        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadio = findViewById(selectedId);

                if (selectedRadio == null) {
                    Toast.makeText(getApplicationContext(), "Please select payment option!", Toast.LENGTH_SHORT).show();
                }else{
                    String text = selectedRadio.getText().toString();
                    selectedRadiotext = text;
                    FindVendorTask findVendorTask = new FindVendorTask();
                    findVendorTask.execute();
                    /*if (text.equals("   Cash on Delivery")) {

                        // create an alert builder
                        alert.show();
                        Log.i("CashSelected", "Hello");

                        //confirmUserBooking(selectedDate, selectedTime, "Cash");
                    }else{
                        Log.i("Online", "Payment");
                        ProfileData profileData = new ProfileData();
                        profileData.execute();
                    }*/

                }
            }
        });
    }




    private void confirmUserBooking(String selectedDate, String selectedTime, String modeOfPayment) {
        SharedPreferenceHelper sharedPreferenceHelper= new SharedPreferenceHelper(getApplicationContext());

        DataBaseHelper dataBaseHelper = new DataBaseHelper();
        String userId = sharedPreferenceHelper.getUserid();
        //ProfileModel profileModel = dataBaseHelper.getProfileData(vendorId);
        AddressModel addressModel = sharedPreferenceHelper.getAddress();
        String streetAddress = addressModel.toString();
        LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(getApplicationContext());
        int grossTotal = localDatabaseHelper.getGrandTotal();

        List<CatSubChildModel> cartData =  localDatabaseHelper.fetchCartData();
        /* setBookingTable(String vendorId, List<CatSubChildModel> cartDataModels,String selectedDate, String selectedTime, String address, String mode, String vendorId)*/
        dataBaseHelper.setBookingTable(userId,cartData, selectedDate, selectedTime, streetAddress, "CASH", vendorId);
        //dataBaseHelper.setBookingTable(vendorId, cartData, grossTotal, selectedDate, selectedTime, streetAddress, "CASH", "Active");
        Log.i("Insertion", "Completed");
        localDatabaseHelper.flushCart();

        /*send Confirm booking sms*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class SyncData extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress = null;

        protected void onPreExecute(){

            progress = new ProgressDialog(SelectModeOfPayment.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{

                confirmUserBooking(strings[0], strings[1], strings[1]);
            }catch (Exception e){
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();

            }
            return "";
        }
        protected void onPostExecute(String msg){

            if (progress!=null)
            progress.dismiss();
            try {
                MessageTask messageTask = new MessageTask();
                messageTask.execute(userMobile, vendorMobile);

            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }

    private class ProfileData extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress = null;

        protected void onPreExecute(){

            progress = new ProgressDialog(SelectModeOfPayment.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{
                SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
                String userId = sharedPreferenceHelper.getUserid();
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                profileModel = dataBaseHelper.getProfileData(userId);
            }catch (Exception e){
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();

            }
            return "";
        }
        protected void onPostExecute(String msg){

            if (progress!=null)
                progress.dismiss();
            try {

                String mobile = profileModel.getPhone();
                if (mobile.equals("null") || mobile.equals("") || mobile.length()!=10) {
                    Toast.makeText(SelectModeOfPayment.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent1 = new Intent(getApplicationContext(), PaymentActivity.class);
                    intent1.putExtra(IntentsTags.DATE, selectedDate);
                    intent1.putExtra(IntentsTags.TIME, selectedTime);
                    intent1.putExtra(IntentsTags.VENDOR_ID, vendorId);
                    startActivity(intent1);
                }


            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }

    private class ProfileDataCash extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress = null;

        protected void onPreExecute(){

            progress = new ProgressDialog(SelectModeOfPayment.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{
                SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
                String userId = sharedPreferenceHelper.getUserid();
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                profileModel = dataBaseHelper.getProfileData(userId);
            }catch (Exception e){
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();

            }
            return "";
        }
        protected void onPostExecute(String msg){

            if (progress!=null)
                progress.dismiss();
            try {

                String mobile = profileModel.getPhone();
                if (mobile.equals("null") || mobile.equals("") || mobile.length()!=10) {
                    Toast.makeText(SelectModeOfPayment.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                }else {
                    userMobile = mobile;
                    alert.show();
                }



            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }
    private class FindVendorTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(SelectModeOfPayment.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Finding");
            progress.setMessage("Near by service provider!");
            progress.setIndeterminate(true);
            progress.show();

        }

        protected String doInBackground(String... strings){
            try{

                VendorDatabaseHelper vendorDatabaseHelper = new VendorDatabaseHelper();

                //vendorDatabaseHelper.deleteVendor();

                //vendorDatabaseHelper.setVendorActivity(17, 1, 1, 0, 0,"reg","", "", "");
                ArrayList<VendorProfileModel> vendorProfileModelArrayList = vendorDatabaseHelper.getVendorDetailsForBooking(addressModel.getCityName());


                if (vendorProfileModelArrayList.size() == 0 ){
                    //Toast.makeText(AddressActivity.this, , Toast.LENGTH_SHORT).show();
                    msg = "Sorry! No vendor available near by";

                }else {
                    /*some vendor are there*/
                    System.out.println("Some vendor are present near by "+ vendorProfileModelArrayList.size());

                    LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(getApplicationContext());
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



                    DistanceHelper distanceHelper = new DistanceHelper();

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
                            vendorId = selectedVendor.getVendorId();
                            vendorMobile = selectedVendor.getPhone();
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
                    /*push vendor Mobile in shared pref*/
                    sharedPreferenceHelper.setVendorMobile(vendorMobile);
                    if (selectedRadiotext.equals("   Cash on Delivery")) {

                        // create an alert builder
                        ProfileDataCash profileData = new ProfileDataCash();
                        profileData.execute();


                        Log.i("CashSelected", "Hello");


                        //confirmUserBooking(selectedDate, selectedTime, "Cash");
                    }else{
                        Log.i("Online", "Payment");
                        ProfileData profileData = new ProfileData();
                        profileData.execute();
                    }


                   /* Intent intent = new Intent(getApplicationContext(), MySchedule.class);
                    intent.putExtra(IntentsTags.VENDOR_ID, selectedVendor.getVendorId());
                    startActivity(intent);*/
                }else{
                    Toast.makeText(SelectModeOfPayment.this, "Sorry! No vendor near by", Toast.LENGTH_SHORT).show();
                }


            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }


    class MessageTask extends AsyncTask<String, String, String>{

        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){

            progress = new ProgressDialog(SelectModeOfPayment.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... uri) {
            String responseString = null;

            int amount = 0;
            String customerMsgTxt = "Your booking has been confirmed with Perfect Punters, kindly check the Booking page on App for confirmation.\nThanks.";
            String customerMsg = "http://admagister.net/api/mt/SendSMS?user=PERPUN2020&password=PERPUN2020&senderid=PERPUN&channel=trans&DCS=0&flashsms=0&number="+ uri[0] +
                    "&text="+ customerMsgTxt +"&route=14";

            String vendorMsg = "http://admagister.net/api/mt/SendSMS?user=PERPUN2020&password=PERPUN2020&senderid=PERPUN&channel=trans&DCS=0&flashsms=0&number="+ uri[1] +
                    "&text=You have a new booking in Perfect Punter App.&route=14";

            Log.i("CustomerNumber", uri[0]);
            Log.i("VendorNumber", uri[1]);

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
            try {
                URL url = new URL(vendorMsg);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK){
                    // Do normal input or output stream reading
                    Log.i("VendorMsg", "Done");
                }
                else {
                    Log.i("VendorMsg", "Failed"); // See documentation for more info on response handling
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

            Toast.makeText(getApplicationContext(), "Booking Confirmed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SelectModeOfPayment.this, ClientHomepageActivity.class);
            startActivity(intent);
        }
    }



}
