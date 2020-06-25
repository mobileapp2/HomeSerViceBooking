package com.designtech9studio.puntersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Activity.ClientHomepageActivity;
import com.designtech9studio.puntersapp.Activity.ClientProfileActivity;
import com.designtech9studio.puntersapp.Activity.MainActivity;
import com.designtech9studio.puntersapp.Activity.SelectModeOfPayment;
import com.designtech9studio.puntersapp.Helpers.DataBaseHelper;
import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.LocalDatabaseHelper;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Model.AddressModel;
import com.designtech9studio.puntersapp.Model.CatSubChildModel;
import com.designtech9studio.puntersapp.Model.ProfileModel;
import com.instamojo.android.Instamojo;
import com.instamojo.android.InstamojoApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class PaymentActivity extends AppCompatActivity {

    ProfileModel profileModel;
    String selectedDate, selectedTime, vendorId, vendorPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent intent = getIntent();
        selectedDate = intent.getStringExtra(IntentsTags.DATE);
        selectedTime = intent.getStringExtra(IntentsTags.TIME);
        vendorId = intent.getStringExtra(IntentsTags.VENDOR_ID);

        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
        vendorPhone = sharedPreferenceHelper.getVENDOR_Mobile();
        Log.i("VendorMobile", vendorPhone);

        SyncData data = new SyncData();
        data.execute();
        /*SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
        String userId = sharedPreferenceHelper.getUserid();
        DataBaseHelper dataBaseHelper = new DataBaseHelper();
        profileModel = dataBaseHelper.getProfileData(userId);

        profileModel = dataBaseHelper.getProfileData(userId);
        LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(getApplicationContext());
        int total = localDatabaseHelper.getGrandTotal();

        System.out.println("PAYMENT email: " + profileModel.getEmail() +" phone: " + profileModel.getPhone() + " Amount: " + total);
        callInstamojoPay(profileModel.getEmail(), profileModel.getPhone(), String.valueOf(total), "Booking of amenities", "HomeService");*/

    }
    private class SyncData extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(PaymentActivity.this,R.style.AppCompatAlertDialogStyle);
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
                //profileModel = dataBaseHelper.getProfileData(userId);

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
                LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(getApplicationContext());
                int total = localDatabaseHelper.getGrandTotal();
                String email = profileModel.getEmail();
                String phone = profileModel.getPhone();
                if (email.equals("null") || email.equals(""))
                    email = "eample123456@gmail.com";//dummy email id

                callInstamojoPay(email, phone, String.valueOf(total), "Booking of amenities", "HomeService");

            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }


    InstamojoPay instamojoPay;
    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;

        Log.i("PAYMENT", "CAlled");
        instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        Log.i("PAYMENT", "CAlled2");
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;
    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
                        .show();

                ConfimBookingTask confimBookingTask = new ConfimBookingTask();
                confimBookingTask.execute(selectedDate, selectedTime, "Online");
                //confirmUserBooking(selectedDate, selectedTime, "Online");

                //Log.i("PAYMENT", "COmpleted");

                /*Send SMS*/
                /*MessageTask messageTask = new MessageTask();
                messageTask.execute(profileModel.getPhone(), vendorPhone);
                Intent intent = new Intent(getApplicationContext(), ClientHomepageActivity.class);
                startActivity(intent);
                finish();*/
            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
                Log.i("PAYMENT", reason);
                Intent intent = new Intent(getApplicationContext(), ClientHomepageActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    private void confirmUserBooking(String selectedDate, String selectedTime, String modeOfPayment) {

        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
        String userId = sharedPreferenceHelper.getUserid();
        //ProfileModel profileModel = dataBaseHelper.getProfileData(userId);
        AddressModel addressModel = sharedPreferenceHelper.getAddress();
        String streetAddress = addressModel.toString();
        LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(getApplicationContext());
        int grossTotal = localDatabaseHelper.getGrandTotal();

        List<CatSubChildModel> cartData =  localDatabaseHelper.fetchCartData();
        DataBaseHelper dataBaseHelper = new DataBaseHelper();

        dataBaseHelper.setBookingTable(userId, cartData, selectedDate, selectedTime, streetAddress, modeOfPayment, vendorId);

        //dataBaseHelper.setBookingTable(userId, cartData, grossTotal, selectedDate, selectedTime, streetAddress, modeOfPayment, "Active");
        Log.i("Insertion", "Payment Online Done");
        localDatabaseHelper.flushCart();
    }

    @Override
    protected void onDestroy() {
        if (instamojoPay != null)
            unregisterReceiver(instamojoPay);
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class MessageTask extends AsyncTask<String, String, String>{

        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){

            progress = new ProgressDialog(PaymentActivity.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait..");
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... uri) {
            String responseString = null;

            String customerMsg = "http://admagister.net/api/mt/SendSMS?user=PERPUN2020&password=PERPUN2020&senderid=PERPUN&channel=trans&DCS=0&flashsms=0&number="+ uri[0] +
                    "&text=Your booking have been confirmed by Punter App.&route=14";

            String vendorMsg = "http://admagister.net/api/mt/SendSMS?user=PERPUN2020&password=PERPUN2020&senderid=PERPUN&channel=trans&DCS=0&flashsms=0&number="+ uri[1] +
                    "&text=You have a new booking in Punter App.&route=14";

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
            Intent intent = new Intent(PaymentActivity.this, ClientHomepageActivity.class);
            startActivity(intent);
        }
    }

    private class ConfimBookingTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress = null;

        protected void onPreExecute(){

            progress = new ProgressDialog(PaymentActivity.this,R.style.AppCompatAlertDialogStyle);
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
                messageTask.execute(profileModel.getPhone(), vendorPhone);
            }catch (Exception ex){
                System.out.println("Exception MainPage "+ex.getMessage());
            }
        }
    }
}
