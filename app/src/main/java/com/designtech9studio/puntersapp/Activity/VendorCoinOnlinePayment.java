package com.designtech9studio.puntersapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.designtech9studio.puntersapp.Helpers.IntentsTags;
import com.designtech9studio.puntersapp.Helpers.SharedPreferenceHelper;
import com.designtech9studio.puntersapp.Helpers.VendorDatabaseHelper;
import com.designtech9studio.puntersapp.Model.ProfileModel;
import com.designtech9studio.puntersapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class VendorCoinOnlinePayment extends AppCompatActivity {

    ProfileModel profileModel;
    String vendorId;
    int total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_coin_online_payment);
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
        vendorId = sharedPreferenceHelper.getVendorId();
        total = Integer.valueOf(getIntent().getStringExtra(IntentsTags.VENDOR_CART_AMOUNT));
        SyncData data = new SyncData();

        data.execute();
    }
    private class SyncData extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(VendorCoinOnlinePayment.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.show();
        }

        protected String doInBackground(String... strings){
            try{
                VendorDatabaseHelper databaseHelper = new VendorDatabaseHelper();
                profileModel = databaseHelper.getVendorProfile(vendorId);

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

                int total = Integer.valueOf(getIntent().getStringExtra(IntentsTags.VENDOR_CART_AMOUNT));
                String email = profileModel.getEmail();
                String phone = profileModel.getPhone();
                if (email.equals("null") || email.equals(""))
                    email = "eample123456@gmail.com";//dummy email id
                System.out.println("Mobile: " + phone + " email: " + email + " Total: " + total);
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
            Log.i("PAYMENT", "Data problem " + e.getMessage());
            //e.printStackTrace();

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

                RechargeTask rechargeTask = new RechargeTask();
                rechargeTask.execute(vendorId, String.valueOf(total), "Online");

                Log.i("PAYMENT", "COmpleted");

            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
                Log.i("PAYMENT", reason);
                Intent intent = new Intent(getApplicationContext(), VendorNewLeadActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }
    private class RechargeTask extends AsyncTask<String, String, String >
    {
        String msg = "Internet/DB_Credentials/Windows_Firewall_TurnOn Error, See Android Monitor in the bottom for details!";
        ProgressDialog progress;

        protected void onPreExecute(){
            progress = new ProgressDialog(VendorCoinOnlinePayment.this,R.style.AppCompatAlertDialogStyle);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
        }

        protected String doInBackground(String... strings){
            try{

                VendorDatabaseHelper databaseHelper = new VendorDatabaseHelper();
                /*vendorId Cart_Amount PAYMENT_MODE*/
                databaseHelper.purchaseCoins(Integer.valueOf(strings[0]), Integer.valueOf(strings[1]),  strings[2]);
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

            Toast.makeText(VendorCoinOnlinePayment.this, "Transaction completed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VendorCoinOnlinePayment.this, VendorNewLeadActivity.class);
            startActivity(intent);
            fileList();
        }
    }
}
